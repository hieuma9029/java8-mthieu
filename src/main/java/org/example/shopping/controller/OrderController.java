package org.example.shopping.controller;

import org.example.shopping.entity.Orders;
import org.example.shopping.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
/** API REST quản lý đơn hàng tại /orders. */
public class OrderController {

    private final OrderService orderService;

    /** Inject tầng nghiệp vụ đơn hàng. */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    /** GET /orders: lấy danh sách đơn hàng. */
    public List<Orders> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    /** GET /orders/{id}: lấy đơn hàng theo id. */
    public Orders getOrderById(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @PostMapping
    /** POST /orders: tạo đơn hàng từ JSON request body. */
    public void saveOrder(@RequestBody Orders orders) {
        orderService.save(orders);
    }

    @PutMapping("/{id}")
    /** PUT /orders/{id}: cập nhật đơn hàng. */
    public void updateOrder(@PathVariable Integer id,
                            @RequestBody Orders orders) {

        orderService.update(id, orders);
    }

    @DeleteMapping("/{id}")
    /** DELETE /orders/{id}: xóa đơn hàng. */
    public void deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
    }
}
