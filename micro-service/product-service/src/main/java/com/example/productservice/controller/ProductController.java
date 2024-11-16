package com.example.productservice.controller;

import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Products;
import com.example.productservice.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ProductResponse<List<Products>> getAllProducts(
            @RequestParam(required = false) String product_name,
            @RequestParam(required = false) Integer category_id,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService.getAllProducts(product_name, category_id, status, page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse<Products> getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponse<Products> createProduct(@RequestBody Products product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ProductResponse<Products> updateProduct(@PathVariable int id, @RequestBody Products product) {
        product.setProduct_id(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public ProductResponse<Void> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }
}
