package org.example.shopping.service.impl;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.Products;
import org.example.shopping.entity.CartItems;
import org.example.shopping.entity.Carts;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.model.OrderDetailsResponse;
import org.example.shopping.model.OrderItemResponse;
import org.example.shopping.model.OrderStatusRequest;
import org.example.shopping.repository.CartItemRepository;
import org.example.shopping.repository.OrderDetailRepository;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.OrderService;
import org.example.shopping.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
/**
 * Hiện thực các nghiệp vụ liên quan đến đơn hàng bằng OrderRepository.
 * Lớp này xử lý checkout, cập nhật trạng thái, lấy chi tiết đơn và xóa dữ liệu liên quan.
 */
public class OrderServiceImpl extends BaseServiceImpl<Orders, Integer, OrderRepository> implements OrderService {

    /** Dùng để lấy giá thật và trạng thái xóa mềm của sản phẩm khi checkout. */
    private final ProductRepository productRepository;
    /** Lưu các dòng hàng đã được chốt vào đơn. */
    private final OrderDetailRepository orderDetailRepository;
    /** Cung cấp giỏ hàng của tài khoản đang checkout. */
    private final CartService cartService;
    /** Đọc và xóa các dòng giỏ sau khi tạo đơn thành công. */
    private final CartItemRepository cartItemRepository;

    /**
     * Khởi tạo service với repository truy cập dữ liệu đơn hàng.
     *
     * @param orderRepository repository dùng cho các thao tác đơn hàng
     */
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderDetailRepository orderDetailRepository,
                            CartService cartService,
                            CartItemRepository cartItemRepository) {
        super(orderRepository);
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    /** Gán trạng thái mặc định cho đơn hàng mới khi tạo bằng API thủ công. */
    public void save(Orders order) {
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }
        if (order.getIsDelete() == null) {
            order.setIsDelete(false);
        }
        repository.save(order);
    }

    @Override
    @Transactional
    /**
     * Chuyển giỏ hàng hiện tại thành một đơn hàng trong cùng transaction:
     * tính giá từ database, lưu order_details và chỉ sau đó mới xóa giỏ.
     */
    public Orders checkout(CheckoutRequest request) {
        Orders order = new Orders();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setIsDelete(false);

        Carts cart = cartService.getCurrentCartEntity();
        if (cart == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Giỏ hàng trống");
        }
        java.util.List<CartItems> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Giỏ hàng trống");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItems cartItem : cartItems) {
            Products product = getAvailableProduct(cartItem.getProduct().getId());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setAmount(totalAmount);

        Orders savedOrder = repository.save(order);
        savedOrder.setOrderNum(savedOrder.getId());

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
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    /** Cập nhật trạng thái đơn hàng theo mã định danh và payload trạng thái mới. */
    public Orders updateStatus(Integer id, OrderStatusRequest request) {
        Orders order = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Không tìm thấy đơn hàng có id = " + id));
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        order.setUpdatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    @Override
    public void delete(Integer id) {
        Orders order = repository.findById(id).orElse(null);
        if (order != null) {
            java.util.List<OrderDetails> orderDetails = orderDetailRepository.findByOrders(order);
            if (!orderDetails.isEmpty()) {
                orderDetailRepository.deleteAll(orderDetails);
            }
            repository.delete(order);
        }
    }

    @Override
    @Transactional(readOnly = true)
    /** Trả về các dòng hàng đã chốt để trang Order Detail hiển thị như giỏ hàng. */
    public OrderDetailsResponse getOrderDetails(Integer orderId) {
        Orders order = repository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Không tìm thấy đơn hàng có id = " + orderId));

        List<OrderItemResponse> items = new ArrayList<>();
        for (OrderDetails detail : orderDetailRepository.findByOrders(order)) {
            Products product = detail.getProducts();
            OrderItemResponse item = new OrderItemResponse();
            item.setProductId(product.getId());
            item.setCode(product.getCode());
            item.setName(product.getName());
            item.setImage(product.getImage());
            item.setPrice(detail.getPrice());
            item.setQuantity(detail.getQuantity());
            item.setSubtotal(detail.getAmount());
            items.add(item);
        }

        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setItems(items);
        response.setTotalAmount(order.getAmount());
        return response;
    }

    /** Kiểm tra sản phẩm tồn tại và chưa bị xóa mềm trước khi chốt đơn. */
    private Products getAvailableProduct(Integer productId) {
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null || Boolean.TRUE.equals(product.getIsDelete())) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Không tìm thấy sản phẩm có id = " + productId);
        }
        return product;
    }

    @Override
    /**
     * Sao chép dữ liệu cập nhật từ đối tượng nguồn vào đơn hàng hiện có.
     *
     * @param existing đơn hàng hiện có trong database
     * @param source   đơn hàng chứa dữ liệu mới từ client
     */
    protected void copyForUpdate(Orders existing, Orders source) {
        existing.setOrderNum(source.getOrderNum());
        existing.setAmount(source.getAmount());
        existing.setCustomerName(source.getCustomerName());
        existing.setCustomerEmail(source.getCustomerEmail());
        existing.setCustomerPhone(source.getCustomerPhone());
        existing.setCustomerAddress(source.getCustomerAddress());
        existing.setOrderDate(source.getOrderDate());
        if (source.getStatus() != null) {
            existing.setStatus(source.getStatus());
        }
        existing.setIsDelete(source.getIsDelete());
        existing.setDeletedAt(source.getDeletedAt());
        existing.setCreatedAt(source.getCreatedAt());
        existing.setUpdatedAt(source.getUpdatedAt());
    }
} 
