package org.example.shopping.model;

/** Dòng sản phẩm hiển thị trong giỏ hàng. */
public class CartItemResponse {
    /** ID sản phẩm để frontend gọi API cập nhật hoặc xóa. */
    private Integer productId;
    /** Mã hiển thị của sản phẩm. */
    private String code;
    /** Tên sản phẩm hiển thị trong giỏ. */
    private String name;
    /** Đơn giá hiện tại lấy từ database. */
    private Double price;
    /** Số lượng khách đã chọn. */
    private Integer quantity;
    /** Thành tiền của dòng giỏ: price × quantity. */
    private Double subtotal;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
