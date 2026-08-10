package org.example.shopping.service;

import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.model.OrderStatusRequest;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.service.OrderService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Disabled("Tạm vô hiệu hóa vì các test ghi dữ liệu order vào database")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldUpdateOrderStatusToConfirmedFromPending() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.PENDING);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.CONFIRMED);

        Orders updatedOrder = orderService.updateStatus(savedOrder.getId(), request);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.CONFIRMED, updatedOrder.getStatus());
    }

    @Test
    void shouldRejectInvalidTransitionFromPending() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.PENDING);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.SHIPPED);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> orderService.updateStatus(savedOrder.getId(), request));

        assertEquals("Đơn hàng ở trạng thái PENDING chỉ có thể chuyển sang CONFIRMED hoặc CANCELLED", exception.getReason());
    }

    @Test
    void shouldUpdateShippedOrderToDelivered() {
        Orders order = new Orders();
        order.setStatus(OrderStatus.SHIPPED);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.DELIVERED);

        Orders updatedOrder = orderService.updateStatus(savedOrder.getId(), request);

        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }

}
