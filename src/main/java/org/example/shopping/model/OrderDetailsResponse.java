package org.example.shopping.model;

import java.util.List;
import java.math.BigDecimal;

/** Danh sách các dòng hàng và tổng tiền của một đơn. */
public class OrderDetailsResponse {
    /** Các sản phẩm đã được chốt trong đơn hàng. */
    private List<OrderItemResponse> items;
    /** Tổng tiền của đơn hàng. */
    private BigDecimal totalAmount;

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
