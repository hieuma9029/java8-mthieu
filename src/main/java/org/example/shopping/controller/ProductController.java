package org.example.shopping.controller;

import org.example.shopping.entity.Products;
import org.example.shopping.model.ProductRequest;
import org.example.shopping.model.ProductResponse;
import org.example.shopping.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.example.shopping.model.PaginationResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
/** API REST quản lý sản phẩm tại /products. */
public class ProductController {

    private final ProductService productService;

    /**
     * Khởi tạo controller với tầng nghiệp vụ sản phẩm.
     *
     * @param productService service xử lý nghiệp vụ sản phẩm
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    /** GET /products: lấy danh sách sản phẩm. */
    public List<ProductResponse> getAllProducts() {
        System.out.println("===== ĐÃ VÀO PRODUCT CONTROLLER =====");
        return productService.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/page")
    /**
     * GET /products/page?page=0&size=5
     *
     * API lấy danh sách sản phẩm theo từng trang.
     */
    public PaginationResult<ProductResponse> getProductsByPage(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        Page<Products> productsPage = productService.findAll(page, size);

        PaginationResult<ProductResponse> result = new PaginationResult<>();

        result.setList(
                productsPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
        );

        result.setCurrentPage(productsPage.getNumber());
        result.setTotalPages(productsPage.getTotalPages());
        result.setTotalItems(productsPage.getTotalElements());
        result.setHasNext(productsPage.hasNext());
        result.setHasPrevious(productsPage.hasPrevious());

        return result;
    }

    @GetMapping("/{id}")
    /**
     * GET /products/{id}: lấy sản phẩm theo mã định danh.
     *
     * @param id mã sản phẩm trên URL
     * @return DTO phản hồi của sản phẩm tìm được
     */
    public ProductResponse getProductById(@PathVariable Integer id) {

        Products product = productService.findById(id);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
        }

        return toResponse(product);
    }

    @PostMapping
    /**
     * POST /products: tạo sản phẩm; {@code @Valid} kiểm tra dữ liệu đầu vào.
     *
     * @param request DTO chứa dữ liệu sản phẩm do client gửi
     */
    public void saveProduct(@Valid @RequestBody ProductRequest request) {

        String code = request.getCode();
        if (code == null || code.trim().isEmpty()) {
            code = productService.generateNextCode();
        }

        if (productService.existsByCode(code)) {
            throw new RuntimeException("Mã sản phẩm đã tồn tại.");
        }

        Products product = new Products();

        product.setCode(code);
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productService.save(product);
    }

    @PutMapping("/{id}")
    /**
     * PUT /products/{id}: cập nhật sản phẩm và kiểm tra dữ liệu đầu vào.
     *
     * @param id mã sản phẩm cần cập nhật
     * @param request DTO chứa dữ liệu sản phẩm mới
     */
    public void updateProduct(@PathVariable Integer id,
                              @Valid @RequestBody ProductRequest request) {

        Products existingProduct = productService.findById(id);
        if (existingProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
        }

        Products product = new Products();

        product.setCode(request.getCode() == null || request.getCode().trim().isEmpty()
                ? existingProduct.getCode()
                : request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    /**
     * DELETE /products/{id}: xóa sản phẩm.
     *
     * @param id mã sản phẩm cần xóa
     */
    public void deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
    }

    @PutMapping("/{id}/restore")
    /**
     * PUT /products/{id}/restore: khôi phục sản phẩm đã xóa mềm.
     *
     * @param id mã định danh sản phẩm
     */
    public void restoreProduct(@PathVariable Integer id) {
        productService.restore(id);
    }

    /**
     * Chuyển từ Entity sang Response DTO.
     *
     * @param product entity sản phẩm nguồn
     * @return DTO chỉ chứa dữ liệu cần trả về cho client
     */
    private ProductResponse toResponse(Products product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setCode(product.getCode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }
}
