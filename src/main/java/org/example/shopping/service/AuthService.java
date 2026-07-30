package org.example.shopping.service;

import org.example.shopping.model.LoginRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Service chuyên trách cho các thao tác xác thực, đăng xuất và cập nhật hồ sơ người dùng.
 */
public interface AuthService {

    /** Xác thực người dùng bằng username và password. */
    ResponseEntity<Map<String, Object>> login(LoginRequest request);

    /** Đăng xuất người dùng khỏi phiên hiện tại. */
    ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response);

    /** Lấy thông tin người dùng đang đăng nhập. */
    ResponseEntity<Map<String, Object>> getCurrentUser();

    /** Cập nhật profile cho người dùng đang đăng nhập. */
    ResponseEntity<Map<String, Object>> updateProfile(Map<String, Object> profileData);
}
