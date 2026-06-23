package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "delegaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delegacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "provincia_estado", nullable = false, length = 100)
    private String provinciaEstado;
}