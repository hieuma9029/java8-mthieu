package org.example.shopping.entity;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
/* Entity đại diện một dòng hàng trong đơn: sản phẩm, số lượng và giá tại lúc mua. */
public class OrderDetails extends BaseEntity {
    /** Khóa chính tự tăng của dòng chi tiết. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double amount;

    @Column
    private Double price;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
