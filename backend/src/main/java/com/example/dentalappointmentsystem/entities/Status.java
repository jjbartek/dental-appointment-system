package com.example.dentalappointmentsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "statuses")
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status_name")
    private String name;
}
