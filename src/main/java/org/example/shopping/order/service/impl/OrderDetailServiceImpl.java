package org.example.shopping.order.service.impl;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.order.repository.OrderDetailRepository;
import org.example.shopping.order.service.OrderDetailService;
import org.example.shopping.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
/** Hiện thực CRUD cho các dòng chi tiết đơn hàng bằng OrderDetailRepository. */
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetails, Integer, OrderDetailRepository> implements OrderDetailService {

    /**
     * Khởi tạo service với repository truy cập dữ liệu chi tiết đơn.
     *
     * @param orderDetailRepository repository dùng cho các thao tác chi tiết đơn
     */
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        super(orderDetailRepository);
    }

    @Override
    /**
     * Sao chép dữ liệu cập nhật từ đối tượng nguồn vào chi tiết đơn hàng hiện có.
     *
     * @param existing chi tiết đơn hàng hiện có trong database
     * @param source   chi tiết đơn hàng chứa dữ liệu mới từ client
     */
    protected void copyForUpdate(OrderDetails existing, OrderDetails source) {
        existing.setAmount(source.getAmount());
        existing.setPrice(source.getPrice());
        existing.setQuantity(source.getQuantity());
        existing.setOrders(source.getOrders());
        existing.setProducts(source.getProducts());
        existing.setIsDelete(source.getIsDelete());
        existing.setDeletedAt(source.getDeletedAt());
        existing.setCreatedAt(source.getCreatedAt());
        existing.setUpdatedAt(source.getUpdatedAt());
    }
} 
