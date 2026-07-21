package org.example.shopping.service;

import org.example.shopping.entity.OrderDetails;

import java.util.List;

/** Hợp đồng nghiệp vụ CRUD dành cho từng dòng chi tiết đơn hàng. */
public interface OrderDetailService {

    /** Lấy tất cả chi tiết đơn. */
    List<OrderDetails> findAll();

    /** Tìm chi tiết đơn theo id. */
    OrderDetails findById(Integer id);

    /** Lưu chi tiết đơn mới. */
    void save(OrderDetails orderDetails);

    /** Cập nhật chi tiết đơn theo id. */
    void update(Integer id, OrderDetails orderDetails);

    /** Xóa chi tiết đơn theo id. */
    void delete(Integer id);
}
