package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
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

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Orders o WHERE o.isDelete = false AND o.status = :status")
    BigDecimal sumAmountByStatusAndIsDeleteFalse(OrderStatus status);

    long countByStatusAndIsDeleteFalse(OrderStatus status);
}
