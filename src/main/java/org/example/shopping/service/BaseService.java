package org.example.shopping.service;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service cha dùng chung cho các Entity.
 */
public interface BaseService<T, ID> {

    /**
     * Lấy danh sách tất cả thực thể.
     *
     * @return danh sách các thực thể
     */
    List<T> findAll();

    /**
     * Lấy danh sách thực thể theo trang.
     *
     * @param page số trang bắt đầu từ 0
     * @param size số phần tử mỗi trang
     * @return trang chứa các thực thể
     */
    Page<T> findAll(int page, int size);

    /**
     * Tìm thực thể theo id.
     *
     * @param id mã định danh của thực thể
     * @return thực thể tương ứng hoặc {@code null} nếu không tìm thấy
     */
    T findById(ID id);

    /**
     * Lưu thực thể mới vào cơ sở dữ liệu.
     *
     * @param entity thực thể cần lưu
     */
    void save(T entity);

    /**
     * Cập nhật thực thể đã tồn tại theo id.
     *
     * @param id     mã định danh của thực thể cần cập nhật
     * @param entity dữ liệu thực thể mới
     */
    void update(ID id, T entity);

    /**
     * Xóa thực thể theo id.
     *
     * @param id mã định danh của thực thể cần xóa
     */
    void delete(ID id);
}