package com.example.demo.controller;

import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.service.VacacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController // 1. Cambiado de @Controller a @RestController
@RequestMapping("/api/empleado") // 2. Buenas prácticas: Añadir /api/ para las rutas REST
@CrossOrigin(origins = "http://localhost:4200") // 3. Permite que tu Angular local se conecte sin errores de CORS
public class EmpleadoVacacionesController {

    private final VacacionesService vacacionesService;

    public EmpleadoVacacionesController(VacacionesService vacacionesService) {
        this.vacacionesService = vacacionesService;
    }

    // GET: http://localhost:8080/api/empleado/solicitudes
    @GetMapping("/solicitudes") // 4. Cambiado el endpoint para que sea más semántico
    public ResponseEntity<List<SolicitudVacaciones>> verPanelEmpleado() {
        Long usuarioIdSimulado = 2L; 
        
        List<SolicitudVacaciones> historial = vacacionesService.obtenerHistorialUsuario(usuarioIdSimulado);
        
        return ResponseEntity.ok(historial); // 5. Devolvemos los datos directamente en JSON
    }

    // POST: http://localhost:8080/api/empleado/solicitar
    @PostMapping("/solicitar")
    public ResponseEntity<?> tramitarSolicitud(@RequestBody Map<String, String> payload) { // 6. Recibe un JSON de Angular
        Long usuarioIdSimulado = 2L;

        try {
            LocalDate inicio = LocalDate.parse(payload.get("fechaInicio"));
            LocalDate fin = LocalDate.parse(payload.get("fechaFin"));
            
            SolicitudVacaciones nuevaSolicitud = vacacionesService.crearSolicitud(usuarioIdSimulado, inicio, fin);
            return ResponseEntity.ok(nuevaSolicitud); // Devuelve la solicitud creada con éxito
            
        } catch (Exception e) {
            // Si hay error (ej: no te quedan días), enviamos el mensaje en un JSON limpio con un código 400 (Bad Request)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}