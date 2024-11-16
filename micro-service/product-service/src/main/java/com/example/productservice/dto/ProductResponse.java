package com.example.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ProductResponse<T> {
    private String code;
    private String message;
    private List<T> data;
    private Long totalElements;

    public ProductResponse() {}

    public ProductResponse(String code, String message, List<T> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ProductResponse(String code, String message, List<T> data, Long totalElements) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.totalElements = totalElements;
    }
}
