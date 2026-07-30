package org.example.shopping.order.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO dùng để nhận dữ liệu đánh giá sản phẩm từ client.
 * Người mua chỉ được gửi rating khi đơn hàng đã ở trạng thái DELIVERED.
 */
public class ReviewRequest {

    /** Điểm đánh giá, từ 1 đến 5. */
    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải lớn hơn hoặc bằng 1")
    @Max(value = 5, message = "Rating phải nhỏ hơn hoặc bằng 5")
    private Integer rating;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
