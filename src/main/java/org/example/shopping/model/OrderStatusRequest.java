package org.example.shopping.model;

import org.example.shopping.entity.OrderStatus;

import javax.validation.constraints.NotNull;

/**
 * DTO dùng để nhận yêu cầu cập nhật trạng thái đơn hàng từ client.
 * Frontend gửi trạng thái mới bằng payload này khi thay đổi tiến trình đơn.
 */
public class OrderStatusRequest {

    /** Trạng thái mới mà đơn hàng sẽ được cập nhật sang. */
    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
