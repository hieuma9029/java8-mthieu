package org.example.shopping.service;

import org.example.shopping.entity.Products;

import java.util.List;

/** Hợp đồng nghiệp vụ CRUD dành cho sản phẩm. */
public interface ProductService {

    /** Lấy tất cả sản phẩm. */
    List<Products> findAll();

    /** Tìm sản phẩm theo id. */
    Products findById(Integer id);

    /** Lưu sản phẩm mới. */
    void save(Products products);

    /** Cập nhật sản phẩm theo id. */
    void update(Integer id, Products products);

    /** Xóa sản phẩm theo id. */
    void delete(Integer id);
}
