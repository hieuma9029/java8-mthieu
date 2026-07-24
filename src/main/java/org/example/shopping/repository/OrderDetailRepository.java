package org.example.shopping.repository;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.Orders;

import java.util.List;

/**
 * Repository truy cập dữ liệu cho thực thể {@link OrderDetails}.
 *
 * <p>Kế thừa {@link BaseRepository} để sử dụng các thao tác CRUD có sẵn như
 * tìm kiếm, lưu, cập nhật và xóa dòng chi tiết đơn hàng.</p>
 *
 * @see OrderDetails
 * @see BaseRepository
 */
public interface OrderDetailRepository extends BaseRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrders(Orders orders);
}
