package org.example.shopping.model;

import java.time.LocalDateTime;

/**
 * DTO dùng để trả dữ liệu đơn hàng.
 */
public class OrderResponse {
    private Integer id;

    private Integer orderNum;

    private String customerName;

    private Double amount;

    private LocalDateTime orderDate;

    /**
     * Các getter/setter bên dưới lần lượt đọc hoặc gán dữ liệu đơn hàng trong
     * DTO phản hồi. Tham số của mỗi setter là giá trị mới cho thuộc tính có
     * cùng tên.
     */
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
