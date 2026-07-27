package org.example.shopping.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * DTO dùng để nhận dữ liệu tài khoản từ client.
 */
public class AccountRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Vai trò không được để trống")
    @Pattern(regexp = "ROLE_(ADMIN|USER)|ADMIN|USER", message = "Role must be ADMIN or USER")
    private String userRole;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán các trường userName,
     * password và userRole của yêu cầu tạo/cập nhật tài khoản. Tham số của
     * mỗi setter là giá trị mới cho thuộc tính có cùng tên.
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
