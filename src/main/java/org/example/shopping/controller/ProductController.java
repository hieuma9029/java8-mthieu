package org.example.shopping.controller;

import org.example.shopping.entity.Products;
import org.example.shopping.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Products> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Products getProductById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    @PostMapping
    public void saveProduct(@RequestBody Products products) {
        productService.save(products);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Integer id,
                              @RequestBody Products products) {

        productService.update(id, products);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
    }
}