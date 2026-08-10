package org.example.shopping.order.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Products;
import org.example.shopping.order.model.AdminOrderStatsResponse;
import org.example.shopping.order.model.BestSellingProductResponse;
import org.example.shopping.order.model.CheckoutRequest;
import org.example.shopping.order.model.OrderDetailsResponse;
import org.example.shopping.order.model.OrderItemResponse;
import org.example.shopping.order.model.OrderStatusRequest;
import org.example.shopping.order.repository.OrderRepository;
import org.example.shopping.order.service.OrderService;
import org.example.shopping.order.repository.OrderDetailRepository;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.impl.BaseServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
/**
 * Hiện thực các nghiệp vụ liên quan đến đơn hàng bằng OrderRepository.
 * Lớp này xử lý checkout, cập nhật trạng thái, lấy chi tiết đơn và kiểm soát quyền xem của admin/user.
 */
public class OrderServiceImpl extends BaseServiceImpl<Orders, Integer, OrderRepository> implements OrderService {

    /** Repository đơn hàng để dùng các query thống kê. */
    private final OrderRepository orderRepository;
    /** Lưu các dòng hàng đã được chốt vào đơn. */
    private final OrderDetailRepository orderDetailRepository;
    /** Repository sản phẩm để cập nhật tồn kho khi đơn được xác nhận. */
    private final ProductRepository productRepository;
    /** Service phụ trách tạo đơn hàng từ giỏ hàng hiện tại. */
    private final OrderCheckoutService orderCheckoutService;

    /**
     * Khởi tạo service với repository truy cập dữ liệu đơn hàng.
     *
     * @param orderRepository repository dùng cho các thao tác đơn hàng
     */
    private final AccountRepository accountRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailRepository orderDetailRepository,
                            ProductRepository productRepository,
                            OrderCheckoutService orderCheckoutService,
                            AccountRepository accountRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.orderCheckoutService = orderCheckoutService;
        this.accountRepository = accountRepository;
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
    /**
     * Chuyển giỏ hàng hiện tại thành một đơn hàng bằng service phụ trách checkout.
     */
    public Orders checkout(CheckoutRequest request) {
        return orderCheckoutService.checkout(request);
    }

    @Override
    public List<Orders> findByAccountName(String username) {
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(username);
        if (account == null) {
            return new ArrayList<>();
        }

        if (isAdmin(account)) {
            return repository.findAll();
        }

        return repository.findByAccountAndIsDeleteFalse(account);
    }

    @Override
    public Orders findOwnedByIdOrThrow(Integer orderId, String username) {
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(username);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }

        Orders order = repository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Không tìm thấy đơn hàng có id = " + orderId));

        if (isAdmin(account)) {
            return order;
        }

        if (order.getAccount() == null || !account.getId().equals(order.getAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem đơn hàng này");
        }
        return order;
    }

    private boolean isAdmin(Accounts account) {
        if (account == null) {
            return false;
        }

        String role = account.getUserRole();
        if (role == null || role.trim().isEmpty()) {
            return false;
        }

        String[] roles = role.split(",");
        for (String item : roles) {
            String normalizedRole = item.trim().toUpperCase();
            if ("ROLE_ADMIN".equals(normalizedRole) || "ADMIN".equals(normalizedRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    /** Cập nhật trạng thái đơn hàng theo mã định danh và payload trạng thái mới. */
    public Orders updateStatus(Integer id, OrderStatusRequest request) {
        Orders order = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Không tìm thấy đơn hàng có id = " + id));
        if (request.getStatus() != null) {
            OrderStatus previousStatus = order.getStatus();
            validateTransition(previousStatus, request.getStatus());
            // Nếu đơn hàng vừa xác nhận nhưng bị hủy thì hoàn lại tồn kho.
            if (previousStatus == OrderStatus.CONFIRMED && request.getStatus() == OrderStatus.CANCELLED) {
                restoreProductQuantity(order);
            // Nếu đơn hàng đã nhận thành công mà khách trả lại thì cũng hoàn kho.
            } else if (previousStatus == OrderStatus.DELIVERED && request.getStatus() == OrderStatus.RETURNED) {
                restoreProductQuantity(order);
            // Nếu đơn hàng chuyển sang trạng thái CONFIRMED thì trừ tồn kho tương ứng.
            } else if (previousStatus != OrderStatus.CONFIRMED
                    && request.getStatus() == OrderStatus.CONFIRMED) {
                adjustProductQuantity(order);
            }
            order.setStatus(request.getStatus());
        }
        order.setUpdatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    private void validateTransition(OrderStatus previousStatus, OrderStatus nextStatus) {
        if (previousStatus == null) {
            previousStatus = OrderStatus.PENDING;
        }

        if (previousStatus == OrderStatus.PENDING) {
            if (nextStatus != OrderStatus.CONFIRMED && nextStatus != OrderStatus.CANCELLED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Đơn hàng ở trạng thái PENDING chỉ có thể chuyển sang CONFIRMED hoặc CANCELLED");
            }
            return;
        }

        if (previousStatus == OrderStatus.CONFIRMED) {
            if (nextStatus != OrderStatus.CANCELLED && nextStatus != OrderStatus.SHIPPED && nextStatus != OrderStatus.DELIVERED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Đơn hàng ở trạng thái CONFIRMED chỉ có thể chuyển sang CANCELLED, SHIPPED hoặc DELIVERED");
            }
            return;
        }

        if (previousStatus == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng ở trạng thái CANCELLED không thể chuyển tiếp");
        }

        if (previousStatus == OrderStatus.SHIPPED) {
            if (nextStatus != OrderStatus.DELIVERED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Đơn hàng ở trạng thái SHIPPED chỉ có thể chuyển sang DELIVERED");
            }
            return;
        }

        // Sau khi khách đã nhận hàng, chỉ cho phép chuyển sang trạng thái trả hàng.
        if (previousStatus == OrderStatus.DELIVERED) {
            if (nextStatus != OrderStatus.RETURNED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Đơn hàng ở trạng thái DELIVERED chỉ có thể chuyển sang RETURNED");
            }
            return;
        }
    }

    private void adjustProductQuantity(Orders order) {
        for (OrderDetails detail : orderDetailRepository.findByOrders(order)) {
            Products product = detail.getProducts();
            if (product == null) {
                continue;
            }
            int newQuantity = product.getQuantity() - detail.getQuantity();
            if (newQuantity < 0) {
                newQuantity = 0;
            }
            product.setQuantity(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    private void restoreProductQuantity(Orders order) {
        for (OrderDetails detail : orderDetailRepository.findByOrders(order)) {
            Products product = detail.getProducts();
            if (product == null) {
                continue;
            }
            int newQuantity = product.getQuantity() + detail.getQuantity();
            product.setQuantity(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
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
            if (product == null) {
                continue;
            }
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

    @Override
    public AdminOrderStatsResponse getAdminStats() {
        BigDecimal totalRevenue = orderRepository.sumAmountByStatusAndIsDeleteFalse(OrderStatus.DELIVERED);
        long totalOrders = orderRepository.countByStatusAndIsDeleteFalse(OrderStatus.DELIVERED);
        List<BestSellingProductResponse> topProducts = orderDetailRepository.findTopProductsByStatus(OrderStatus.DELIVERED, PageRequest.of(0, 10));

        AdminOrderStatsResponse stats = new AdminOrderStatsResponse();
        stats.setTotalRevenue(totalRevenue);
        stats.setTotalOrders(totalOrders);
        stats.setTopProducts(topProducts);
        return stats;
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
