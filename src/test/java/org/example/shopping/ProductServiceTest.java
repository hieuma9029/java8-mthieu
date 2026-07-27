package org.example.shopping;

import org.example.shopping.entity.Products;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSoftDeleteProduct() {
        Products product = new Products();
        product.setCode("SP999");
        product.setName("Test Product");
        product.setPrice(BigDecimal.TEN);
        product.setQuantity(10);
        product.setIsDelete(false);
        Products saved = productRepository.save(product);

        productService.delete(saved.getId());
        Products deleted = productRepository.findById(saved.getId()).orElse(null);

        assertTrue(deleted != null && Boolean.TRUE.equals(deleted.getIsDelete()));
        assertFalse(productService.findById(saved.getId()) != null);
    }
}
