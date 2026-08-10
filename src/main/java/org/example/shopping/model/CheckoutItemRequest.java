package org.example.shopping.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/** Một sản phẩm và số lượng cần đặt từ giỏ hàng. */
public class CheckoutItemRequest {

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Integer productId;

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
