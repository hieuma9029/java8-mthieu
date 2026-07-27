package org.example.shopping.service.impl;

import org.example.shopping.entity.CartItems;
import org.example.shopping.entity.Carts;
import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.Products;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.repository.CartItemRepository;
import org.example.shopping.repository.OrderDetailRepository;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service phụ trách tạo đơn hàng từ giỏ hàng hiện tại.
 * Lớp này tách riêng phần checkout khỏi OrderServiceImpl để giữ code dễ đọc hơn.
 */
@Service
public class OrderCheckoutService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    public OrderCheckoutService(OrderRepository orderRepository,
                                ProductRepository productRepository,
                                OrderDetailRepository orderDetailRepository,
                                CartService cartService,
                                CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Tạo đơn hàng mới từ giỏ hàng hiện tại.
     *
     * @param request dữ liệu khách hàng và thông tin checkout
     * @return đơn hàng vừa được tạo
     */
    @Transactional
    public Orders checkout(CheckoutRequest request) {
        Orders order = buildOrder(request);
        Carts cart = cartService.getCurrentCartEntity();
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng trống");
        }

        List<CartItems> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng trống");
        }

        BigDecimal totalAmount = calculateTotalAmount(cartItems);
        order.setAmount(totalAmount);

        Orders savedOrder = orderRepository.save(order);
        savedOrder.setOrderNum(savedOrder.getId());

        createOrderDetails(savedOrder, cartItems);
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    private Orders buildOrder(CheckoutRequest request) {
        Orders order = new Orders();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setIsDelete(false);
        return order;
    }

    private BigDecimal calculateTotalAmount(List<CartItems> cartItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItems cartItem : cartItems) {
            Products product = getAvailableProduct(cartItem.getProduct().getId());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalAmount;
    }

    private void createOrderDetails(Orders savedOrder, List<CartItems> cartItems) {
        for (CartItems cartItem : cartItems) {
            Products product = getAvailableProduct(cartItem.getProduct().getId());
            OrderDetails detail = new OrderDetails();
            detail.setOrders(savedOrder);
            detail.setProducts(product);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(product.getPrice());
            detail.setAmount(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            detail.setCreatedAt(LocalDateTime.now());
            detail.setIsDelete(false);
            orderDetailRepository.save(detail);
        }
    }

    private Products getAvailableProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null || Boolean.TRUE.equals(product.getIsDelete())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy sản phẩm có id = " + productId);
        }
        return product;
    }
}
