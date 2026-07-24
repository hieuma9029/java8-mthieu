package org.example.shopping.model;

import java.util.List;

/** Danh sách các dòng hàng và tổng tiền của một đơn. */
public class OrderDetailsResponse {
    /** Các sản phẩm đã được chốt trong đơn hàng. */
    private List<OrderItemResponse> items;
    /** Tổng tiền của đơn hàng. */
    private Double totalAmount;

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
