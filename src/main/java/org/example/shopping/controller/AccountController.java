package org.example.shopping.controller;

import org.example.shopping.entity.Accounts;
import org.example.shopping.model.AccountRequest;
import org.example.shopping.model.AccountResponse;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
/** API REST quản lý tài khoản tại đường dẫn /accounts. */
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Khởi tạo controller với tầng nghiệp vụ tài khoản.
     *
     * @param accountService  service xử lý nghiệp vụ tài khoản
     * @param accountRepository repository truy vấn tài khoản
     * @param passwordEncoder bộ mã hóa mật khẩu trước khi lưu vào database
     */
    public AccountController(AccountService accountService,
                             AccountRepository accountRepository,
                             PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    /** GET /accounts: trả về tất cả tài khoản. */
    public List<AccountResponse> getAllAccounts() {
        return accountService.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    /**
     * GET /accounts/{id}: lấy một tài khoản theo mã định danh trên URL.
     *
     * @param id mã tài khoản trên URL
     * @return tài khoản tìm được
     */
    public AccountResponse getAccountById(@PathVariable Integer id) {
        Accounts account = accountService.findById(id);
        if (account == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return toResponse(account);
    }

    @PostMapping
    /**
     * POST /accounts: nhận JSON và tạo tài khoản mới.
     *
     * @param request dữ liệu tài khoản do client gửi; mật khẩu gốc sẽ được mã hóa
     */
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAccount(@Valid @RequestBody AccountRequest request) {
        if (accountRepository.findByUserName(request.getUserName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại");
        }

        Accounts accounts = new Accounts();
        accounts.setUserName(request.getUserName());
        accounts.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
        accounts.setUserRole(normalizeRole(request.getUserRole()));
        accounts.setActive(true);
        accounts.setCreatedAt(LocalDateTime.now());
        accountService.save(accounts);
    }

    @PutMapping("/{id}")
    /**
     * PUT /accounts/{id}: cập nhật tài khoản có id tương ứng.
     *
     * @param id mã tài khoản cần cập nhật
     * @param request dữ liệu tài khoản mới; mật khẩu gốc sẽ được mã hóa
     */
    public void updateAccount(@PathVariable Integer id,
                              @Valid @RequestBody AccountRequest request) {
        Accounts accounts = accountService.findById(id);
        if (accounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        Accounts existingUser = accountRepository.findByUserName(request.getUserName());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại");
        }

        accounts.setUserName(request.getUserName());
        accounts.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
        accounts.setUserRole(normalizeRole(request.getUserRole()));
        accounts.setUpdatedAt(LocalDateTime.now());
        accountService.update(id, accounts);
    }

    @DeleteMapping("/{id}")
    /**
     * DELETE /accounts/{id}: xóa bản ghi tài khoản.
     *
     * @param id mã tài khoản cần xóa
     */
    public void deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
    }

    /** Chuyển entity tài khoản sang DTO an toàn, không chứa mật khẩu hoặc hash mật khẩu. */
    private AccountResponse toResponse(Accounts account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setUserName(account.getUserName());
        response.setUserRole(account.getUserRole());
        response.setActive(account.getActive());
        response.setName(account.getName());
        response.setEmail(account.getEmail());
        response.setPhone(account.getPhone());
        response.setAddress(account.getAddress());
        return response;
    }

    /** Chuẩn hóa role theo định dạng Spring Security, ví dụ ADMIN thành ROLE_ADMIN. */
    private String normalizeRole(String role) {
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
