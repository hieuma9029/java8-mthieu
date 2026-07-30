package org.example.shopping.order.model;

import java.time.LocalDateTime;

/**
 * DTO trả về rating cho từng sản phẩm trong một đơn hàng của người dùng hiện tại.
 */
public class OrderReviewResponse {

    private Integer id;
    private Integer productId;
    private Integer rating;
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
