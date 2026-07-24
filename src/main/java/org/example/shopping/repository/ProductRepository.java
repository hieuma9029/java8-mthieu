package org.example.shopping.repository;

import org.example.shopping.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository truy cập dữ liệu cho thực thể {@link Products}.
 *
 * <p>Kế thừa {@link BaseRepository} để sử dụng các thao tác CRUD có sẵn.</p>
 */
public interface ProductRepository extends BaseRepository<Products, Integer> {

    /**
     * Kiểm tra mã sản phẩm đã tồn tại hay chưa.
     *
     * @param code mã sản phẩm
     * @return true nếu tồn tại
     */
    boolean existsByCode(String code);

    /**
     * Lấy tất cả sản phẩm chưa bị xóa.
     *
     * @return danh sách sản phẩm
     */
    List<Products> findByIsDeleteFalse();

    /**
     * Lấy danh sách sản phẩm chưa bị xóa theo phân trang.
     *
     * @param pageable thông tin phân trang
     * @return trang sản phẩm
     */
    Page<Products> findByIsDeleteFalse(Pageable pageable);
}
