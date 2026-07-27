package org.example.shopping.config;

import org.example.shopping.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

/** Cấu hình xác thực và phân quyền HTTP cho ứng dụng. */
@Configuration
public class SecurityConfig {
    // Service tải thông tin người dùng từ cơ sở dữ liệu khi đăng nhập.
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Khởi tạo cấu hình với dịch vụ tải thông tin người dùng.
     *
     * @param userDetailsService dịch vụ truy xuất người dùng khi xác thực
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Tạo bộ mã hóa BCrypt dùng để lưu và đối chiếu mật khẩu.
     *
     * @return bean mã hóa mật khẩu
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Tạo provider xác thực bằng dữ liệu người dùng trong cơ sở dữ liệu.
     *
     * @return provider đã gắn dịch vụ người dùng và bộ mã hóa mật khẩu
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Khởi tạo provider chịu trách nhiệm kiểm tra thông tin đăng nhập.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Chỉ định service dùng để tìm người dùng theo username.
        provider.setUserDetailsService(userDetailsService);
        // Chỉ định thuật toán mã hóa dùng để đối chiếu mật khẩu.
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Cung cấp trình quản lý xác thực cho Spring Security.
     *
     * @param configuration cấu hình xác thực do Spring cung cấp
     * @return trình quản lý xác thực
     * @throws Exception nếu Spring không thể tạo trình quản lý xác thực
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Cấu hình chuỗi bộ lọc bảo mật áp dụng cho các HTTP request.
     *
     * @param http đối tượng dùng để khai báo các quy tắc bảo mật HTTP
     * @return chuỗi bộ lọc bảo mật đã cấu hình
     * @throws Exception nếu không thể xây dựng chuỗi bộ lọc
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .cors().and()
                // Tắt CSRF vì frontend gọi API JSON và đang dùng CORS + cookie.
                .csrf().disable()
                // Đăng ký provider xác thực đã cấu hình ở trên.
                .authenticationProvider(authenticationProvider())
                // Tắt form login để sử dụng API JSON.
                .formLogin().disable()
                .logout().disable()
                // Khai báo quyền truy cập cho từng nhóm đường dẫn.
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/auth/login", "/auth/logout").permitAll()
                        .antMatchers("/auth/me", "/auth/profile").authenticated()
                        .antMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                );
        return http.build();
    }

    /**
     * Cấu hình CORS cho frontend React chạy tại localhost:3000.
     *
     * @return nguồn cấu hình CORS cho Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 
