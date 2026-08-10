package org.example.shopping.controller;

import org.example.shopping.model.LoginRequest;
import org.example.shopping.model.RegisterRequest;
import org.example.shopping.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Xác thực người dùng theo tên đăng nhập và mật khẩu.
     *
     * @param request DTO chứa username và password
     * @return ResponseEntity chứa success và data user nếu đăng nhập thành công
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Đăng ký tài khoản mới cho người dùng.
     *
     * @param request thông tin đăng ký từ client
     * @return ResponseEntity chứa success và dữ liệu user sau khi tạo tài khoản
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
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
        return authService.logout(request, response);
    }

    /**
     * Lấy thông tin người dùng hiện đang đăng nhập.
     *
     * @return ResponseEntity chứa success và dữ liệu user, hoặc lỗi 401 nếu chưa đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        return authService.getCurrentUser();
    }

    /**
     * Cập nhật thông tin profile của người dùng đang đăng nhập.
     *
     * @param profileData dữ liệu profile có thể bao gồm name, email, phone, address
     * @return ResponseEntity chứa success và dữ liệu user đã cập nhật
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> profileData) {
        return authService.updateProfile(profileData);
    }
}
