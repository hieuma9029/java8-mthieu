package org.example.shopping.entity;

import javax.persistence.*;

/** Giỏ hàng đang hoạt động của một tài khoản. */
@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(columnNames = "account_id"))
public class Carts extends BaseEntity {

    /** Khóa chính của giỏ hàng. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Tài khoản sở hữu giỏ hàng; mỗi tài khoản chỉ có một giỏ đang hoạt động. */
    @OneToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }
}
