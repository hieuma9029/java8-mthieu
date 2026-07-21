package org.example.shopping.service.impl;

import org.example.shopping.entity.Products;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** Hiện thực ProductService, thao tác dữ liệu sản phẩm qua ProductRepository. */
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /** Inject lớp truy cập database sản phẩm. */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    /** Đọc toàn bộ sản phẩm. */
    public List<Products> findAll() {
        return productRepository.findAll();
    }

    @Override
    /** Lưu sản phẩm mới hoặc entity đã có id. */
    public void save(Products product) {
        productRepository.save(product);
    }

    @Override
    /** Tìm sản phẩm; trả về null nếu không tìm thấy. */
    public Products findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    /** Chỉ sao chép code, tên, giá và ảnh khi sản phẩm cũ tồn tại. */
    public void update(Integer id, Products products) {

        Products oldProduct = productRepository.findById(id).orElse(null);

        if (oldProduct != null) {
            oldProduct.setCode(products.getCode());
            oldProduct.setName(products.getName());
            oldProduct.setPrice(products.getPrice());
            oldProduct.setImage(products.getImage());

            productRepository.save(oldProduct);
        }
    }

    @Override
    /** Xóa cứng sản phẩm theo id. */
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
}
