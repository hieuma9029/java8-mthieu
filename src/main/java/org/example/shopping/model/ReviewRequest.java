package org.example.shopping.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * DTO dùng để nhận dữ liệu đánh giá sản phẩm từ client.
 * Người mua chỉ được gửi rating khi đơn hàng đã ở trạng thái DELIVERED.
 */
public class ReviewRequest {

    /** Điểm đánh giá, từ 1 đến 5. */
    @Min(value = 1, message = "Rating phải lớn hơn hoặc bằng 1")
    @Max(value = 5, message = "Rating phải nhỏ hơn hoặc bằng 5")
    private Integer rating;

    /** Bình luận về chức năng / trải nghiệm sản phẩm do người dùng gửi. */
    @Size(max = 1000, message = "Bình luận không được dài quá 1000 ký tự")
    private String comment;

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
}
