package org.example.shopping.order.model;

import java.time.LocalDateTime;

/**
 * DTO trả về thông tin đánh giá sản phẩm khi client yêu cầu xem review.
 */
public class ReviewResponse {

    private Integer id;
    private Integer rating;
    private String userName;
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
