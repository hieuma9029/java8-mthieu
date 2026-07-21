package org.example.shopping.repository;

import org.example.shopping.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

/** Data access cho Orders; kế thừa các thao tác CRUD mặc định của Spring Data JPA. */
public interface OrderRepository extends JpaRepository<Orders, Integer> {

}
