package com.example.demo.repository;

import com.example.demo.model.Delegacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegacionRepository extends JpaRepository<Delegacion, Long> {
    // Ya incluye buscar por ID, listar todos, guardar y borrar.
}   