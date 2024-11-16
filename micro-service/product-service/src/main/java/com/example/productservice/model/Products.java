package com.example.productservice.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class Products {
    private int product_id;
    private int category_id;
    private String product_name;
    private BigDecimal price;
    private int stock;
    private Timestamp create_at;
    private Timestamp update_at;
    private String product_img;
    private String product_description;
    private Status status = Status.Available;

    public enum Status {
        Available, Unavailable, OutOfStock
    }
}
