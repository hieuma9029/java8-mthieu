    package org.example.shopping.entity;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import javax.persistence.*;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.Collections;

    @Entity
    @Table(name = "accounts")
    /**
     * Entity ánh xạ bảng accounts và là thông tin người dùng cho Spring Security.
     * UserDetails cho phép Spring Security lấy username, password, trạng thái và quyền.
     */
    public class Accounts implements UserDetails {

        /** Khóa chính tự tăng của tài khoản. */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        /** Tên người dùng dùng để đăng nhập. */
        @Column(name = "user_name")
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

        /** Cờ xóa mềm; bản ghi có thể vẫn còn trong database. */
        @Column(name = "is_delete")
        private Boolean isDelete;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

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

        public Boolean getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Boolean isDelete) {
            this.isDelete = isDelete;
        }

        public LocalDateTime getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        /** Chuyển userRole thành quyền mà Spring Security dùng khi phân quyền. */
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(
                    new SimpleGrantedAuthority(userRole)
            );
        }

        @Override
        /** Trả về mật khẩu mã hóa để Spring Security đối chiếu khi đăng nhập. */
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
