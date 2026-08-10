package org.example.shopping.order.model;

import java.time.LocalDateTime;

/**
 * DTO trả về thông tin đánh giá sản phẩm khi client yêu cầu xem review.
 * Trường `comment` dùng để hiển thị bình luận về chức năng / trải nghiệm sản phẩm.
 */
public class ReviewResponse {

    private Integer id;
    private Integer rating;
    private String comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
