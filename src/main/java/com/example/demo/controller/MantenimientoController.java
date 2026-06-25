package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Puesto;
import com.example.demo.model.Delegacion;
import com.example.demo.model.FestivoDelegacion; // Ajusta el nombre según tu modelo
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.PuestoRepository;
import com.example.demo.repository.DelegacionRepository;
import com.example.demo.repository.FestivoDelegacionRepository; // Ajusta si usas repositorio directo

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Mapea la raíz /api/ que espera tu Angular
@CrossOrigin(origins = "http://localhost:4200")
public class MantenimientoController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PuestoRepository puestoRepository;
    @Autowired private DelegacionRepository delegacionRepository;
    // Si no usas repositorios directos, inyecta tus Services aquí de la misma forma

    // --- ENDPOINTS PARA USUARIOS ---
    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll(); // Esto jala los datos reales de Neon
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


    
}