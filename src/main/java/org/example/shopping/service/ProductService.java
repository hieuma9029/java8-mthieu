package org.example.shopping.service;

import org.example.shopping.entity.Product;

import java.util.List;

public interface ProductService {

    Product save(Product product);

    List<Product> findAll();
}