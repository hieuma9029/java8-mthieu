package org.example.shopping.service;

import org.example.shopping.entity.Products;

/** Service cho các nghiệp vụ đặc thù của sản phẩm. */
public interface ProductService extends BaseService<Products, Integer> {

    /**
     * Kiểm tra mã sản phẩm đã tồn tại trong hệ thống.
     *
     * @param code mã sản phẩm cần kiểm tra
     * @return true nếu mã đã tồn tại, false nếu chưa
     */
    boolean existsByCode(String code);

    /**
     * Khôi phục sản phẩm đã bị xóa mềm.
     *
     * @param id mã định danh sản phẩm
     */
    void restore(Integer id);

    /**
     * Tạo mã sản phẩm tiếp theo theo định dạng SP001, SP002, ...
     *
     * @return mã sản phẩm mới chưa được sử dụng
     */
    String generateNextCode();
}
