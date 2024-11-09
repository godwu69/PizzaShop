package com.example.userservicespring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private LocalDate dob;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role = Role.Member;

    @Column
    private LocalDateTime created_at = LocalDateTime.now();

    @Column
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Active;

    public enum Gender {
        M, F
    }

    public enum Role {
        Member, Staff, Admin
    }

    public enum Status {
        Active, Inactive
    }

}
