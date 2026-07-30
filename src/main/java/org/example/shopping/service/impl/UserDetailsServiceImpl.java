package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Hiện thực dịch vụ tải người dùng cho quá trình xác thực của Spring Security.
 * Lớp này lấy tài khoản từ database để Spring Security có thể kiểm tra đăng nhập.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // Repository dùng để truy vấn thông tin tài khoản từ cơ sở dữ liệu.
    private final AccountRepository accountRepository;

    /**
     * Khởi tạo dịch vụ với repository truy xuất tài khoản.
     *
     * @param accountRepository repository dùng để tìm tài khoản
     */
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Tải người dùng theo tên đăng nhập cho Spring Security.
     *
     * @param username tên đăng nhập cần xác thực
     * @return thông tin người dùng tương ứng
     * @throws UsernameNotFoundException nếu không tìm thấy tài khoản
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Tìm tài khoản chưa bị xóa mềm theo tên đăng nhập.
        Accounts account = accountRepository.findByUserNameAndIsDeleteFalse(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return account;
    }
}
