package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "festivos_delegacion", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"delegacion_id", "fecha"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FestivoDelegacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "delegacion_id", nullable = false)
    private Delegacion delegacion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 150)
    private String descripcion;
}