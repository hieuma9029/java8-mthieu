package org.example.shopping.service.impl;

import org.example.shopping.entity.Products;
import org.example.shopping.repository.ProductRepository;
import org.example.shopping.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Products> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void save(Products product) {
        productRepository.save(product);
    }

    @Override
    public Products findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
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
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
}