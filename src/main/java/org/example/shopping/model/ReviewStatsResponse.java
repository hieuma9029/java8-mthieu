package org.example.shopping.order.model;

/**
 * DTO trả về thống kê đánh giá của một sản phẩm.
 */
public class ReviewStatsResponse {

    /** Điểm trung bình của sản phẩm. */
    private Double averageRating;

    /** Tổng số rating đã gửi cho sản phẩm. */
    private Long reviewCount;

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
