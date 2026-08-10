package org.example.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
/** Điểm khởi động: tạo ApplicationContext và chạy web server Spring Boot. */
public class ShoppingApplication {
    /**
     * Khởi chạy toàn bộ ứng dụng với cấu hình mặc định của Spring Boot.
     *
     * @param args các đối số dòng lệnh truyền cho Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class, args);
    }
}