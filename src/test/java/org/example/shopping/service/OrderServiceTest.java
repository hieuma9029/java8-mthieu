package org.example.shopping.order;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.Products;
import org.example.shopping.order.model.OrderStatusRequest;
import org.example.shopping.order.repository.OrderDetailRepository;
import org.example.shopping.order.repository.OrderRepository;
import org.example.shopping.order.service.OrderService;
import org.example.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    void shouldUpdateOrderStatusToConfirmedFromPending() {
        Orders order = new Orders();
        order.setCustomerName("Test Customer " + System.nanoTime());
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
    void shouldCancelConfirmedOrderAndRestoreProductQuantity() {
        Products product = new Products();
        product.setCode("TEST-" + System.nanoTime());
        product.setName("Test Product " + System.nanoTime());
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        Products savedProduct = productRepository.save(product);

        Orders order = new Orders();
        order.setCustomerName("Test Customer " + System.nanoTime());
        order.setStatus(OrderStatus.PENDING);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderDetails detail = new OrderDetails();
        detail.setOrders(savedOrder);
        detail.setProducts(savedProduct);
        detail.setQuantity(3);
        detail.setPrice(savedProduct.getPrice());
        detail.setAmount(savedProduct.getPrice().multiply(BigDecimal.valueOf(3)));
        detail.setIsDelete(false);
        orderDetailRepository.save(detail);

        OrderStatusRequest confirmRequest = new OrderStatusRequest();
        confirmRequest.setStatus(OrderStatus.CONFIRMED);
        orderService.updateStatus(savedOrder.getId(), confirmRequest);

        OrderStatusRequest cancelRequest = new OrderStatusRequest();
        cancelRequest.setStatus(OrderStatus.CANCELLED);
        Orders cancelledOrder = orderService.updateStatus(savedOrder.getId(), cancelRequest);

        Products updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow(AssertionError::new);
        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
        assertEquals(Integer.valueOf(10), updatedProduct.getQuantity());
    }

    @Test
    void shouldRejectInvalidTransitionFromPending() {
        Orders order = new Orders();
        order.setCustomerName("Test Customer " + System.nanoTime());
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
        order.setCustomerName("Test Customer " + System.nanoTime());
        order.setStatus(OrderStatus.SHIPPED);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.DELIVERED);

        Orders updatedOrder = orderService.updateStatus(savedOrder.getId(), request);

        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }

    @Test
    void shouldReturnDeliveredOrderAndRestoreProductQuantity() {
        Products product = new Products();
        product.setCode("TEST-" + System.nanoTime());
        product.setName("Test Product " + System.nanoTime());
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        Products savedProduct = productRepository.save(product);

        Orders order = new Orders();
        order.setCustomerName("Test Customer " + System.nanoTime());
        order.setStatus(OrderStatus.PENDING);
        order.setIsDelete(false);
        Orders savedOrder = orderRepository.save(order);

        OrderDetails detail = new OrderDetails();
        detail.setOrders(savedOrder);
        detail.setProducts(savedProduct);
        detail.setQuantity(3);
        detail.setPrice(savedProduct.getPrice());
        detail.setAmount(savedProduct.getPrice().multiply(BigDecimal.valueOf(3)));
        detail.setIsDelete(false);
        orderDetailRepository.save(detail);

        OrderStatusRequest confirmRequest = new OrderStatusRequest();
        confirmRequest.setStatus(OrderStatus.CONFIRMED);
        orderService.updateStatus(savedOrder.getId(), confirmRequest);

        OrderStatusRequest shippedRequest = new OrderStatusRequest();
        shippedRequest.setStatus(OrderStatus.SHIPPED);
        orderService.updateStatus(savedOrder.getId(), shippedRequest);

        OrderStatusRequest deliveredRequest = new OrderStatusRequest();
        deliveredRequest.setStatus(OrderStatus.DELIVERED);
        orderService.updateStatus(savedOrder.getId(), deliveredRequest);

        OrderStatusRequest returnedRequest = new OrderStatusRequest();
        returnedRequest.setStatus(OrderStatus.RETURNED);
        Orders returnedOrder = orderService.updateStatus(savedOrder.getId(), returnedRequest);

        Products updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow(AssertionError::new);
        assertEquals(OrderStatus.RETURNED, returnedOrder.getStatus());
        assertEquals(Integer.valueOf(10), updatedProduct.getQuantity());
    }
}
