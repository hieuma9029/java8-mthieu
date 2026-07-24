package org.example.shopping.entity;

import javax.persistence.*;

/** Một sản phẩm và số lượng tương ứng trong giỏ hàng. */
@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"}))
public class CartItems extends BaseEntity {

    /** Khóa chính của dòng giỏ hàng. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Giỏ hàng chứa dòng sản phẩm này. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Carts cart;

    /** Sản phẩm được khách chọn mua. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    /** Số lượng sản phẩm khách muốn mua; luôn phải lớn hơn 0. */
    @Column(nullable = false)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Carts getCart() {
        return cart;
    }

    public void setCart(Carts cart) {
        this.cart = cart;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
