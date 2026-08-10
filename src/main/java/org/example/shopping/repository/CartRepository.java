package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.Carts;

import java.time.LocalDateTime;
import java.util.List;

/** Repository truy cập giỏ hàng theo tài khoản. */
public interface CartRepository extends BaseRepository<Carts, Integer> {
    /** @return giỏ hàng đang hoạt động của tài khoản, hoặc {@code null} nếu chưa có. */
    Carts findByAccount(Accounts account);
    /** Tìm giỏ hàng theo session id (anonymous carts). */
    Carts findBySessionId(String sessionId);

    /** Tìm các cart anonymous đã hết hạn để dọn khỏi cơ sở dữ liệu. */
    List<Carts> findByAccountIsNullAndExpiresAtBefore(LocalDateTime time);
}
