package org.example.shopping.service;

import org.example.shopping.entity.Orders;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.model.OrderDetailsResponse;
import org.example.shopping.model.OrderStatusRequest;

/**
 * Service chuyên trách cho các nghiệp vụ liên quan đến đơn hàng.
 * Bao gồm tạo đơn, xem chi tiết, cập nhật trạng thái và xóa mềm/đối tượng.
 */
public interface OrderService extends BaseService<Orders, Integer> {

    /** Tạo đơn hàng và các dòng chi tiết từ dữ liệu giỏ hàng hiện tại. */
    Orders checkout(CheckoutRequest request);

    /** Lấy các sản phẩm đã chốt trong một đơn hàng để hiển thị chi tiết. */
    OrderDetailsResponse getOrderDetails(Integer orderId);

    /** Cập nhật trạng thái mới cho một đơn hàng đã tồn tại. */
    Orders updateStatus(Integer id, OrderStatusRequest request);
}
