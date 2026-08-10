package org.example.shopping.order.repository;

import org.example.shopping.entity.OrderDetails;
import org.example.shopping.entity.OrderStatus;
import org.example.shopping.entity.Orders;
import org.example.shopping.order.model.BestSellingProductResponse;
import org.example.shopping.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT new org.example.shopping.order.model.BestSellingProductResponse(" +
            "od.products.id, od.products.name, SUM(od.quantity), COALESCE(SUM(od.amount), 0)) " +
            "FROM OrderDetails od " +
            "WHERE od.orders.isDelete = false AND od.orders.status = :status " +
            "GROUP BY od.products.id, od.products.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<BestSellingProductResponse> findTopProductsByStatus(OrderStatus status, Pageable pageable);
}
