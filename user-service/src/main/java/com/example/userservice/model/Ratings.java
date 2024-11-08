package com.example.userservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rating_id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer orderId;

    private LocalDateTime rating_date = LocalDateTime.now();;

    private String comments;

    @Column(nullable = false)
    private Integer rating;

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getRating_date() {
        return rating_date;
    }

    public void setRating_date(LocalDateTime rating_date) {
        this.rating_date = rating_date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
