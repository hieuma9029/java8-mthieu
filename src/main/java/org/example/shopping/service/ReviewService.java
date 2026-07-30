package org.example.shopping.service;

import org.example.shopping.order.model.OrderReviewResponse;
import org.example.shopping.order.model.ReviewRequest;
import org.example.shopping.order.model.ReviewResponse;
import org.example.shopping.order.model.ReviewStatsResponse;

import java.util.List;

/**
 * Interface dịch vụ đánh giá sản phẩm.
 */
public interface ReviewService {

    /**
     * Tạo review cho sản phẩm nếu người mua đã nhận hàng thành công.
     *
     * @param productId mã sản phẩm
     * @param orderId   mã đơn hàng
     * @param username  tên người dùng hiện tại
     * @param request   dữ liệu review từ client
     * @return review đã tạo dưới dạng DTO
     */
    ReviewResponse submitReview(Integer productId, Integer orderId, String username, ReviewRequest request);

    /**
     * Lấy review cho một sản phẩm.
     *
     * @param productId mã sản phẩm
     * @return danh sách review
     */
    List<ReviewResponse> findReviewsByProduct(Integer productId);

    /**
     * Lấy thống kê review cho một sản phẩm.
     *
     * @param productId mã sản phẩm
     * @return thống kê review, gồm điểm trung bình và tổng số đánh giá
     */
    ReviewStatsResponse getReviewStatsByProduct(Integer productId);

    /**
     * Lấy đánh giá của người dùng hiện tại cho các sản phẩm trong một đơn hàng.
     *
     * @param orderId  mã đơn hàng
     * @param username tên người dùng hiện tại
     * @return danh sách rating theo sản phẩm trong order
     */
    List<OrderReviewResponse> findReviewsByOrder(Integer orderId, String username);
}
