package org.example.shopping.repository;

import org.example.shopping.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

/** Data access cho Products; JpaRepository hỗ trợ find, save và delete. */
public interface ProductRepository extends JpaRepository<Products, Integer> {

}
