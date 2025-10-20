package com.tvvina.tvvina.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role",length = 10)
    private String role;

    @Column(name = "username", length = 250)
    private String username;

    @Column(name = "password", length = 550)
    private String password;

    @Column(name = "phone",length = 10)
    private String phone;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "is_active")
    private Integer isActive;

}