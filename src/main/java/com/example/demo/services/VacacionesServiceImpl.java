package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.SolicitudVacaciones;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.SolicitudVacacionesRepository;
import com.example.demo.repository.FestivoDelegacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class VacacionesServiceImpl implements VacacionesService {

    private final SolicitudVacacionesRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final FestivoDelegacionRepository festivoRepository;

    // Inyección por constructor (Práctica recomendada en Spring)
    public VacacionesServiceImpl(SolicitudVacacionesRepository solicitudRepository,
                                 UsuarioRepository usuarioRepository,
                                 FestivoDelegacionRepository festivoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.festivoRepository = festivoRepository;
    }

    @Override
    @Transactional
    public SolicitudVacaciones crearSolicitud(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }

        // 1. Calcular cuántos días útiles consume la solicitud
        int diasUtiles = calcularDiasLaborablesReales(usuarioId, fechaInicio, fechaFin);
        if (diasUtiles == 0) {
            throw new IllegalArgumentException("El rango seleccionado no contiene días laborables");
        }

        // 2. Verificar si el usuario tiene días suficientes disponibles
        // Nota: En una app real restarías los días ya consumidos y aprobados.
        if (diasUtiles > usuario.getDiasTotalesAnuales()) {
            throw new IllegalArgumentException("No tienes suficientes días disponibles. Solicitas " 
                    + diasUtiles + " pero te quedan " + usuario.getDiasTotalesAnuales());
        }

        // 3. Crear y guardar la solicitud en estado 'Pendiente'
        SolicitudVacaciones solicitud = new SolicitudVacaciones();
        solicitud.setUsuario(usuario);
        solicitud.setFechaInicio(fechaInicio);
        solicitud.setFechaFin(fechaFin);
        solicitud.setDiasSolicitados(diasUtiles);
        solicitud.setEstado("Pendiente");

        return solicitudRepository.save(solicitud);
    }

    @Override
    @Transactional
    public SolicitudVacaciones responderSolicitud(Long solicitudId, String nuevoEstado, String motivoRechazo) {
        SolicitudVacaciones solicitud = solicitudRepository.findById(solicitidId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getEstado().equals("Pendiente")) {
            throw new IllegalStateException("Esta solicitud ya ha sido procesada previamente");
        }

        if (!nuevoEstado.equals("Aprobado") && !nuevoEstado.equals("Rechazado")) {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'Aprobado' o 'Rechazado'");
        }

        if (nuevoEstado.equals("Aprobado")) {
            Usuario usuario = solicitud.getUsuario();
            
            // 4. ALERTA DE SOLAPAMIENTO: Contar compañeros del mismo puesto y delegación con vacaciones aprobadas
            int solapamientos = solicitudRepository.countSolapamientos(
                    usuario.getDelegacion().getId(),
                    usuario.getPuesto().getId(),
                    usuario.getId(),
                    solicitud.getFechaInicio(),
                    solicitud.getFechaFin()
            );

            // Podrías lanzar excepción o simplemente dejar que el admin decida, aquí lanzamos aviso preventivo si quieres bloquearlo
            if (solapamientos > 0) {
                // Opcional: lanzar error o guardar un flag. Dejaremos que pase pero restando el saldo del usuario.
            }

            // Restar los días del saldo anual del usuario
            usuario.setDiasTotalesAnuales(usuario.getDiasTotalesAnuales() - solicitud.getDiasSolicitados());
            usuarioRepository.save(usuario);
        } else {
            solicitud.setMotivoRechazo(motivoRechazo);
        }

        solicitud.setEstado(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }

    @Override
    public List<SolicitudVacaciones> obtenerHistorialUsuario(Long usuarioId) {
        return solicitudRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<SolicitudVacaciones> obtenerSolicitudesPendientes() {
        return solicitudRepository.findByEstado("Pendiente");
    }

    @Override
    public int calcularDiasLaborablesReales(Long usuarioId, LocalDate inicio, LocalDate fin) {
        int diasLaborables = 0;
        LocalDate fechaActual = inicio;

        // Recorrer el rango día por día en Java para quitar los fines de semana
        while (!fechaActual.isAfter(fin)) {
            DayOfWeek diaSemana = fechaActual.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                diasLaborables++;
            }
            fechaActual = fechaActual.plusDays(1);
        }

        // Restar los festivos registrados en Neon para la delegación de este usuario
        int festivosEnMedio = festivoRepository.countFestivosParaUsuario(usuarioId, inicio, fin);
        
        return Math.max(0, diasLaborables - festivosEnMedio);
    }
}