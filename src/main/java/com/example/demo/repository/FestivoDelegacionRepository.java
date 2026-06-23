package com.example.demo.repository;

import com.example.demo.model.FestivoDelegacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FestivoDelegacionRepository extends JpaRepository<FestivoDelegacion, Long> {
    
    // Obtiene todos los festivos de una delegación específica
    List<FestivoDelegacion> findByDelegacionId(Long delegacionId);

    // Consulta nativa en Postgres para contar cuántos festivos caen en un rango de fechas para un usuario dado
    @Query(value = "SELECT COUNT(*) FROM festivos_delegacion fd " +
                   "JOIN usuarios u ON u.delegacion_id = fd.delegacion_id " +
                   "WHERE u.id = :usuarioId AND fd.fecha BETWEEN :inicio AND :fin", 
           nativeQuery = true)
    int countFestivosParaUsuario(@Param("usuarioId") Long usuarioId, 
                                @Param("inicio") LocalDate inicio, 
                                @Param("fin") LocalDate fin);
}