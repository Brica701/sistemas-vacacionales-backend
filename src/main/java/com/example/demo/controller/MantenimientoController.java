package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Puesto;
import com.example.demo.model.Delegacion;
import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.PuestoRepository;
import com.example.demo.repository.DelegacionRepository;
import com.example.demo.repository.SolicitudVacacionesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
// ⚠️ Se elimina @CrossOrigin para evitar conflictos con SecurityConfig.java
public class MantenimientoController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PuestoRepository puestoRepository;
    @Autowired private DelegacionRepository delegacionRepository;
    @Autowired private SolicitudVacacionesRepository solicitudVacacionesRepository;

    // --- ENDPOINTS PARA USUARIOS ---
    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/usuarios")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- ENDPOINTS PARA PUESTOS ---
    @GetMapping("/puestos")
    public List<Puesto> obtenerPuestos() {
        return puestoRepository.findAll();
    }

    @PostMapping("/puestos")
    public Puesto guardarPuesto(@RequestBody Puesto puesto) {
        return puestoRepository.save(puesto);
    }

    @DeleteMapping("/puestos/{id}")
    public ResponseEntity<?> eliminarPuesto(@PathVariable Long id) {
        puestoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- ENDPOINTS PARA DELEGACIONES ---
    @GetMapping("/delegaciones")
    public List<Delegacion> obtenerDelegaciones() {
        return delegacionRepository.findAll();
    }

    @PostMapping("/delegaciones")
    public Delegacion guardarDelegacion(@RequestBody Delegacion delegacion) {
        return delegacionRepository.save(delegacion);
    }

    @DeleteMapping("/delegaciones/{id}")
    public ResponseEntity<?> eliminarDelegacion(@PathVariable Long id) {
        delegacionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- AUTENTICACIÓN ---
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return usuarioRepository.findByEmail(email)
                .map(usuario -> {
                    if (usuario.getPasswordHash() != null && usuario.getPasswordHash().equals(password)) {
                        return ResponseEntity.ok(usuario);
                    } else {
                        return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "El correo no existe en la base de datos")));
    }

    // --- VACACIONES GENERALES ---
    @GetMapping("/vacaciones")
    public List<SolicitudVacaciones> obtenerTodasLasVacaciones() {
        return solicitudVacacionesRepository.findAll();
    }
}