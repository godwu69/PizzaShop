package com.example.productservice.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Categories {
    private int category_id;
    private String category_name;
    private String category_description;
    private Timestamp create_at;
    private Timestamp update_at;
}
