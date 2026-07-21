package org.example.shopping.controller;

import org.example.shopping.entity.Accounts;
import org.example.shopping.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
/** API REST quản lý tài khoản tại đường dẫn /accounts. */
public class AccountController {

    private final AccountService accountService;

    /** Inject tầng nghiệp vụ tài khoản qua constructor. */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    /** GET /accounts: trả về tất cả tài khoản. */
    public List<Accounts> getAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    /** GET /accounts/{id}: lấy một tài khoản theo id trên URL. */
    public Accounts getAccountById(@PathVariable Integer id) {
        return accountService.findById(id);
    }

    @PostMapping
    /** POST /accounts: nhận JSON và tạo tài khoản mới. */
    public void saveAccount(@RequestBody Accounts accounts) {
        accountService.save(accounts);
    }

    @PutMapping("/{id}")
    /** PUT /accounts/{id}: cập nhật tài khoản có id tương ứng. */
    public void updateAccount(@PathVariable Integer id,
                              @RequestBody Accounts accounts) {

        accountService.update(id, accounts);
    }

    @DeleteMapping("/{id}")
    /** DELETE /accounts/{id}: xóa bản ghi tài khoản. */
    public void deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
    }
}
