package org.example.shopping.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {
        "order_id", "product_id", "account_id"}))
/**
 * Entity đại diện cho đánh giá sản phẩm do người mua gửi sau khi đơn hàng đã nhận.
 * Mỗi đánh giá liên kết với tài khoản, đơn hàng và sản phẩm cụ thể.
 */
public class Review extends BaseEntity {

    /** Khóa chính tự tăng của đánh giá. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Điểm đánh giá sản phẩm từ 1 đến 5. */
    @Column(nullable = false)
    private Integer rating;

    /** Thời điểm đánh giá được tạo. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Sản phẩm được đánh giá. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products products;

    /** Tài khoản đã gửi đánh giá. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Accounts account;

    /** Đơn hàng chứa sản phẩm đã mua và nhận hàng. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
