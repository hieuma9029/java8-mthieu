package org.example.shopping.service;

import org.example.shopping.entity.Category;

/**
 * Service chuyên trách cho các nghiệp vụ liên quan đến danh mục sản phẩm.
 */
public interface CategoryService extends BaseService<Category, Integer> {

    /**
     * Kiểm tra tên danh mục đã được sử dụng bởi một danh mục đang hoạt động hay chưa.
     *
     * @param name tên danh mục cần kiểm tra
     * @return true nếu tên đã tồn tại
     */
    boolean existsByName(String name);

    /**
     * Khôi phục danh mục bị xóa mềm.
     *
     * @param id mã định danh danh mục
     */
    void restore(Integer id);
}
