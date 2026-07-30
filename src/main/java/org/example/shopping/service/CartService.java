package org.example.shopping.service;

import org.example.shopping.entity.Carts;
import org.example.shopping.model.CartItemRequest;
import org.example.shopping.model.CartResponse;

/** Giao diện nghiệp vụ giỏ hàng cho tài khoản đang đăng nhập. */
public interface CartService {
    /** @return giỏ hàng hiện tại ở định dạng dành cho frontend hiển thị. */
    CartResponse getCurrentCart();

    /** @return giỏ hàng sau khi thêm sản phẩm theo yêu cầu. */
    CartResponse addItem(CartItemRequest request);

    /** @return giỏ hàng sau khi thay đổi số lượng một sản phẩm. */
    CartResponse updateItem(Integer productId, CartItemRequest request);

    /** Xóa sản phẩm khỏi giỏ hàng của tài khoản đang đăng nhập. */
    void removeItem(Integer productId);

    /**
     * Lấy entity giỏ hàng để các nghiệp vụ nội bộ, chẳng hạn checkout, sử dụng.
     *
     * @return entity giỏ hàng hoặc {@code null} khi người dùng chưa từng thêm sản phẩm
     */
    Carts getCurrentCartEntity();

    /**
     * Merge anonymous session cart (identified by sessionId) into the account's cart after login.
     * If the account has no cart, the session cart is assigned to the account.
     * @param sessionId id stored in HttpSession attribute CART_ID
     * @param accountAccount account to merge into
     */
    void mergeSessionCartIntoAccount(String sessionId, org.example.shopping.entity.Accounts accountAccount);
}
