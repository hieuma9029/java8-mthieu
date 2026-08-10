package org.example.shopping.order.model;

import org.example.shopping.entity.Orders;

/**
 * Helper dùng để chuyển đổi giữa entity Orders và các DTO dùng cho API đơn hàng.
 * Việc tách riêng lớp này giúp controller không cần trực tiếp thao tác với entity.
 */
public class OrderMapper {

    public static OrderResponse toResponse(Orders order) {
        if (order == null) return null;
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setOrderNum(order.getOrderNum());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setCustomerAddress(order.getCustomerAddress());
        dto.setAmount(order.getAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        return dto;
    }

    public static Orders toEntity(OrderRequest req) {
        if (req == null) return null;
        Orders o = new Orders();
        o.setOrderNum(req.getOrderNum());
        o.setAmount(req.getAmount());
        o.setCustomerName(req.getCustomerName());
        o.setCustomerEmail(req.getCustomerEmail());
        o.setCustomerPhone(req.getCustomerPhone());
        o.setCustomerAddress(req.getCustomerAddress());
        o.setOrderDate(req.getOrderDate());
        if (req.getStatus() != null) o.setStatus(req.getStatus());
        o.setIsDelete(req.getIsDelete());
        return o;
    }

    public static void updateEntityFromRequest(Orders existing, OrderRequest req) {
        if (existing == null || req == null) return;
        if (req.getOrderNum() != null) existing.setOrderNum(req.getOrderNum());
        if (req.getAmount() != null) existing.setAmount(req.getAmount());
        if (req.getCustomerName() != null) existing.setCustomerName(req.getCustomerName());
        if (req.getCustomerEmail() != null) existing.setCustomerEmail(req.getCustomerEmail());
        if (req.getCustomerPhone() != null) existing.setCustomerPhone(req.getCustomerPhone());
        if (req.getCustomerAddress() != null) existing.setCustomerAddress(req.getCustomerAddress());
        if (req.getOrderDate() != null) existing.setOrderDate(req.getOrderDate());
        if (req.getStatus() != null) existing.setStatus(req.getStatus());
        if (req.getIsDelete() != null) existing.setIsDelete(req.getIsDelete());
    }
}
