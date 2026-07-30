package org.example.shopping.order.repository;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.Orders;
import org.example.shopping.repository.BaseRepository;

import java.util.List;

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
    List<Orders> findByAccountAndIsDeleteFalse(Accounts account);
}
