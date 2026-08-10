package org.example.shopping.order;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.Orders;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.order.repository.OrderDetailRepository;
import org.example.shopping.order.repository.OrderRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.order.service.impl.OrderCheckoutService;
import org.example.shopping.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
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
}
