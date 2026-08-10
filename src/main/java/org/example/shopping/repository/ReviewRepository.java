package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.Products;
import org.example.shopping.entity.Review;
import org.example.shopping.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository truy cập dữ liệu cho thực thể {@link Review}.
 */
public interface ReviewRepository extends BaseRepository<Review, Integer> {

    /**
     * Lấy danh sách review của một sản phẩm.
     *
     * @param products sản phẩm cần lấy review
     * @return danh sách review
     */
    List<Review> findByProducts(Products products);

    /**
     * Kiểm tra review đã tồn tại cho cùng product, cùng order, cùng account.
     *
     * @param products sản phẩm
     * @param orders đơn hàng
     * @param account tài khoản
     * @return review nếu tồn tại
     */
    Review findByProductsAndOrdersAndAccount(Products products, Orders orders, Accounts account);

    List<Review> findByOrdersAndAccount(Orders orders, Accounts account);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.products = :product")
    Double findAverageRatingByProducts(@Param("product") Products product);

    long countByProducts(Products product);
}
