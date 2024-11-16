package com.example.productservice.repository;

import com.example.productservice.model.Products;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Products> productRowMapper = (rs, rowNum) -> {
        Products product = new Products();
        product.setProduct_id(rs.getInt("product_id"));
        product.setCategory_id(rs.getInt("category_id"));
        product.setProduct_name(rs.getString("product_name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setCreate_at(rs.getTimestamp("create_at"));
        product.setUpdate_at(rs.getTimestamp("update_at"));
        product.setProduct_img(rs.getString("product_img"));
        product.setProduct_description(rs.getString("product_description"));
        product.setStatus(Products.Status.valueOf(rs.getString("status")));
        return product;
    };

    public List<Products> getAllProducts(String productName, Integer categoryId, String status, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Products WHERE 1=1");

        if (productName != null && !productName.isEmpty()) {
            sql.append(" AND product_name LIKE ?");
        }
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        sql.append(" LIMIT ? OFFSET ?");

        int offset = page * size;

        List<Object> params = new ArrayList<>();
        if (productName != null && !productName.isEmpty()) {
            params.add("%" + productName + "%");
        }
        if (categoryId != null) {
            params.add(categoryId);
        }
        if (status != null && !status.isEmpty()) {
            params.add(status);
        }
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), params.toArray(), productRowMapper);
    }

    public Products getProductById(int product_id) {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, product_id);
    }

    public int createProduct(Products product) {
        String sql = "INSERT INTO Products (category_id, product_name, price, stock, product_img, product_description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, product.getCategory_id(), product.getProduct_name(), product.getPrice(),
                product.getStock(), product.getProduct_img(), product.getProduct_description());
    }

    public int updateProduct(Products product) {
        String sql = "UPDATE Products SET category_id = ?, product_name = ?, price = ?, stock = ?, product_img = ?, " +
                "product_description = ?, update_at = CURRENT_TIMESTAMP WHERE product_id = ?";
        return jdbcTemplate.update(sql, product.getCategory_id(), product.getProduct_name(), product.getPrice(),
                product.getStock(), product.getProduct_img(), product.getProduct_description(), product.getProduct_id());
    }

    public int changeProductStatus(Products product) {
        String sql = "UPDATE Products SET status = ? WHERE product_id = ?";
        return jdbcTemplate.update(sql, product.getStatus(), product.getProduct_id());
    }

    public int deleteProduct(int product_id) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        return jdbcTemplate.update(sql, product_id);
    }
}
