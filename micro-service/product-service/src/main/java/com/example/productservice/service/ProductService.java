package com.example.productservice.service;

import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Products;

import java.util.List;

public interface ProductService {
    ProductResponse<List<Products>> getAllProducts(String product_name, Integer category_id, String status, int page, int size);

    ProductResponse<Products> getProductById(int product_id);

    ProductResponse<Products> createProduct(Products product);

    ProductResponse<Products> updateProduct(Products product);

    ProductResponse<Products> changeProductStatus(Products product);

    ProductResponse<Void> deleteProduct(int product_id);
}
