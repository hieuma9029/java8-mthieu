package org.example.shopping.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/** Dữ liệu thêm hoặc cập nhật một sản phẩm trong giỏ hàng. */
public class CartItemRequest {

    /** ID sản phẩm được thêm hoặc cập nhật trong giỏ. */
    @NotNull(message = "Mã sản phẩm không được để trống")
    private Integer productId;

    /** Số lượng cần thêm hoặc số lượng mới khi cập nhật giỏ. */
    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
