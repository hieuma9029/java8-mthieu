    package org.example.shopping.entity;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import javax.persistence.*;
    import java.util.Collection;
    import java.util.Collections;
    @Entity
    @Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = "user_name"))
    /**
     * Entity ánh xạ bảng accounts và là thông tin người dùng cho Spring Security.
     * UserDetails cho phép Spring Security lấy username, password, trạng thái và quyền.
     */
    public class Accounts extends BaseEntity implements UserDetails {
        /** Khóa chính tự tăng của tài khoản. */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        /** Tên người dùng dùng để đăng nhập. */
        @Column(name = "user_name", nullable = false)
        private String userName;

        /** Mật khẩu đã được mã hóa, không lưu mật khẩu gốc. */
        @Column(name = "encrypted_password")
        private String encryptedPassword;

        /** Vai trò Spring Security, ví dụ ROLE_ADMIN hoặc ROLE_USER. */
        @Column(name = "user_role")
        private String userRole;

        /** Trạng thái cho phép đăng nhập của tài khoản. */
        @Column
        private Boolean active;

        /** Tên hiển thị của người dùng. */
        @Column
        private String name;

        /** Email của người dùng. */
        @Column
        private String email;

        /** Số điện thoại của người dùng. */
        @Column
        private String phone;

        /** Địa chỉ của người dùng. */
        @Column
        private String address;

        /** Cờ xóa mềm; bản ghi có thể vẫn còn trong database. */

        /** Các getter/setter bên dưới đọc hoặc cập nhật từng thuộc tính của entity. */
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @JsonIgnore
        public String getEncryptedPassword() {
            return encryptedPassword;
        }

        public void setEncryptedPassword(String encryptedPassword) {
            this.encryptedPassword = encryptedPassword;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        @Override
        /** Chuyển userRole thành quyền mà Spring Security dùng khi phân quyền. */
        @JsonIgnore
        public Collection<? extends GrantedAuthority> getAuthorities() {
            String roleAuthority = userRole;
            if (roleAuthority != null && !roleAuthority.startsWith("ROLE_")) {
                roleAuthority = "ROLE_" + roleAuthority;
            }
            return Collections.singletonList(
                    new SimpleGrantedAuthority(roleAuthority)
            );
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        /** Trả về mật khẩu mã hóa để Spring Security đối chiếu khi đăng nhập. */
        @JsonIgnore
        public String getPassword() {
            return encryptedPassword;
        }

        @Override
        /** Trả về định danh đăng nhập của người dùng. */
        public String getUsername() {
            return userName;
        }

        @Override
        /** Chưa áp dụng tính năng hết hạn tài khoản nên luôn hợp lệ. */
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        /** Chưa áp dụng khóa tài khoản nên luôn không bị khóa. */
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        /** Chưa áp dụng hết hạn mật khẩu nên luôn hợp lệ. */
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        /** Chỉ kích hoạt tài khoản khi active khác null và bằng true. */
        public boolean isEnabled() {
            return active != null && active;
        }
    }
