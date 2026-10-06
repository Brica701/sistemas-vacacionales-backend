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
    public ResponseEntity<List<SolicitudVacaciones>> verPanelEmpleado(
            @RequestParam(value = "usuarioId", required = false) Long usuarioId) {

        // Si no se envía usuarioId por query param, usa el ID 2L de prueba/simulado
        Long targetUserId = (usuarioId != null) ? usuarioId : 2L;

        List<SolicitudVacaciones> historial = vacacionesService.obtenerHistorialUsuario(targetUserId);

        return ResponseEntity.ok(historial);
    }

    // POST: http://localhost:8080/api/empleado/solicitar
    @PostMapping("/solicitar")
    public ResponseEntity<?> tramitarSolicitud(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Obtener usuarioId dinámicamente desde el payload enviada por Angular
            Long usuarioId = null;

            if (payload.containsKey("usuarioId") && payload.get("usuarioId") != null) {
                usuarioId = Long.parseLong(payload.get("usuarioId").toString());
            } else if (payload.containsKey("usuario") && payload.get("usuario") instanceof Map) {
                // Compatibilidad en caso de que Angular envíe { usuario: { id: X } }
                Map<?, ?> userMap = (Map<?, ?>) payload.get("usuario");
                if (userMap.get("id") != null) {
                    usuarioId = Long.parseLong(userMap.get("id").toString());
                }
            }

            // Si no viene ningún ID, se usa el ID 2L como respaldo simulado
            if (usuarioId == null) {
                usuarioId = 2L;
            }

            // 2. Extraer y parsear las fechas
            String inicioStr = payload.get("fechaInicio").toString();
            String finStr = payload.get("fechaFin").toString();

            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            // 3. Crear solicitud en el servicio
            SolicitudVacaciones nuevaSolicitud = vacacionesService.crearSolicitud(usuarioId, inicio, fin);

            return ResponseEntity.ok(nuevaSolicitud);

        } catch (IllegalArgumentException e) {
            // Errores de validación de negocio (fechas no válidas, sin días disponibles, etc.)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Otros errores no previstos
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }
}