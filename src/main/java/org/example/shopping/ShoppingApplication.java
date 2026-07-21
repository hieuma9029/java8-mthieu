package org.example.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/** Điểm khởi động: tạo ApplicationContext và chạy web server Spring Boot. */
public class ShoppingApplication {
    /** Khởi chạy toàn bộ ứng dụng với cấu hình mặc định của Spring Boot. */
    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class, args);
    }
}
