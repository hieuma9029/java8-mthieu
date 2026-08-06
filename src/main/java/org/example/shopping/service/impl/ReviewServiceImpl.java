package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.Products;
import org.example.shopping.entity.Review;
import org.example.shopping.order.model.OrderReviewResponse;
import org.example.shopping.order.model.ReviewRequest;
import org.example.shopping.order.model.ReviewResponse;
import org.example.shopping.order.model.ReviewStatsResponse;
import org.example.shopping.repository.ReviewRepository;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.order.repository.OrderDetailRepository;
import org.example.shopping.order.repository.OrderRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hiện thực dịch vụ review sản phẩm.
 *
 * <p>Chỉ chấp nhận review khi người dùng đã mua và đơn hàng đã ở trạng thái DELIVERED.</p>
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             AccountRepository accountRepository,
                             OrderRepository orderRepository,
                             OrderDetailRepository orderDetailRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    @Transactional
    /** Gửi hoặc cập nhật review cho một sản phẩm trong đơn hàng đã giao thành công. */
    public ReviewResponse submitReview(Integer productId, Integer orderId, String username, ReviewRequest request) {
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(username);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }

        Products product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));

        if (!account.getId().equals(order.getAccount() != null ? order.getAccount().getId() : null)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền đánh giá cho đơn hàng này");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ có thể đánh giá sản phẩm khi đơn đã nhận (DELIVERED)");
        }

        List<OrderDetails> details = orderDetailRepository.findByOrders(order);
        boolean purchasedProduct = false;
        for (OrderDetails detail : details) {
            if (detail.getProducts() != null && detail.getProducts().getId().equals(productId)) {
                purchasedProduct = true;
                break;
            }
        }

        if (!purchasedProduct) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm này không thuộc đơn hàng đã nhận");
        }

        Review existingReview = reviewRepository.findByProductsAndOrdersAndAccount(product, order, account);
        Review review;
        
        if (existingReview != null) {
            // Cập nhật review đã tồn tại
            review = existingReview;
            if (request.getRating() != null) {
                review.setRating(request.getRating());
            }
            if (request.getComment() != null) {
                review.setComment(request.getComment().trim());
            }
        } else {
            // Tạo review mới
            review = new Review();
            review.setProducts(product);
            review.setOrders(order);
            review.setAccount(account);
            if (request.getRating() != null) {
                review.setRating(request.getRating());
            }
            if (request.getComment() != null) {
                review.setComment(request.getComment().trim());
            }
            review.setCreatedAt(LocalDateTime.now());
        }

        Review saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> findReviewsByProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        List<Review> reviews = reviewRepository.findByProducts(product);
        List<ReviewResponse> responseList = new ArrayList<>();
        for (Review review : reviews) {
            responseList.add(toResponse(review));
        }
        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderReviewResponse> findReviewsByOrder(Integer orderId, String username) {
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(username);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }

        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));

        if (!account.getId().equals(order.getAccount() != null ? order.getAccount().getId() : null)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem đánh giá cho đơn hàng này");
        }

        List<OrderDetails> orderDetails = orderDetailRepository.findByOrders(order);
        List<Review> reviews = reviewRepository.findByOrdersAndAccount(order, account);
        Map<Integer, Review> reviewByProductId = new HashMap<>();
        for (Review review : reviews) {
            if (review.getProducts() != null && review.getProducts().getId() != null) {
                reviewByProductId.put(review.getProducts().getId(), review);
            }
        }

        List<OrderReviewResponse> responseList = new ArrayList<>();
        for (OrderDetails detail : orderDetails) {
            Integer productId = detail.getProducts() != null ? detail.getProducts().getId() : null;
            OrderReviewResponse response = new OrderReviewResponse();
            response.setProductId(productId);

            Review userReview = productId != null ? reviewByProductId.get(productId) : null;
            if (userReview != null) {
                response.setId(userReview.getId());
                response.setRating(userReview.getRating());
                response.setComment(userReview.getComment());
                response.setCreatedAt(userReview.getCreatedAt());
            } else {
                response.setRating(0);
            }
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewStatsResponse getReviewStatsByProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        Double averageRating = reviewRepository.findAverageRatingByProducts(product);
        long reviewCount = reviewRepository.countByProducts(product);

        ReviewStatsResponse stats = new ReviewStatsResponse();
        stats.setAverageRating(averageRating != null ? averageRating : 0.0);
        stats.setReviewCount(reviewCount);
        return stats;
    }

    private ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setUserName(review.getAccount() != null ? review.getAccount().getUserName() : null);
        return response;
    }
}
