package com.example.userservice.repository;

import com.example.userservice.model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Ratings, Integer> {
}
