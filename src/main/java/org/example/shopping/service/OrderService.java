package org.example.shopping.service;

import org.example.shopping.entity.Orders;

import java.util.List;

/** Hợp đồng nghiệp vụ CRUD dành cho đơn hàng. */
public interface OrderService {

    /** Lấy tất cả đơn hàng. */
    List<Orders> findAll();

    /** Tìm đơn hàng theo id. */
    Orders findById(Integer id);

    /** Lưu đơn hàng mới. */
    void save(Orders orders);

    /** Cập nhật đơn hàng theo id. */
    void update(Integer id, Orders orders);

    /** Xóa đơn hàng theo id. */
    void delete(Integer id);
}
