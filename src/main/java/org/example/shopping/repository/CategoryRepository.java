package org.example.shopping.repository;

import org.example.shopping.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository truy cập dữ liệu cho thực thể {@link Category}.
 */
public interface CategoryRepository extends BaseRepository<Category, Integer> {

    /**
     * Kiểm tra tên danh mục đã tồn tại hay chưa.
     *
     * @param name tên danh mục
     * @return true nếu đã tồn tại
     */
    boolean existsByName(String name);

    /**
     * Lấy tất cả danh mục chưa bị xóa mềm.
     *
     * @return danh sách danh mục hoạt động
     */
    List<Category> findByIsDeleteFalse();

    /**
     * Lấy danh sách danh mục chưa bị xóa mềm theo phân trang.
     *
     * @param pageable thông tin phân trang
     * @return trang danh mục
     */
    Page<Category> findByIsDeleteFalse(Pageable pageable);
}
