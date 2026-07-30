package org.example.shopping.order;

import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.order.model.OrderStatusRequest;
import org.example.shopping.order.repository.OrderRepository;
import org.example.shopping.order.service.OrderService;
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
        order.setCustomerName("Test Customer " + System.nanoTime());
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
