package com.example.demo.service;

import com.example.demo.model.SolicitudVacaciones;
import java.time.LocalDate;
import java.util.List;

public interface VacacionesService {
    SolicitudVacaciones crearSolicitud(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);
    SolicitudVacaciones responderSolicitud(Long solicitudId, String nuevoEstado, String motivoRechazo);
    List<SolicitudVacaciones> obtenerHistorialUsuario(Long usuarioId);
    List<SolicitudVacaciones> obtenerSolicitudesPendientes();
    int calcularDiasLaborablesReales(Long usuarioId, LocalDate inicio, LocalDate fin);
}