package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.model.LoginRequest;
import org.example.shopping.model.RegisterRequest;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.example.shopping.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Hiện thực các nghiệp vụ xác thực và profile cho người dùng.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CartServiceImpl cartService;
    private final PasswordEncoder passwordEncoder;
    private final javax.servlet.http.HttpSession httpSession;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           AccountRepository accountRepository,
                           AccountService accountService,
                           CartServiceImpl cartService,
                           PasswordEncoder passwordEncoder,
                           javax.servlet.http.HttpSession httpSession) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
    }

    @Override
    /**
     * Xác thực người dùng bằng username/password và khởi tạo session cho phiên đăng nhập.
     *
     * @param request thông tin đăng nhập từ client
     * @return response chứa thông tin user khi đăng nhập thành công hoặc lỗi 401 khi sai thông tin
     */
    public ResponseEntity<Map<String, Object>> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(request.getUserName());
            // Merge anonymous session cart into account cart after successful login
            try {
                String sessionId = (String) httpSession.getAttribute("CART_ID");
                cartService.mergeSessionCartIntoAccount(sessionId, account);
            } catch (Exception ex) {
                // swallow merge errors to not block login; log if needed
            }
            return ResponseEntity.ok(buildSuccessResponse(account));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401)
                    .body(buildErrorResponse("Tên đăng nhập hoặc mật khẩu không đúng"));
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> register(RegisterRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(buildErrorResponse("Dữ liệu đăng ký không hợp lệ"));
        }

        Accounts existing = accountRepository.findByUserNameAndIsDeleteFalse(request.getUserName());
        if (existing != null) {
            return ResponseEntity.status(409).body(buildErrorResponse("Tên đăng nhập đã tồn tại"));
        }

        Accounts account = new Accounts();
        account.setUserName(request.getUserName());
        account.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
        account.setUserRole("ROLE_USER");
        account.setActive(true);
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAddress(request.getAddress());
        accountService.save(account);

        return ResponseEntity.status(201).body(buildSuccessResponse(account));
    }

    @Override
    /**
     * Đăng xuất người dùng bằng cách hủy session hiện tại và xoá context xác thực.
     *
     * @param request request HTTP hiện tại
     * @param response response HTTP hiện tại
     * @return response xác nhận đã đăng xuất
     */
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        if (request != null) {
            javax.servlet.http.HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        SecurityContextHolder.clearContext();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Đã đăng xuất");
        return ResponseEntity.ok(result);
    }

    @Override
    /**
     * Lấy thông tin người dùng đang đăng nhập từ SecurityContext hiện tại.
     *
     * @return response chứa profile người dùng hoặc lỗi 401/404 khi chưa đăng nhập hoặc không tìm thấy tài khoản
     */
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(buildErrorResponse("Chưa đăng nhập"));
        }

        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(authentication.getName());
        if (account == null) {
            return ResponseEntity.status(404).body(buildErrorResponse("Không tìm thấy người dùng"));
        }

        return ResponseEntity.ok(buildSuccessResponse(account));
    }

    @Override
    /**
     * Cập nhật thông tin hồ sơ cho tài khoản đang đăng nhập.
     *
     * @param profileData dữ liệu mới như tên, email, phone, address
     * @return response chứa thông tin tài khoản sau khi cập nhật
     */
    public ResponseEntity<Map<String, Object>> updateProfile(Map<String, Object> profileData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(buildErrorResponse("Chưa đăng nhập"));
        }

        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(authentication.getName());
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

        accountService.update(account.getId(), account);
        return ResponseEntity.ok(buildSuccessResponse(account));
    }

    /**
     * Tạo payload phản hồi thành công cho các endpoint xác thực và profile.
     *
     * @param account tài khoản đang xử lý
     * @return map dữ liệu chuẩn hóa để frontend sử dụng
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
     * Tạo payload phản hồi lỗi cho các endpoint xác thực.
     *
     * @param message thông báo lỗi cần trả về client
     * @return map chứa trạng thái thất bại và nội dung lỗi
     */
    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
