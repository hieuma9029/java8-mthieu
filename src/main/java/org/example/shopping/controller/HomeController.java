package org.example.shopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // Cả ADMIN và USER đều có thể truy cập.
    @GetMapping("/profile")
    public String profile() {
        return "Xin chào, bạn đã đăng nhập!";
    }

    // Trang chủ để test.
    @GetMapping("/")
    public String home() {
        return "Home";
    }
}