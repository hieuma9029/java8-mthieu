package org.example.shopping.controller;

import org.example.shopping.entity.Orders;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.model.OrderDetailsResponse;
import org.example.shopping.model.OrderStatusRequest;
import org.example.shopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
/** API REST quản lý đơn hàng tại đường dẫn /orders. */
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

    @GetMapping
    /** GET /orders: lấy danh sách đơn hàng. */
    public List<Orders> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    /**
     * GET /orders/{id}: lấy đơn hàng theo mã định danh.
     *
     * @param id mã đơn hàng trên URL
     * @return đơn hàng tìm được
     */
    public Orders getOrderById(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @GetMapping("/{id}/details")
    /** GET /orders/{id}/details: lấy các sản phẩm đã chốt của một đơn. */
    public OrderDetailsResponse getOrderDetails(@PathVariable Integer id) {
        return orderService.getOrderDetails(id);
    }

    @PostMapping
    /**
     * POST /orders: tạo đơn hàng từ JSON request body.
     *
     * @param orders dữ liệu đơn hàng do client gửi
     */
    public void saveOrder(@RequestBody Orders orders) {
        orderService.save(orders);
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Tạo đơn hàng từ giỏ hàng. Giá và tổng tiền luôn được tính lại từ database.
     */
    public Orders checkout(@Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(request);
    }

    @PutMapping("/{id}")
    /**
     * PUT /orders/{id}: cập nhật thông tin đơn hàng.
     *
     * @param id mã đơn hàng cần cập nhật
     * @param orders dữ liệu đơn hàng mới do client gửi
     */
    public void updateOrder(@PathVariable Integer id,
                            @RequestBody Orders orders) {
        orderService.update(id, orders);
    }

    @PutMapping("/{id}/status")
    /**
     * PUT /orders/{id}/status: cập nhật trạng thái đơn hàng.
     *
     * @param id mã đơn hàng cần cập nhật
     * @param request yêu cầu trạng thái mới
     * @return đơn hàng sau khi đã cập nhật trạng thái
     */
    public Orders updateOrderStatus(@PathVariable Integer id,
                                    @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    /**
     * DELETE /orders/{id}: xóa đơn hàng.
     *
     * @param id mã đơn hàng cần xóa
     */
    public void deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
    }
}
