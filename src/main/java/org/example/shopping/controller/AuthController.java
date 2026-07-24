package org.example.shopping.controller;

import org.example.shopping.entity.Accounts;
import org.example.shopping.model.LoginRequest;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller xử lý các API xác thực và profile cho frontend.
 *
 * <p>Frontend React gọi các endpoint này để đăng nhập, kiểm tra người dùng hiện tại,
 * cập nhật profile và đăng xuất.</p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AuthController(AuthenticationManager authenticationManager,
                          AccountRepository accountRepository,
                          AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    /**
     * Xác thực người dùng theo tên đăng nhập và mật khẩu.
     *
     * @param request DTO chứa username và password
     * @return ResponseEntity chứa success và data user nếu đăng nhập thành công
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Accounts account = accountRepository.findByUserName(request.getUserName());
            Map<String, Object> result = buildSuccessResponse(account);
            return ResponseEntity.ok(result);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401)
                    .body(buildErrorResponse("Tên đăng nhập hoặc mật khẩu không đúng"));
        }
    }

    /**
     * Đăng xuất người dùng bằng cách xóa ngữ cảnh bảo mật hiện tại.
     *
     * @param request  đối tượng HTTP request
     * @param response đối tượng HTTP response
     * @return ResponseEntity xác nhận đã đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Đã đăng xuất");
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thông tin người dùng hiện đang đăng nhập.
     *
     * @return ResponseEntity chứa success và dữ liệu user, hoặc lỗi 401 nếu chưa đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(buildErrorResponse("Chưa đăng nhập"));
        }

        Accounts account = accountRepository.findByUserName(authentication.getName());
        if (account == null) {
            return ResponseEntity.status(404).body(buildErrorResponse("Không tìm thấy người dùng"));
        }

        return ResponseEntity.ok(buildSuccessResponse(account));
    }

    /**
     * Cập nhật thông tin profile của người dùng đang đăng nhập.
     *
     * @param profileData dữ liệu profile có thể bao gồm name, email, phone, address
     * @return ResponseEntity chứa success và dữ liệu user đã cập nhật
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> profileData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(buildErrorResponse("Chưa đăng nhập"));
        }

        Accounts account = accountRepository.findByUserName(authentication.getName());
        if (account == null) {
            return ResponseEntity.status(404).body(buildErrorResponse("Không tìm thấy người dùng"));
        }

        if (profileData.containsKey("name")) {
            account.setName(profileData.get("name").toString());
        }
        if (profileData.containsKey("email")) {
            account.setEmail(profileData.get("email").toString());
        }
        if (profileData.containsKey("phone")) {
            account.setPhone(profileData.get("phone").toString());
        }
        if (profileData.containsKey("address")) {
            account.setAddress(profileData.get("address").toString());
        }

        accountService.save(account);
        return ResponseEntity.ok(buildSuccessResponse(account));
    }

    /**
     * Tạo một cấu trúc JSON trả về theo format frontend mong muốn.
     *
     * @param account thông tin tài khoản cần trả về
     * @return map chứa success và dữ liệu user
     */
    private Map<String, Object> buildSuccessResponse(Accounts account) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        Map<String, Object> data = new HashMap<>();
        data.put("username", account.getUserName());
        data.put("role", account.getUserRole() != null && account.getUserRole().startsWith("ROLE_")
                ? account.getUserRole().substring(5)
                : account.getUserRole());
        data.put("name", account.getName());
        data.put("email", account.getEmail());
        data.put("phone", account.getPhone());
        data.put("address", account.getAddress());
        result.put("data", data);
        return result;
    }

    /**
     * Tạo response lỗi với thông điệp cụ thể.
     *
     * @param message nội dung lỗi
     * @return map chứa success=false và thông điệp lỗi
     */
    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
