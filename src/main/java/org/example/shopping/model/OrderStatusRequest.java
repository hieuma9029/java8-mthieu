package org.example.shopping.model;

import org.example.shopping.entity.OrderStatus;

import javax.validation.constraints.NotNull;

/**
 * DTO dùng để nhận yêu cầu cập nhật trạng thái đơn hàng từ client.
 * Frontend gửi trạng thái mới bằng payload này nhằm điều khiển luồng xử lý đơn hàng
 * theo các trạng thái như PENDING, CONFIRMED, CANCELLED, SHIPPED, DELIVERED, RETURNED.
 * <p>Trong luồng hiện tại, trạng thái RETURNED chỉ hợp lệ khi đơn đang ở trạng thái DELIVERED.</p>
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
