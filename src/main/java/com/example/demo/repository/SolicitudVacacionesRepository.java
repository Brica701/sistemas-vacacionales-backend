package com.example.demo.repository;

import com.example.demo.model.SolicitudVacaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitudVacacionesRepository extends JpaRepository<SolicitudVacaciones, Long> {
    
    // Historial de solicitudes de un empleado concreto
    List<SolicitudVacaciones> findByUsuarioId(Long usuarioId);

    // Muestra al Admin las solicitudes pendientes de gestionar
    List<SolicitudVacaciones> findByEstado(String estado);

    // 🔥 LA MAGIA DE POSTGRES: Busca si hay solapamientos en la misma delegación y puesto usando OVERLAPS
    @Query(value = "SELECT COUNT(*) FROM solicitudes_vacaciones sv " +
                   "JOIN usuarios u ON sv.usuario_id = u.id " +
                   "WHERE u.delegacion_id = :delegacionId " +
                   "AND u.puesto_id = :puestoId " +
                   "AND sv.estado = 'Aprobado' " +
                   "AND u.id <> :usuarioId " +
                   "AND (sv.fecha_inicio, sv.fecha_fin) OVERLAPS (:inicio, :fin)", 
           nativeQuery = true)
    int countSolapamientos(@Param("delegacionId") Long delegacionId,
                           @Param("puestoId") Long puestoId,
                           @Param("usuarioId") Long usuarioId,
                           @Param("inicio") LocalDate inicio,
                           @Param("fin") LocalDate fin);
}