package com.example.userservice.repository;

import com.example.userservice.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM Users u WHERE " +
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:phone_number IS NULL OR u.phone_number LIKE %:phone_number%)")
    Page<Users> findUsersByParam(@Param("name") String name,
                                    @Param("email") String email,
                                    @Param("phone_number") String phone_number,
                                    Pageable pageable);
}
