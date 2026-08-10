package org.example.shopping.entity;

import javax.persistence.*;

/** Giỏ hàng đang hoạt động của một tài khoản. */
@Entity
@Table(name = "carts")
public class Carts extends BaseEntity {

    /** Khóa chính của giỏ hàng. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Tài khoản sở hữu giỏ hàng; có thể null cho anonymous cart. */
    @OneToOne(optional = true)
    @JoinColumn(name = "account_id", nullable = true)
    private Accounts account;

    /** Id phiên/session để liên kết anonymous cart với HttpSession. */
    @Column(name = "session_id", unique = true)
    private String sessionId;

    /** Thời điểm hết hạn của giỏ hàng anonymous; giỏ của tài khoản không dùng trường này. */
    @Column(name = "expires_at")
    private java.time.LocalDateTime expiresAt;

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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public java.time.LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(java.time.LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
