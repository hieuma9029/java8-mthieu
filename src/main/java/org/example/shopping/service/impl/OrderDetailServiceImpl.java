package org.example.shopping.service.impl;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.repository.OrderDetailRepository;
import org.example.shopping.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** Hiện thực CRUD các dòng chi tiết đơn hàng bằng OrderDetailRepository. */
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    /** Inject lớp truy cập database chi tiết đơn. */
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    /** Đọc tất cả dòng chi tiết đơn. */
    public List<OrderDetails> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    /** Tìm chi tiết đơn; trả về null nếu không tồn tại. */
    public OrderDetails findById(Integer id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    /** Lưu một dòng chi tiết mới hoặc đã tồn tại. */
    public void save(OrderDetails orderDetails) {
        orderDetailRepository.save(orderDetails);
    }

    @Override
    /** Cập nhật các trường và quan hệ order/product nếu bản ghi tồn tại. */
    public void update(Integer id, OrderDetails orderDetails) {

        OrderDetails oldDetail = orderDetailRepository.findById(id).orElse(null);

        if (oldDetail != null) {

            oldDetail.setAmount(orderDetails.getAmount());
            oldDetail.setPrice(orderDetails.getPrice());
            oldDetail.setQuantity(orderDetails.getQuantity());
            oldDetail.setOrders(orderDetails.getOrders());
            oldDetail.setProducts(orderDetails.getProducts());
            oldDetail.setIsDelete(orderDetails.getIsDelete());
            oldDetail.setDeletedAt(orderDetails.getDeletedAt());
            oldDetail.setCreatedAt(orderDetails.getCreatedAt());
            oldDetail.setUpdatedAt(orderDetails.getUpdatedAt());

            orderDetailRepository.save(oldDetail);
        }
    }

    @Override
    /** Xóa cứng chi tiết đơn theo id. */
    public void delete(Integer id) {
        orderDetailRepository.deleteById(id);
    }
}
