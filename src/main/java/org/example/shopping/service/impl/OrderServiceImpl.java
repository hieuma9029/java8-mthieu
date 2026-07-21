package org.example.shopping.service.impl;

import org.example.shopping.entity.Orders;
import org.example.shopping.repository.OrderRepository;
import org.example.shopping.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** Hiện thực CRUD đơn hàng bằng OrderRepository. */
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    /** Inject lớp truy cập database đơn hàng. */
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    /** Đọc toàn bộ đơn hàng. */
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    /** Tìm đơn hàng; trả về null nếu không tìm thấy. */
    public Orders findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    /** Lưu đơn hàng mới hoặc entity có sẵn id. */
    public void save(Orders orders) {
        orderRepository.save(orders);
    }

    @Override
    /** Cập nhật bản ghi cũ nếu id tồn tại, sau đó lưu thay đổi. */
    public void update(Integer id, Orders orders) {

        Orders oldOrder = orderRepository.findById(id).orElse(null);

        if (oldOrder != null) {

            oldOrder.setOrderNum(orders.getOrderNum());
            oldOrder.setAmount(orders.getAmount());
            oldOrder.setCustomerName(orders.getCustomerName());
            oldOrder.setCustomerEmail(orders.getCustomerEmail());
            oldOrder.setCustomerPhone(orders.getCustomerPhone());
            oldOrder.setCustomerAddress(orders.getCustomerAddress());
            oldOrder.setOrderDate(orders.getOrderDate());
            oldOrder.setIsDelete(orders.getIsDelete());
            oldOrder.setDeletedAt(orders.getDeletedAt());
            oldOrder.setCreatedAt(orders.getCreatedAt());
            oldOrder.setUpdatedAt(orders.getUpdatedAt());

            orderRepository.save(oldOrder);
        }
    }

    @Override
    /** Xóa cứng đơn hàng theo id. */
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }
}
