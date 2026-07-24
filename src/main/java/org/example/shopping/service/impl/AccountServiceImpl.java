package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.springframework.stereotype.Service;

@Service
/** Hiện thực AccountService, điều phối CRUD tài khoản qua AccountRepository. */
public class AccountServiceImpl extends BaseServiceImpl<Accounts, Integer, AccountRepository> implements AccountService {

    /**
     * Khởi tạo service với repository truy cập dữ liệu tài khoản.
     *
     * @param accountRepository repository dùng cho các thao tác tài khoản
     */
    public AccountServiceImpl(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    /**
     * Sao chép dữ liệu cập nhật từ đối tượng nguồn vào tài khoản hiện có.
     *
     * @param existing tài khoản hiện có trong database
     * @param source   tài khoản chứa dữ liệu mới từ client
     */
    protected void copyForUpdate(Accounts existing, Accounts source) {
        existing.setUserName(source.getUserName());
        existing.setEncryptedPassword(source.getEncryptedPassword());
        existing.setUserRole(source.getUserRole());
        existing.setActive(source.getActive());
        existing.setIsDelete(source.getIsDelete());
        existing.setDeletedAt(source.getDeletedAt());
        existing.setCreatedAt(source.getCreatedAt());
        existing.setUpdatedAt(source.getUpdatedAt());
    }
} 
