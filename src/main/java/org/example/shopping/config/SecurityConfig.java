package org.example.shopping.config;

import org.example.shopping.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import javax.servlet.http.HttpServletResponse;

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
     * Tạo provider xác thực bằng dữ liệu người dùng trong cơ sở dữ liệu.
     *
     * @return provider đã gắn dịch vụ người dùng và bộ mã hóa mật khẩu
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        // Khởi tạo provider chịu trách nhiệm kiểm tra thông tin đăng nhập.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Chỉ định service dùng để tìm người dùng theo username.
        provider.setUserDetailsService(userDetailsService);
        // Chỉ định thuật toán mã hóa dùng để đối chiếu mật khẩu.
        provider.setPasswordEncoder(passwordEncoder);
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authenticationProvider)
            throws Exception {
        http
                .cors().and()
                // Dùng CSRF token vì xác thực được lưu trong session cookie.
                // Frontend đọc cookie XSRF-TOKEN và gửi lại qua header X-XSRF-TOKEN.
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                // Đăng ký provider xác thực đã cấu hình ở trên.
                .authenticationProvider(authenticationProvider)
                // Tắt form login để sử dụng API JSON.
                .formLogin().disable()
                .logout().disable()
                // Khai báo quyền truy cập cho từng nhóm đường dẫn.
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/auth/login", "/auth/logout", "/auth/csrf").permitAll()
                        .antMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/orders/**").authenticated()
                        .antMatchers(HttpMethod.POST, "/orders/checkout").permitAll()
                        .antMatchers(HttpMethod.POST, "/cart/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/cart/**").permitAll()
                        .antMatchers(HttpMethod.PUT, "/cart/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/cart/**").permitAll()
                        .antMatchers("/auth/me", "/auth/profile").authenticated()
                        .antMatchers("/accounts/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                );
        return http.build();
    }

}
