package org.example.shopping.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
/**
 * Entity đại diện cho một dòng sản phẩm nằm trong đơn hàng.
 * Mỗi bản ghi lưu sản phẩm đã được chọn, số lượng và giá trị tiền tại thời điểm đặt hàng.
 */
public class OrderDetails extends BaseEntity {
    /** Khóa chính tự tăng của dòng chi tiết. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Column
    private Integer quantity;

    /** Nhiều chi tiết thuộc về một đơn hàng, lưu khóa ngoại order_id. */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    /** Nhiều chi tiết có thể tham chiếu cùng một sản phẩm qua product_id. */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    /** Các trường xóa mềm và lịch sử tạo/cập nhật. */
    /** Các getter/setter bên dưới đọc hoặc cập nhật các thuộc tính chi tiết đơn. */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

}
