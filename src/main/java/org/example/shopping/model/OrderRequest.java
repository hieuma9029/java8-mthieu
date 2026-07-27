package org.example.shopping.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import org.example.shopping.entity.OrderStatus;

/**
 * DTO dùng để nhận dữ liệu đơn hàng từ client khi tạo hoặc cập nhật đơn hàng.
 * Chứa thông tin khách hàng, tổng tiền và trạng thái đơn hàng.
 */
public class OrderRequest {
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @NotBlank(message = "Email không được để trống")
    private String customerEmail;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String customerPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String customerAddress;

    @NotNull(message = "Tổng tiền không được để trống")
    @Positive(message = "Tổng tiền phải lớn hơn 0")
    private BigDecimal amount;

    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private OrderStatus status;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán thông tin khách hàng
     * và tổng tiền của yêu cầu tạo/cập nhật đơn hàng. Tham số của mỗi setter
     * là giá trị mới cho thuộc tính có cùng tên.
     */
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
