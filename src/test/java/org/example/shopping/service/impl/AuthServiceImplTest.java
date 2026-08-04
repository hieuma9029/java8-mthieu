package org.example.shopping.service.impl;

import org.example.shopping.entity.Accounts;
import org.example.shopping.model.RegisterRequest;
import org.example.shopping.repository.AccountRepository;
import org.example.shopping.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CartServiceImpl cartService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerShouldCreateNewAccountWhenUsernameIsAvailable() {
        RegisterRequest request = new RegisterRequest();
        request.setUserName("newuser");
        request.setPassword("123456");
        request.setName("Nguyễn Văn A");
        request.setEmail("a@example.com");

        when(accountRepository.findByUserNameAndIsDeleteFalse("newuser")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");

        ResponseEntity<Map<String, Object>> response = authService.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        verify(accountService).save(any(Accounts.class));
    }
}
