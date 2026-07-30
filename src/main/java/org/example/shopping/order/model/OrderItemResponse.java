package org.example.shopping.order.model;
import java.math.BigDecimal;

/** Một dòng sản phẩm đã được chốt trong đơn hàng. */
public class OrderItemResponse {
    /** ID sản phẩm của dòng hàng đã đặt. */
    private Integer productId;
    /** Mã sản phẩm tại thời điểm trả dữ liệu. */
    private String code;
    /** Tên sản phẩm hiển thị trong chi tiết đơn. */
    private String name;
    /** Ảnh nhị phân; Jackson trả về chuỗi Base64 trong JSON. */
    private byte[] image;
    /** Đơn giá đã được chốt khi checkout. */
    private BigDecimal price;
    /** Số lượng khách đã đặt. */
    private Integer quantity;
    /** Thành tiền đã được chốt của dòng đơn hàng. */
    private BigDecimal subtotal;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
