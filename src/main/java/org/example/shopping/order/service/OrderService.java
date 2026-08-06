package org.example.shopping.order.service;

import org.example.shopping.entity.Orders;
import org.example.shopping.order.model.AdminOrderStatsResponse;
import org.example.shopping.order.model.CheckoutRequest;
import org.example.shopping.order.model.OrderDetailsResponse;
import org.example.shopping.order.model.OrderStatusRequest;
import org.example.shopping.service.BaseService;

import java.util.List;

/**
 * Service chuyên trách cho các nghiệp vụ liên quan đến đơn hàng.
 * Bao gồm tạo đơn, xem chi tiết, cập nhật trạng thái và kiểm tra quyền sở hữu.
 */
public interface OrderService extends BaseService<Orders, Integer> {

    /** Tạo đơn hàng và các dòng chi tiết từ dữ liệu giỏ hàng hiện tại. */
    Orders checkout(CheckoutRequest request);

    /** Lấy các sản phẩm đã chốt trong một đơn hàng để hiển thị chi tiết. */
    OrderDetailsResponse getOrderDetails(Integer orderId);

    /** Cập nhật trạng thái mới cho một đơn hàng đã tồn tại. */
    Orders updateStatus(Integer id, OrderStatusRequest request);

    /** Lấy danh sách đơn hàng thuộc tài khoản đang đăng nhập. */
    List<Orders> findByAccountName(String username);

    /** Lấy đơn hàng nếu nó thuộc tài khoản đang đăng nhập, hoặc ném lỗi nếu không. */
    Orders findOwnedByIdOrThrow(Integer orderId, String username);

    /** Lấy số liệu thống kê doanh thu và sản phẩm bán chạy cho admin. */
    AdminOrderStatsResponse getAdminStats();
}
