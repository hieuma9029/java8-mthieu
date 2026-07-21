package org.example.shopping.service;

import org.example.shopping.entity.Accounts;

import java.util.List;

/** Hợp đồng nghiệp vụ CRUD dành cho tài khoản. */
public interface AccountService {

    /** Lấy tất cả tài khoản. */
    List<Accounts> findAll();

    /** Tìm một tài khoản theo id. */
    Accounts findById(Integer id);

    /** Lưu tài khoản mới. */
    void save(Accounts accounts);

    /** Cập nhật tài khoản theo id. */
    void update(Integer id, Accounts accounts);

    /** Xóa tài khoản theo id. */
    void delete(Integer id);
}
