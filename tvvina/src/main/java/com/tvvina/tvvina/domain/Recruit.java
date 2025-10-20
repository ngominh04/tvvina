package com.tvvina.tvvina.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recruit")
public class Recruit {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "address", length = 250)
    private String address;

    @Column(name = "basic_salary", length = 250)
    private String basicSalary;

    @Column(name = "allowance", length = 250)
    private String allowance;

    @Column(name = "image", length = 550)
    private String image;

    @Lob
    @Column(name = "note")
    private String note;

    @Column(name = "quantity_taken")
    private Integer quantityTaken;

    @Column(name = "is_delete")
    private Integer isDelete;
}