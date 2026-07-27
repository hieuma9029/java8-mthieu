package org.example.shopping.repository;

import org.example.shopping.entity.Accounts;

/** Data access cho Accounts; BaseRepository cung cấp sẵn CRUD theo id Integer. */
public interface AccountRepository extends BaseRepository<Accounts, Integer> {
    /**
     * Tìm tài khoản theo tên đăng nhập.
     *
     * @param userName tên đăng nhập cần tìm
     * @return tài khoản tương ứng, hoặc {@code null} nếu không tồn tại
     */
    Accounts findByUserName(String userName);

    /**
     * Tìm tài khoản chưa bị xóa mềm theo tên đăng nhập.
     */
    Accounts findByUserNameAndIsDeleteFalse(String userName);

    /**
     * Tìm tài khoản chưa bị xóa mềm theo id.
     */
    Accounts findByIdAndIsDeleteFalse(Integer id);

    /**
     * Trả về tất cả tài khoản chưa bị xóa mềm.
     */
    java.util.List<Accounts> findByIsDeleteFalse();
}
