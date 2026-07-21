package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

/** Data access cho Accounts; JpaRepository cung cấp sẵn CRUD theo id Integer. */
public interface AccountRepository extends JpaRepository<Accounts, Integer> {
    // Spring Data JPA tự tạo truy vấn để tìm một tài khoản theo trường userName.
    Accounts findByUserName(String userName);
}
