package org.example.shopping.service;

import org.example.shopping.entity.Products;

/** Service chuyên trách cho các nghiệp vụ liên quan đến sản phẩm. */
public interface ProductService extends BaseService<Products, Integer> {

    /**
     * Kiểm tra mã sản phẩm đã tồn tại trong hệ thống.
     *
     * @param code mã sản phẩm cần kiểm tra
     * @return true nếu mã đã tồn tại, false nếu chưa
     */
    boolean existsByCode(String code);

    /**
     * Kiểm tra tên sản phẩm đã được sử dụng bởi một sản phẩm đang hoạt động hay chưa.
     * Việc so sánh phân biệt chữ hoa/chữ thường và không tự cắt khoảng trắng.
     *
     * @param name tên sản phẩm cần kiểm tra
     * @return true nếu có tên giống hệt, false nếu chưa có
     */
    boolean existsByName(String name);

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
