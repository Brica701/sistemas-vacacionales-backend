package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Activar configuración CORS explícita
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2. Desactivar CSRF para endpoints de tipo REST API
            .csrf(csrf -> csrf.disable())
            
            // 3. Abrir la compuerta pública para la API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // 👈 Autoriza todo lo que empiece por /api/
                .anyRequest().authenticated()
            )
            
            // Desactivamos el formLogin web para evitar que devuelva HTML en lugar de errores JSON claros (401/403/404)
            .formLogin(form -> form.disable()); 

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Agregamos tanto el puerto de desarrollo de Angular como los posibles mapeos internos de SSR
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}