package org.example.shopping.service.impl;

import org.example.shopping.entity.Products;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
/** Hiện thực ProductService, thao tác dữ liệu sản phẩm qua ProductRepository. */
public class ProductServiceImpl extends BaseServiceImpl<Products, Integer, ProductRepository> implements ProductService {

    /**
     * Khởi tạo service với repository truy cập dữ liệu sản phẩm.
     *
     * @param productRepository repository dùng cho các thao tác sản phẩm
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    /** Đọc toàn bộ các sản phẩm chưa bị xóa. */
    public List<Products> findAll() {
        return repository.findByIsDeleteFalse();
    }

    /**
     * Đọc danh sách sản phẩm theo từng trang.
     *
     * @param page số trang (bắt đầu từ 0)
     * @param size số phần tử trên mỗi trang
     * @return một trang dữ liệu sản phẩm
     */
    @Override
    public Page<Products> findAll(int page, int size) {
        return repository.findByIsDeleteFalse(PageRequest.of(page, size));
    }

    @Override
    /** Lưu sản phẩm mới. */
    public void save(Products product) {
        product.setIsDelete(false);
        product.setCreatedAt(LocalDateTime.now());
        repository.save(product);
    }

    @Override
    /** Tìm sản phẩm; trả về null nếu không tìm thấy hoặc đã bị xóa mềm. */
    public Products findById(Integer id) {
        Products product = repository.findById(id).orElse(null);
        if (product == null || Boolean.TRUE.equals(product.getIsDelete())) {
            return null;
        }
        return product;
    }

    @Override
    /** Cập nhật thông tin sản phẩm. */
    public void update(Integer id, Products products) {

        // Chỉ cập nhật khi sản phẩm tồn tại và chưa bị xóa mềm.
        Products oldProduct = findById(id);

        if (oldProduct != null) {

            oldProduct.setCode(products.getCode());
            oldProduct.setName(products.getName());
            oldProduct.setPrice(products.getPrice());
            oldProduct.setImage(products.getImage());

            oldProduct.setUpdatedAt(LocalDateTime.now());

            repository.save(oldProduct);
        }
    }

    @Override
    /** Xóa mềm sản phẩm theo id. */
    public void delete(Integer id) {

        // Chỉ xóa mềm khi sản phẩm còn tồn tại.
        Products product = findById(id);

        if (product != null) {

            product.setIsDelete(true);
            product.setDeletedAt(LocalDateTime.now());

            repository.save(product);
        }
    }

    @Override
    /** Khôi phục sản phẩm đã bị xóa mềm. */
    public void restore(Integer id) {
        // Không dùng findById(id) vì hàm đó ẩn sản phẩm có isDelete = true.
        Products product = repository.findById(id).orElse(null);

        if (product != null && Boolean.TRUE.equals(product.getIsDelete())) {
            if (existsByName(product.getName())) {
                throw new IllegalArgumentException("Tên sản phẩm đã tồn tại.");
            }
            product.setIsDelete(false);
            product.setDeletedAt(null);
            product.setUpdatedAt(LocalDateTime.now());
            repository.save(product);
        }
    }

    /**
     * Kiểm tra xem mã sản phẩm đã tồn tại hay chưa.
     *
     * @param code mã sản phẩm cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa tồn tại
     */
    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    /** Kiểm tra tên sản phẩm có đang được một sản phẩm chưa xóa mềm sử dụng hay không. */
    @Override
    public boolean existsByName(String name) {
        return repository.findByIsDeleteFalse().stream()
                .map(Products::getName)
                .anyMatch(name::equals);
    }

    /**
     * Tạo mã SP kế tiếp từ mã lớn nhất hiện có và bỏ qua mã đã tồn tại.
     * Ràng buộc unique tại database vẫn là lớp bảo vệ cuối cùng khi có request đồng thời.
     *
     * @return mã sản phẩm dạng SP001, SP002, ... chưa tồn tại tại thời điểm kiểm tra
     */
    @Override
    public String generateNextCode() {
        int largestNumber = repository.findByIsDeleteFalse().stream()
                .map(Products::getCode)
                .filter(code -> code != null)
                .map(Pattern.compile("^SP(\\d+)$")::matcher)
                .filter(Matcher::matches)
                .mapToInt(matcher -> Integer.parseInt(matcher.group(1)))
                .max()
                .orElse(0);

        String nextCode;
        do {
            largestNumber++;
            nextCode = String.format("SP%03d", largestNumber);
        } while (repository.existsByCode(nextCode));

        return nextCode;
    }

    @Override
    /**
     * Phương thức này không dùng trong ProductServiceImpl vì logic cập nhật
     * sản phẩm được xử lý tùy chỉnh trong {@link #update(Integer, Products)}.
     */
    protected void copyForUpdate(Products existing, Products source) {
        // Không dùng trong ProductServiceImpl vì logic cập nhật riêng.
    }
} 
