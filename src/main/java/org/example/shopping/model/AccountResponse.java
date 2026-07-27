package org.example.shopping.model;
/**
 * DTO dùng để trả dữ liệu tài khoản cho client.
 */
public class AccountResponse {
    private Integer id;

    private String userName;

    private String userRole;

    private Boolean active;

    /** Tên hiển thị của người dùng. */
    private String name;

    /** Email liên hệ của người dùng. */
    private String email;

    /** Số điện thoại liên hệ của người dùng. */
    private String phone;

    /** Địa chỉ liên hệ hoặc giao hàng mặc định của người dùng. */
    private String address;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán id, userName, userRole
     * và active của DTO phản hồi. Tham số của mỗi setter là giá trị mới cho
     * thuộc tính có cùng tên.
     */
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
}
