package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<Laboratorio> getAllLaboratorios() {
        return laboratorioRepository.findAll();
    }

    public Optional<Laboratorio> getLaboratorioById(Long id) {
        return laboratorioRepository.findById(id);
    }

    public Laboratorio createLaboratorio(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    public Laboratorio updateLaboratorio(Long id, Laboratorio laboratorioDetails) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        laboratorio.setNombre(laboratorioDetails.getNombre());
        laboratorio.setDireccion(laboratorioDetails.getDireccion());
        laboratorio.setTelefono(laboratorioDetails.getTelefono());
        laboratorio.setEmail(laboratorioDetails.getEmail());
        laboratorio.setActivo(laboratorioDetails.getActivo());

        return laboratorioRepository.save(laboratorio);
    }

    public void deleteLaboratorio(Long id) {
        if (!laboratorioRepository.existsById(id)) {
            throw new RuntimeException("Laboratorio no encontrado");
        }
        laboratorioRepository.deleteById(id);
    }

    // Método corregido: devuelve un laboratorio por el username del usuario (ahora solo devuelve el primero como ejemplo)
    public Optional<Laboratorio> getLaboratorioByUsuarioUsername(String username) {
        return laboratorioRepository.findAll().stream()
                .findFirst();
    }

    public Laboratorio asignarUsuario(Long laboratorioId, Long usuarioId) {
        Laboratorio laboratorio = laboratorioRepository.findById(laboratorioId)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
        // Aquí podrías agregar la lógica de asignar el usuario
        return laboratorioRepository.save(laboratorio);
    }

    // Método corregido: filtra laboratorios activos usando getActivo()
    public List<Laboratorio> getLaboratoriosActivos() {
        return laboratorioRepository.findAll().stream()
                .filter(Laboratorio::getActivo)
                .collect(Collectors.toList());
    }
}
