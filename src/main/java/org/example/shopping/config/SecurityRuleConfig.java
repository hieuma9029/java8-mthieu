package org.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Cấu hình các bean liên quan đến mật khẩu và mã hóa bảo mật.
 */
@Configuration
public class SecurityRuleConfig {

    /**
     * Tạo bộ mã hóa BCrypt dùng để lưu và đối chiếu mật khẩu.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
