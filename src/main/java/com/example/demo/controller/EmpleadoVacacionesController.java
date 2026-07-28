package com.example.demo.controller;

import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.service.VacacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleado")
// ⚠️ Quitamos @CrossOrigin de aquí. La seguridad y los permisos de CORS
// se gestionan centralizadamente en SecurityConfig.java
public class EmpleadoVacacionesController {

    private final VacacionesService vacacionesService;

    public EmpleadoVacacionesController(VacacionesService vacacionesService) {
        this.vacacionesService = vacacionesService;
    }

    // GET: http://localhost:8080/api/empleado/solicitudes
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudVacaciones>> verPanelEmpleado() {
        Long usuarioIdSimulado = 2L;

        List<SolicitudVacaciones> historial = vacacionesService.obtenerHistorialUsuario(usuarioIdSimulado);

        return ResponseEntity.ok(historial);
    }

    // POST: http://localhost:8080/api/empleado/solicitar
    @PostMapping("/solicitar")
    public ResponseEntity<?> tramitarSolicitud(@RequestBody Map<String, Object> payload) {
        // 👆 Cambiado a Map<String, Object> para aceptar JSONs anidados de Angular
        Long usuarioIdSimulado = 2L;

        try {
            String inicioStr = payload.get("fechaInicio").toString();
            String finStr = payload.get("fechaFin").toString();

            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            SolicitudVacaciones nuevaSolicitud = vacacionesService.crearSolicitud(usuarioIdSimulado, inicio, fin);
            return ResponseEntity.ok(nuevaSolicitud);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}