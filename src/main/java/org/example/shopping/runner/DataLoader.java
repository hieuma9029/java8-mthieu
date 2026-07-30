package org.example.shopping.runner;

import org.example.shopping.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
/**
 * Component chạy khi ứng dụng khởi động để chuẩn bị dữ liệu mẫu hoặc cấu hình ban đầu.
 * Hiện tại lớp này chưa thực hiện thao tác nạp dữ liệu, nhưng giữ vai trò mở rộng cho các tác vụ khởi tạo.
 */
public class DataLoader implements CommandLineRunner {
    /**
     * Khởi tạo component với repository sản phẩm.
     *
     * @param productRepository repository có thể dùng để nạp dữ liệu mẫu
     */
    public DataLoader(ProductRepository productRepository) {
    }

    @Override
    /**
     * Được Spring gọi sau khi khởi động; hiện chưa nạp dữ liệu nào.
     *
     * @param args các đối số dòng lệnh của ứng dụng
     */
    public void run(String... args) throws Exception {
    }
}
