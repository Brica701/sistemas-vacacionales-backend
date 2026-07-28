package com.example.demo.controller;

import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.service.VacacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
// ⚠️ Se elimina @CrossOrigin. La configuración CORS global está en SecurityConfig.java
public class AdminVacacionesController {

    private final VacacionesService vacacionesService;

    public AdminVacacionesController(VacacionesService vacacionesService) {
        this.vacacionesService = vacacionesService;
    }

    // GET: http://localhost:8080/api/admin/pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudVacaciones>> listarPendientes() {
        List<SolicitudVacaciones> pendientes = vacacionesService.obtenerSolicitudesPendientes();
        return ResponseEntity.ok(pendientes);
    }

    // POST: http://localhost:8080/api/admin/resolver/{id}
    @PostMapping("/resolver/{id}")
    public ResponseEntity<?> resolverSolicitud(
            @PathVariable("id") Long solicitudId,
            @RequestBody Map<String, Object> payload) { // 👈 Cambiado a Object para mayor compatibilidad

        try {
            String accion = payload.get("accion") != null ? payload.get("accion").toString() : null;
            String motivoRechazo = payload.get("motivoRechazo") != null ? payload.get("motivoRechazo").toString() : null;

            SolicitudVacaciones solicitudProcesada = vacacionesService.responderSolicitud(solicitudId, accion, motivoRechazo);
            return ResponseEntity.ok(solicitudProcesada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}