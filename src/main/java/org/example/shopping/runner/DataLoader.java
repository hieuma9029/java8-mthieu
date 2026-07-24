package org.example.shopping.runner;

import org.example.shopping.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
/** Component chạy khi ứng dụng khởi động; có thể dùng để nạp dữ liệu mẫu. */
public class DataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    /**
     * Khởi tạo component với repository sản phẩm.
     *
     * @param productRepository repository có thể dùng để nạp dữ liệu mẫu
     */
    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
