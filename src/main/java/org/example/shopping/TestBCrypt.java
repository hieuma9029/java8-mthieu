package org.example.shopping;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Lớp tiện ích để thử mã hóa mật khẩu bằng BCrypt trên console. */
public class TestBCrypt {
    /**
     * Điểm chạy độc lập để kiểm tra nhanh thuật toán mã hóa mật khẩu.
     *
     * @param args các đối số dòng lệnh, hiện không được sử dụng
     */
    public static void main(String[] args) {
        // Khởi tạo bộ mã hóa BCrypt giống bean PasswordEncoder trong cấu hình bảo mật.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // Mã hóa mật khẩu mẫu và in chuỗi hash để dùng khi tạo dữ liệu tài khoản thử nghiệm.
        System.out.println(encoder.encode("123456"));
    }
}
