package org.example.shopping.controller;

import org.example.shopping.model.OrderReviewResponse;
import org.example.shopping.model.ReviewRequest;
import org.example.shopping.model.ReviewResponse;
import org.example.shopping.model.ReviewStatsResponse;
import org.example.shopping.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller REST quản lý review sản phẩm.
 *
 * <p>Cung cấp endpoint để người mua có thể đánh giá sản phẩm sau khi đơn đã ở trạng thái
 * DELIVERED và hiển thị các review theo sản phẩm.</p>
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * POST /reviews/products/{productId}/orders/{orderId}: gửi đánh giá cho sản phẩm trong đơn hàng.
     *
     * @param productId mã sản phẩm
     * @param orderId   mã đơn hàng
     * @param request   dữ liệu review
     * @return review vừa tạo
     */
    @PostMapping("/products/{productId}/orders/{orderId}")
    public ResponseEntity<ReviewResponse> submitReview(@PathVariable Integer productId,
                                                       @PathVariable Integer orderId,
                                                       @Valid @RequestBody ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        ReviewResponse response = reviewService.submitReview(productId, orderId, username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /reviews/products/{productId}: lấy danh sách review của sản phẩm.
     *
     * @param productId mã sản phẩm
     * @return danh sách review
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable Integer productId) {
        List<ReviewResponse> reviews = reviewService.findReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * GET /reviews/products/{productId}/stats: lấy thống kê đánh giá của sản phẩm.
     *
     * @param productId mã sản phẩm
     * @return trung bình rating và tổng số review
     */
    @GetMapping("/products/{productId}/stats")
    public ResponseEntity<ReviewStatsResponse> getReviewStats(@PathVariable Integer productId) {
        ReviewStatsResponse stats = reviewService.getReviewStatsByProduct(productId);
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /reviews/orders/{orderId}: lấy rating của người dùng hiện tại cho từng sản phẩm trong đơn hàng.
     *
     * @param orderId mã đơn hàng
     * @return danh sách rating theo sản phẩm của order
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderReviewResponse>> getReviewsByOrder(@PathVariable Integer orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        List<OrderReviewResponse> response = reviewService.findReviewsByOrder(orderId, username);
        return ResponseEntity.ok(response);
    }
}
