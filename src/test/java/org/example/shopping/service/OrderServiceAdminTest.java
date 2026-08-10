package org.example.shopping.service;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.Products;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.repository.OrderDetailRepository;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.impl.OrderCheckoutService;
import org.example.shopping.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.shopping.model.OrderStatusRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceAdminTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderCheckoutService orderCheckoutService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldReturnAllOrdersForAdminWithCommaSeparatedRole() {
        Accounts adminAccount = new Accounts();
        adminAccount.setUserName("admin");
        adminAccount.setUserRole("ROLE_ADMIN,ROLE_USER");
        adminAccount.setActive(true);

        Accounts userAccount = new Accounts();
        userAccount.setUserName("user");
        userAccount.setUserRole("ROLE_USER");
        userAccount.setActive(true);

        Orders userOrder = new Orders();
        userOrder.setId(1);
        userOrder.setAccount(userAccount);

        Orders adminOrder = new Orders();
        adminOrder.setId(2);
        adminOrder.setAccount(userAccount);

        when(accountRepository.findByUserNameAndIsDeleteFalse("admin")).thenReturn(adminAccount);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(userOrder, adminOrder));

        List<Orders> result = orderService.findByAccountName("admin");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldRestoreExactOrderDetailQuantityWhenConfirmedOrderIsCancelled() {
        Orders order = new Orders();
        order.setId(10);
        order.setStatus(OrderStatus.CONFIRMED);

        Products product = new Products();
        product.setQuantity(3);

        OrderDetails detail = new OrderDetails();
        detail.setOrders(order);
        detail.setProducts(product);
        detail.setQuantity(4);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(10)).thenReturn(java.util.Optional.of(order));
        when(orderDetailRepository.findByOrders(order)).thenReturn(Collections.singletonList(detail));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateStatus(10, request);

        assertEquals(7, product.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void shouldNotRestoreStockWhenCancellingPendingOrder() {
        Orders order = new Orders();
        order.setId(11);
        order.setStatus(OrderStatus.PENDING);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(11)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateStatus(11, request);

        verify(orderDetailRepository, never()).findByOrders(order);
        verify(productRepository, never()).save(org.mockito.ArgumentMatchers.any(Products.class));
    }

    @Test
    void shouldRejectSecondCancellationWithoutRestoringStockAgain() {
        Orders order = new Orders();
        order.setId(12);
        order.setStatus(OrderStatus.CANCELLED);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(12)).thenReturn(java.util.Optional.of(order));

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> orderService.updateStatus(12, request));

        verify(orderDetailRepository, never()).findByOrders(order);
        verify(productRepository, never()).save(org.mockito.ArgumentMatchers.any(Products.class));
    }

    @Test
    void shouldRejectConfirmationWhenOrderQuantityExceedsCurrentStock() {
        Orders order = new Orders();
        order.setId(13);
        order.setStatus(OrderStatus.PENDING);

        Products product = new Products();
        product.setName("Sản phẩm thử nghiệm");
        product.setQuantity(555);

        OrderDetails detail = new OrderDetails();
        detail.setOrders(order);
        detail.setProducts(product);
        detail.setQuantity(666);

        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(13)).thenReturn(java.util.Optional.of(order));
        when(orderDetailRepository.findByOrders(order)).thenReturn(Collections.singletonList(detail));

        org.springframework.web.server.ResponseStatusException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        org.springframework.web.server.ResponseStatusException.class,
                        () -> orderService.updateStatus(13, request));

        assertEquals(400, exception.getStatus().value());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(555, product.getQuantity());
        verify(productRepository, never()).save(org.mockito.ArgumentMatchers.any(Products.class));
        verify(orderRepository, never()).save(order);
    }
}
