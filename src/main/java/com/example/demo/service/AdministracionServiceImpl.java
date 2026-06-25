package com.example.demo.service;

import com.example.demo.model.Delegacion;
import com.example.demo.model.Puesto;
import com.example.demo.model.FestivoDelegacion;
import com.example.demo.repository.DelegacionRepository;
import com.example.demo.repository.PuestoRepository;
import com.example.demo.repository.FestivoDelegacionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AdministracionServiceImpl implements AdministracionService {

    private final DelegacionRepository delegacionRepository;
    private final PuestoRepository puestoRepository;
    private final FestivoDelegacionRepository festivoRepository;

    public AdministracionServiceImpl(DelegacionRepository delegacionRepository, 
                                     PuestoRepository puestoRepository, 
                                     FestivoDelegacionRepository festivoRepository) {
        this.delegacionRepository = delegacionRepository;
        this.puestoRepository = puestoRepository;
        this.festivoRepository = festivoRepository;
    }

    @Override
    public Delegacion crearDelegacion(String nombre, String provincia) {
        Delegacion d = new Delegacion();
        d.setNombre(nombre);
        d.setProvinciaEstado(provincia);
        return delegacionRepository.save(d);
    }

    @Override
    public Puesto crearPuesto(String nombre) {
        Puesto p = new Puesto();
        p.setNombre(nombre);
        return puestoRepository.save(p);
    }

    @Override
    public FestivoDelegacion registrarFestivo(Long delegacionId, LocalDate fecha, String descripcion) {
        Delegacion d = delegacionRepository.findById(delegacionId)
                .orElseThrow(() -> new RuntimeException("Delegación no encontrada"));
        
        FestivoDelegacion f = new FestivoDelegacion();
        f.setDelegacion(d);
        f.setFecha(fecha);
        f.setDescripcion(descripcion);
        return festivoRepository.save(f);
    }

    @Override
    public List<Delegacion> listarDelegaciones() { return delegacionRepository.findAll(); }

    @Override
    public List<Puesto> listarPuestos() { return puestoRepository.findAll(); }
}