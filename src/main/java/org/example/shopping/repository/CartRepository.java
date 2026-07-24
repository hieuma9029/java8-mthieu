package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;
import org.example.shopping.entity.Carts;

/** Repository truy cập giỏ hàng theo tài khoản. */
public interface CartRepository extends BaseRepository<Carts, Integer> {
    /** @return giỏ hàng đang hoạt động của tài khoản, hoặc {@code null} nếu chưa có. */
    Carts findByAccount(Accounts account);
}
