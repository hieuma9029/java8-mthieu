package org.example.shopping.repository;

import org.example.shopping.entity.Orders;

/**
 * Repository truy cập dữ liệu cho thực thể {@link Orders}.
 *
 * <p>Kế thừa {@link BaseRepository} để sử dụng các thao tác CRUD có sẵn như
 * tìm kiếm, lưu, cập nhật và xóa đơn hàng.</p>
 *
 * @see Orders
 * @see BaseRepository
 */
public interface OrderRepository extends BaseRepository<Orders, Integer> {
}
