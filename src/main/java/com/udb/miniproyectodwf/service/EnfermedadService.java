package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.repository.EnfermedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnfermedadService {

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    public List<Enfermedad> getAllEnfermedades() {
        return enfermedadRepository.findAll();
    }

    public List<Enfermedad> getEnfermedadesActivas() {
        return enfermedadRepository.findByActivaTrue();
    }

    public Optional<Enfermedad> getEnfermedadById(Long id) {
        return enfermedadRepository.findById(id);
    }

    public Enfermedad createEnfermedad(Enfermedad enfermedad) {
        if (enfermedadRepository.existsByNombre(enfermedad.getNombre())) {
            throw new RuntimeException("Ya existe una enfermedad con el nombre: " + enfermedad.getNombre());
        }

        if (enfermedadRepository.existsByCodigo(enfermedad.getCodigo())) {
            throw new RuntimeException("Ya existe una enfermedad con el código: " + enfermedad.getCodigo());
        }

        enfermedad.setFechaCreacion(LocalDateTime.now());
        enfermedad.setActiva(true);

        return enfermedadRepository.save(enfermedad);
    }

    public Enfermedad updateEnfermedad(Long id, Enfermedad enfermedad) {
        Enfermedad existente = enfermedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada con ID: " + id));

        if (!existente.getNombre().equals(enfermedad.getNombre()) &&
                enfermedadRepository.existsByNombre(enfermedad.getNombre())) {
            throw new RuntimeException("Ya existe una enfermedad con el nombre: " + enfermedad.getNombre());
        }

        if (!existente.getCodigo().equals(enfermedad.getCodigo()) &&
                enfermedadRepository.existsByCodigo(enfermedad.getCodigo())) {
            throw new RuntimeException("Ya existe una enfermedad con el código: " + enfermedad.getCodigo());
        }

        existente.setNombre(enfermedad.getNombre());
        existente.setCodigo(enfermedad.getCodigo());
        existente.setDescripcion(enfermedad.getDescripcion());
        existente.setSintomas(enfermedad.getSintomas());
        existente.setTratamiento(enfermedad.getTratamiento());

        return enfermedadRepository.save(existente);
    }

    public void deleteEnfermedad(Long id) {
        if (!enfermedadRepository.existsById(id)) {
            throw new RuntimeException("Enfermedad no encontrada con ID: " + id);
        }
        enfermedadRepository.deleteById(id);
    }

    public Enfermedad desactivarEnfermedad(Long id) {
        Enfermedad enfermedad = enfermedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada con ID: " + id));

        enfermedad.setActiva(false);
        return enfermedadRepository.save(enfermedad);
    }

    // ✅ Nuevo método para el Dashboard
    public List<Enfermedad> getTopEnfermedades(int limite) {
        List<Enfermedad> lista = enfermedadRepository.findTopEnfermedades();
        return lista.stream().limit(limite).toList();
    }
}
