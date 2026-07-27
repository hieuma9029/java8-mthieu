package org.example.shopping;

import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.model.OrderStatusRequest;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldUpdateOrderStatus() {
        Orders order = new Orders();
        order.setCustomerName("Test Customer");
        order.setStatus(OrderStatus.PENDING);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.DELIVERED);

        Orders updatedOrder = orderService.updateStatus(savedOrder.getId(), request);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }
}
