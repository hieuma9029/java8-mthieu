package org.example.shopping.controller;

import org.example.shopping.model.CartItemRequest;
import org.example.shopping.model.CartResponse;
import org.example.shopping.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/** API giỏ hàng của tài khoản đang đăng nhập. */
@RestController
@RequestMapping("/cart")
public class CartController {
    /** Service xử lý giỏ hàng theo tài khoản trong phiên đăng nhập hiện tại. */
    private final CartService cartService;

    /**
     * Khởi tạo controller giỏ hàng.
     *
     * @param cartService service chứa nghiệp vụ thêm, sửa, xóa và đọc giỏ hàng
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    /** GET /cart: lấy các sản phẩm và tổng tiền trong giỏ của người dùng hiện tại. */
    public CartResponse getCart() {
        return cartService.getCurrentCart();
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    /** POST /cart/items: thêm sản phẩm vào giỏ hoặc cộng dồn số lượng nếu đã có. */
    public CartResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(request);
    }

    @PutMapping("/items/{productId}")
    /** PUT /cart/items/{productId}: thay thế số lượng của một sản phẩm trong giỏ. */
    public CartResponse updateItem(@PathVariable Integer productId,
                                   @Valid @RequestBody CartItemRequest request) {
        return cartService.updateItem(productId, request);
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /** DELETE /cart/items/{productId}: loại một sản phẩm khỏi giỏ hiện tại. */
    public void removeItem(@PathVariable Integer productId) {
        cartService.removeItem(productId);
    }
}
