package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/festivos")
@CrossOrigin(origins = "*")
public class FestivoController {

    // Método GET para obtener los festivos
    @GetMapping
    public List<Object> obtenerFestivos() {

        return List.of();
    }
}