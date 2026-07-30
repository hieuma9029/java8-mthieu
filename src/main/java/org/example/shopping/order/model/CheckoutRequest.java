package org.example.shopping.order.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/** Dữ liệu frontend gửi khi khách xác nhận đặt hàng từ giỏ hàng. */
public class CheckoutRequest {

    /** Tên người nhận đơn hàng. */
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    /** Email liên hệ của người nhận. */
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String customerEmail;

    /** Số điện thoại liên hệ của người nhận. */
    @NotBlank(message = "Số điện thoại không được để trống")
    private String customerPhone;

    /** Địa chỉ giao hàng của đơn. */
    @NotBlank(message = "Địa chỉ không được để trống")
    private String customerAddress;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

}
