package com.example.demo.controller;

import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.service.VacacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // 1. Convertido a controlador REST para Angular
@RequestMapping("/api/admin") // 2. Ruta unificada bajo /api/admin
@CrossOrigin(origins = "http://localhost:4200") // 3. Habilita la conexión desde tu frontend Angular
public class AdminVacacionesController {

    private final VacacionesService vacacionesService;

    public AdminVacacionesController(VacacionesService vacacionesService) {
        this.vacacionesService = vacacionesService;
    }

    // GET: http://localhost:8080/api/admin/pendientes
    // Devuelve la lista de solicitudes en formato JSON
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudVacaciones>> listarPendientes() {
        List<SolicitudVacaciones> pendientes = vacacionesService.obtenerSolicitudesPendientes();
        return ResponseEntity.ok(pendientes);
    }

    // POST: http://localhost:8080/api/admin/resolver/5
    // Recibe el ID en la URL y un JSON con la acción y el motivo en el cuerpo
    @PostMapping("/resolver/{id}")
    public ResponseEntity<?> resolverSolicitud(
            @PathVariable("id") Long solicitudId,
            @RequestBody Map<String, String> payload) { // 4. Recibe JSON de Angular con 'accion' y 'motivoRechazo'
        
        try {
            String accion = payload.get("accion"); // Espera 'Aprobado' o 'Rechazado'
            String motivoRechazo = payload.get("motivoRechazo");

            SolicitudVacaciones solicitudProcesada = vacacionesService.responderSolicitud(solicitudId, accion, motivoRechazo);
            return ResponseEntity.ok(solicitudProcesada); // Devuelve la solicitud actualizada
            
        } catch (Exception e) {
            // Si la solicitud no existe o ya estaba procesada, devolvemos el error en un JSON limpio
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}