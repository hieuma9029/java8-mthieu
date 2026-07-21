package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** Hiện thực AccountService, điều phối CRUD tài khoản qua AccountRepository. */
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    /** Inject lớp truy cập database tài khoản. */
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    /** Đọc toàn bộ tài khoản từ database. */
    public List<Accounts> findAll() {
        return accountRepository.findAll();
    }

    @Override
    /** Tìm tài khoản; trả về null nếu không tồn tại. */
    public Accounts findById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    /** Lưu tài khoản mới hoặc entity đã có id. */
    public void save(Accounts accounts) {
        accountRepository.save(accounts);
    }

    @Override
    /** Tìm bản ghi cũ, sao chép các trường từ request rồi lưu lại. */
    public void update(Integer id, Accounts accounts) {

        Accounts oldAccount = accountRepository.findById(id).orElse(null);

        if (oldAccount != null) {

            oldAccount.setUserName(accounts.getUserName());
            oldAccount.setEncryptedPassword(accounts.getEncryptedPassword());
            oldAccount.setUserRole(accounts.getUserRole());
            oldAccount.setActive(accounts.getActive());
            oldAccount.setIsDelete(accounts.getIsDelete());
            oldAccount.setDeletedAt(accounts.getDeletedAt());
            oldAccount.setCreatedAt(accounts.getCreatedAt());
            oldAccount.setUpdatedAt(accounts.getUpdatedAt());

            accountRepository.save(oldAccount);
        }
    }

    @Override
    /** Xóa cứng tài khoản khỏi database theo id. */
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }
}
