package org.example.shopping.model;

import java.util.List;

/** Dữ liệu đầy đủ của giỏ hàng để frontend hiển thị. */
public class CartResponse {
    /** Các dòng sản phẩm trong giỏ hiện tại. */
    private List<CartItemResponse> items;
    /** Tổng tiền được backend tính từ tất cả các dòng giỏ. */
    private Double totalAmount;

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
