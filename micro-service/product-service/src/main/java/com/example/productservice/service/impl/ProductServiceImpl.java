package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Products;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    public ProductServiceImpl(ProductRepository productRepository, JdbcTemplate jdbcTemplate) {
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductResponse<List<Products>> getAllProducts(String product_name, Integer category_id, String status, int page, int size) {
        try {
            List<Products> products = productRepository.getAllProducts(product_name, category_id, status, page, size);
            String countSql = "SELECT COUNT(*) FROM Products WHERE 1=1";
            List<Object> params = new ArrayList<>();
            if (product_name != null && !product_name.isEmpty()) {
                countSql += " AND product_name LIKE ?";
                params.add("%" + product_name + "%");
            }
            if (category_id != null) {
                countSql += " AND category_id = ?";
                params.add(category_id);
            }
            if (status != null && !status.isEmpty()) {
                countSql += " AND status = ?";
                params.add(status);
            }

            List<Integer> totalResults = jdbcTemplate.query(countSql, params.toArray(), (rs, rowNum) -> rs.getInt(1));
            long totalElements = totalResults.isEmpty() ? 0 : totalResults.get(0);
            return new ProductResponse<>("200", "Successfully", List.of(products), totalElements);
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null, 0L);
        }
    }

    @Override
    public ProductResponse<Products> getProductById(int product_id) {
        try {
            Products product = productRepository.getProductById(product_id);
            if (product == null) {
                return new ProductResponse<>("404", "Product with ID " + product_id + " not found.", null);
            }
            return new ProductResponse<>("200", "Successfully", List.of(product));
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ProductResponse<Products> createProduct(Products product) {
        try {
            productRepository.createProduct(product);
            return new ProductResponse<>("201", "Successfully", List.of(product));
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ProductResponse<Products> updateProduct(Products product) {
        try {
            productRepository.updateProduct(product);
            return new ProductResponse<>("200", "Successfully", List.of(product));
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ProductResponse<Products> changeProductStatus(Products product) {
        try {
            productRepository.changeProductStatus(product);
            return new ProductResponse<>("200", "Successfully", List.of(product));
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ProductResponse<Void> deleteProduct(int product_id) {
        try {
            int result = productRepository.deleteProduct(product_id);
            if (result == 0) {
                return new ProductResponse<>("404", "Product with ID " + product_id + " not found.", null);
            }
            return new ProductResponse<>("200", "Successfully", null);
        } catch (Exception e) {
            return new ProductResponse<>("500", "An unexpected error occurred: " + e.getMessage(), null);
        }
    }
}
