package org.example.shopping.repository;

import org.example.shopping.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/** Data access cho OrderDetails; JpaRepository tự tạo các truy vấn CRUD cơ bản. */
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {

}
