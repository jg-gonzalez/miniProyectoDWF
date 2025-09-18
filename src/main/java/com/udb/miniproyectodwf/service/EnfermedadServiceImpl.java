package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.repository.EnfermedadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnfermedadServiceImpl implements EnfermedadService {

    private final EnfermedadRepository enfermedadRepository;

    public EnfermedadServiceImpl(EnfermedadRepository enfermedadRepository) {
        this.enfermedadRepository = enfermedadRepository;
    }

    @Override
    public List<Enfermedad> getAllEnfermedades() {
        return enfermedadRepository.findAll();
    }

    @Override
    public Optional<Enfermedad> getEnfermedadById(Long id) {
        return enfermedadRepository.findById(id);
    }

    @Override
    public Enfermedad createEnfermedad(Enfermedad enfermedad) {
        // Podés validar duplicados por nombre si querés
        return enfermedadRepository.save(enfermedad);
    }

    @Override
    public Enfermedad updateEnfermedad(Long id, Enfermedad enfermedad) {
        return enfermedadRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(enfermedad.getNombre());
                    existing.setDescripcion(enfermedad.getDescripcion());
                    return enfermedadRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada con id " + id));
    }

    @Override
    public void deleteEnfermedad(Long id) {
        enfermedadRepository.deleteById(id);
    }
}
