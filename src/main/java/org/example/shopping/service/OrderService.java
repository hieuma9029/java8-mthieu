package org.example.shopping.service;

import org.example.shopping.entity.Orders;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.model.OrderDetailsResponse;

/** Service cho các nghiệp vụ đặc thù của đơn hàng. */
public interface OrderService extends BaseService<Orders, Integer> {

    /** Tạo đơn hàng và các dòng chi tiết từ dữ liệu giỏ hàng. */
    Orders checkout(CheckoutRequest request);

    /** Lấy các sản phẩm đã chốt trong một đơn hàng. */
    OrderDetailsResponse getOrderDetails(Integer orderId);
}
