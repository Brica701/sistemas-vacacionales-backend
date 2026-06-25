package com.example.demo.service;

import com.example.demo.model.Delegacion;
import com.example.demo.model.Puesto;
import com.example.demo.model.FestivoDelegacion;
import java.time.LocalDate;
import java.util.List;

public interface AdministracionService {
    Delegacion crearDelegacion(String nombre, String provincia);
    Puesto crearPuesto(String nombre);
    FestivoDelegacion registrarFestivo(Long delegacionId, LocalDate fecha, String descripcion);
    List<Delegacion> listarDelegaciones();
    List<Puesto> listarPuestos();
}