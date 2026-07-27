package org.example.shopping.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import org.example.shopping.entity.OrderStatus;

@Entity
@Table(name = "orders")
/**
 * Entity ánh xạ bảng orders, lưu thông tin chung của một đơn hàng.
 * Bao gồm thông tin khách hàng, tổng tiền, thời gian đặt hàng và trạng thái xử lý.
 */
public class Orders extends BaseEntity {
    /** Khóa chính tự tăng. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Mã/số hiển thị của đơn hàng. */
    @Column(name = "order_num")
    private Integer orderNum;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_address")
    private String customerAddress;

    /** Thời điểm đặt đơn. */
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    /** Trạng thái hiện tại của đơn hàng, ví dụ: chờ xác nhận, đã giao, đã nhận. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    /** Các trường xóa mềm và lịch sử tạo/cập nhật bản ghi. */
    /** Các getter/setter bên dưới đọc hoặc thay đổi dữ liệu đơn hàng. */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
