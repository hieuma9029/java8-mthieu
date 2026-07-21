package org.example.shopping.config;

import org.example.shopping.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Đánh dấu đây là lớp chứa các cấu hình Spring Security.
@Configuration
public class SecurityConfig {

    // Service tải thông tin người dùng từ cơ sở dữ liệu khi đăng nhập.
    private final UserDetailsServiceImpl userDetailsService;

    // Spring tự tiêm UserDetailsServiceImpl để sử dụng trong quá trình xác thực.
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Tạo bean mã hóa mật khẩu bằng BCrypt để lưu và so sánh mật khẩu an toàn.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Tạo provider xác thực bằng dữ liệu người dùng lấy từ cơ sở dữ liệu.
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

    // Cung cấp AuthenticationManager để Spring Security điều phối quá trình xác thực.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Cấu hình chuỗi bộ lọc bảo mật áp dụng cho các HTTP request.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Tắt CSRF; thường phù hợp với API không dùng session hoặc môi trường phát triển.
                .csrf().disable()

                // Đăng ký provider xác thực đã cấu hình ở trên.
                .authenticationProvider(authenticationProvider())

                // Khai báo quyền truy cập cho từng nhóm đường dẫn.
                .authorizeHttpRequests(authorize -> authorize

                        // Cho phép mọi người truy cập trang đăng nhập.
                        .antMatchers("/login").permitAll()

                        // Chỉ người dùng có role ADMIN mới được truy cập các URL về sản phẩm.
                        .antMatchers("/products/**")
                        .hasRole("ADMIN")

                        // Cả ADMIN và USER đều được truy cập.
                        .antMatchers("/profile")
                        .hasAnyRole("ADMIN", "USER")

                        // Mọi request còn lại yêu cầu người dùng đã đăng nhập.
                        .anyRequest()
                        .authenticated()
                )

                // Bật cơ chế đăng nhập bằng form mặc định của Spring Security.
                .formLogin(form -> form

                        // Sau khi đăng nhập thành công sẽ chuyển đến /products.
                        .defaultSuccessUrl("/products", true)
                )

                // Cấu hình đăng xuất.
                .logout(logout -> logout

                        // URL thực hiện đăng xuất (mặc định là /logout).
                        .logoutUrl("/logout")

                        // Sau khi đăng xuất thành công sẽ quay về trang đăng nhập.
                        .logoutSuccessUrl("/login")

                        // Xóa Session hiện tại.
                        .invalidateHttpSession(true)

                        // Xóa thông tin Authentication.
                        .clearAuthentication(true)

                        // Xóa cookie JSESSIONID.
                        .deleteCookies("JSESSIONID")
                );

        // Hoàn tất cấu hình và tạo SecurityFilterChain để Spring sử dụng.
        return http.build();
    }
}