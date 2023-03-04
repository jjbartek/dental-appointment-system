package com.example.dentalappointmentsystem.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_name")
    private String name;

    @Column(name = "min_price")
    private Integer minPrice;

    private Integer duration;
}
