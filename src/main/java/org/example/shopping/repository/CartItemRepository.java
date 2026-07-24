package org.example.shopping.repository;

import org.example.shopping.entity.CartItems;
import org.example.shopping.entity.Carts;
import org.example.shopping.entity.Products;

import java.util.List;

/** Repository truy cập các dòng sản phẩm trong giỏ hàng. */
public interface CartItemRepository extends BaseRepository<CartItems, Integer> {
    /** @return tất cả dòng sản phẩm thuộc một giỏ hàng. */
    List<CartItems> findByCart(Carts cart);

    /** @return dòng của sản phẩm trong giỏ, hoặc {@code null} nếu sản phẩm chưa được thêm. */
    CartItems findByCartAndProduct(Carts cart, Products product);
}
