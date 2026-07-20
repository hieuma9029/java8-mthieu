package org.example.shopping.service;

import org.example.shopping.entity.Products;

import java.util.List;

public interface ProductService {

    List<Products> findAll();

    Products findById(Integer id);

    void save(Products products);

    void update(Integer id, Products products);

    void delete(Integer id);
}