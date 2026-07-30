package org.example.shopping.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

/**
 * DTO dùng để nhận thông tin đăng nhập từ frontend.
 * Lớp này chứa username và password để controller chuyển tới tầng xác thực.
 */
public class LoginRequest {
    @JsonProperty("username")
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán userName và password.
     * Tham số của mỗi setter là giá trị mới cho thuộc tính có cùng tên.
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
}