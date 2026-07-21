package org.example.shopping.controller;

import org.example.shopping.entity.Products;
import org.example.shopping.model.ProductRequest;
import org.example.shopping.model.ProductResponse;
import org.example.shopping.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
/** API REST quản lý sản phẩm tại /products. */
public class ProductController {

    private final ProductService productService;

    /** Inject tầng nghiệp vụ sản phẩm. */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    /** GET /products: lấy danh sách sản phẩm. */
    public List<ProductResponse> getAllProducts() {

        return productService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    /** GET /products/{id}: lấy sản phẩm theo id. */
    public ProductResponse getProductById(@PathVariable Integer id) {

        Products product = productService.findById(id);

        return toResponse(product);
    }

    @PostMapping
    /** POST /products: tạo sản phẩm; @Valid kiểm tra dữ liệu đầu vào. */
    public void saveProduct(@Valid @RequestBody ProductRequest request) {

        Products product = new Products();

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productService.save(product);
    }

    @PutMapping("/{id}")
    /** PUT /products/{id}: cập nhật sản phẩm và kiểm tra dữ liệu đầu vào. */
    public void updateProduct(@PathVariable Integer id,
                              @Valid @RequestBody ProductRequest request) {

        Products product = new Products();

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    /** DELETE /products/{id}: xóa sản phẩm. */
    public void deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
    }

    /** Chuyển từ Entity sang Response DTO. */
    private ProductResponse toResponse(Products product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }
}