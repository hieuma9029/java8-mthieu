package org.example.shopping.order.controller;

import org.example.shopping.entity.Orders;
import org.example.shopping.order.model.AdminOrderStatsResponse;
import org.example.shopping.order.model.CheckoutRequest;
import org.example.shopping.order.model.OrderDetailsResponse;
import org.example.shopping.order.model.OrderMapper;
import org.example.shopping.order.model.OrderResponse;
import org.example.shopping.order.model.OrderStatusRequest;
import org.example.shopping.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST quản lý các đơn hàng của tài khoản đang đăng nhập.
 *
 * <p>Cung cấp các endpoint để lấy danh sách đơn, xem chi tiết, tạo đơn từ giỏ hàng,
 * và cập nhật trạng thái đơn. Admin có thể xem toàn bộ đơn, user chỉ xem đơn của mình.</p>
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Khởi tạo controller với tầng nghiệp vụ đơn hàng.
     *
     * @param orderService service xử lý nghiệp vụ đơn hàng
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * GET /orders: lấy danh sách đơn hàng của người dùng hiện tại.
     * Admin sẽ nhận toàn bộ đơn hàng, user chỉ nhận đơn hàng của họ.
     *
     * @return danh sách đơn hàng dạng DTO
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }

        List<Orders> orders = orderService.findByAccountName(authentication.getName());
        List<OrderResponse> responses = orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/stats")
    /** Lấy thống kê doanh thu và sản phẩm bán chạy cho màn hình quản trị. */
    public ResponseEntity<AdminOrderStatsResponse> getAdminOrderStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }

        AdminOrderStatsResponse stats = orderService.getAdminStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /orders/{id}: lấy chi tiết một đơn hàng theo mã định danh.
     *
     * @param id mã đơn hàng trên URL
     * @return thông tin đơn hàng nếu người dùng có quyền xem
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }

        Orders order = orderService.findOwnedByIdOrThrow(id, authentication.getName());
        return ResponseEntity.ok(OrderMapper.toResponse(order));
    }

    /**
     * GET /orders/{id}/details: lấy các dòng chi tiết của đơn hàng.
     *
     * @param id mã đơn hàng
     * @return danh sách sản phẩm đã chốt trong đơn
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }

        // Kiểm tra quyền xem đơn hàng này
        orderService.findOwnedByIdOrThrow(id, authentication.getName());
        OrderDetailsResponse details = orderService.getOrderDetails(id);
        return ResponseEntity.ok(details);
    }

    /**
     * POST /orders/checkout: tạo đơn hàng từ giỏ hàng hiện tại.
     *
     * @param request dữ liệu khách hàng và địa chỉ giao hàng
     * @return đơn hàng vừa được tạo
     */
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        Orders order = orderService.checkout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderMapper.toResponse(order));
    }

    /**
     * PUT /orders/{id}/status: cập nhật trạng thái của một đơn hàng.
     *
     * @param id mã đơn hàng
     * @param request dữ liệu trạng thái mới
     * @return đơn hàng sau khi cập nhật
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Integer id,
                                                          @Valid @RequestBody OrderStatusRequest request) {
        Orders order = orderService.updateStatus(id, request);
        return ResponseEntity.ok(OrderMapper.toResponse(order));
    }
}
