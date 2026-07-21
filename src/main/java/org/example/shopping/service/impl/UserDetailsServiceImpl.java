package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Đăng ký lớp này là một Spring service để Spring Security có thể sử dụng khi xác thực.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repository dùng để truy vấn thông tin tài khoản từ cơ sở dữ liệu.
    private final AccountRepository accountRepository;

    // Spring tự tiêm AccountRepository vào service thông qua constructor.
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Spring Security gọi phương thức này để tải người dùng theo tên đăng nhập.
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Tìm tài khoản có userName trùng với tên đăng nhập được cung cấp.
        Accounts account = accountRepository.findByUserName(username);

        // Dừng quá trình đăng nhập và báo lỗi nếu không tìm thấy tài khoản.
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Trả về tài khoản vì Accounts triển khai UserDetails, chứa dữ liệu cho Spring Security xác thực.
        return account;
    }
}
