package org.example.shopping.controller;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.service.OrderDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
/** API REST quản lý các dòng sản phẩm của đơn hàng. */
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    /** Inject tầng nghiệp vụ chi tiết đơn hàng. */
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping
    /** GET /order-details: lấy tất cả chi tiết đơn. */
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailService.findAll();
    }

    @GetMapping("/{id}")
    /** GET /order-details/{id}: lấy chi tiết theo id. */
    public OrderDetails getOrderDetailById(@PathVariable Integer id) {
        return orderDetailService.findById(id);
    }

    @PostMapping
    /** POST /order-details: thêm một dòng sản phẩm vào đơn. */
    public void saveOrderDetail(@RequestBody OrderDetails orderDetails) {
        orderDetailService.save(orderDetails);
    }

    @PutMapping("/{id}")
    /** PUT /order-details/{id}: cập nhật một dòng chi tiết. */
    public void updateOrderDetail(@PathVariable Integer id,
                                  @RequestBody OrderDetails orderDetails) {

        orderDetailService.update(id, orderDetails);
    }

    @DeleteMapping("/{id}")
    /** DELETE /order-details/{id}: xóa chi tiết đơn hàng. */
    public void deleteOrderDetail(@PathVariable Integer id) {
        orderDetailService.delete(id);
    }
}
