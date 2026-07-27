package org.example.shopping.model;
import java.math.BigDecimal;

/** Dòng sản phẩm hiển thị trong giỏ hàng. */
public class CartItemResponse {
    /** ID sản phẩm để frontend gọi API cập nhật hoặc xóa. */
    private Integer productId;
    /** Mã hiển thị của sản phẩm. */
    private String code;
    /** Tên sản phẩm hiển thị trong giỏ. */
    private String name;
    /** Đơn giá hiện tại lấy từ database. */
    private BigDecimal price;
    /** Số lượng khách đã chọn. */
    private Integer quantity;
    /** Thành tiền của dòng giỏ: price × quantity. */
    private BigDecimal subtotal;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
