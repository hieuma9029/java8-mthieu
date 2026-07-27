package org.example.shopping.model;

import java.util.List;
import java.math.BigDecimal;

/** Dữ liệu đầy đủ của giỏ hàng để frontend hiển thị. */
public class CartResponse {
    /** Các dòng sản phẩm trong giỏ hiện tại. */
    private List<CartItemResponse> items;
    /** Tổng tiền được backend tính từ tất cả các dòng giỏ. */
    private BigDecimal totalAmount;

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
