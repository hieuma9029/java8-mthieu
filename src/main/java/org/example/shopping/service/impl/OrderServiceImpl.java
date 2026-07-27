package org.example.shopping.service.impl;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.Orders;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Products;
import org.example.shopping.model.CheckoutRequest;
import org.example.shopping.model.OrderDetailsResponse;
import org.example.shopping.model.OrderItemResponse;
import org.example.shopping.model.OrderStatusRequest;
import org.example.shopping.repository.OrderDetailRepository;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
/**
 * Hiện thực các nghiệp vụ liên quan đến đơn hàng bằng OrderRepository.
 * Lớp này xử lý checkout, cập nhật trạng thái, lấy chi tiết đơn và xóa dữ liệu liên quan.
 */
public class OrderServiceImpl extends BaseServiceImpl<Orders, Integer, OrderRepository> implements OrderService {

    /** Lưu các dòng hàng đã được chốt vào đơn. */
    private final OrderDetailRepository orderDetailRepository;
    /** Service phụ trách tạo đơn hàng từ giỏ hàng hiện tại. */
    private final OrderCheckoutService orderCheckoutService;

    /**
     * Khởi tạo service với repository truy cập dữ liệu đơn hàng.
     *
     * @param orderRepository repository dùng cho các thao tác đơn hàng
     */
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailRepository orderDetailRepository,
                            OrderCheckoutService orderCheckoutService) {
        super(orderRepository);
        this.orderDetailRepository = orderDetailRepository;
        this.orderCheckoutService = orderCheckoutService;
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
