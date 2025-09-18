package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.repository.LaboratorioRepository;
import com.udb.miniproyectodwf.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaboratorioServiceImpl implements LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;
    private final UsuarioRepository usuarioRepository;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository,
                                  UsuarioRepository usuarioRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Laboratorio> getAllLaboratorios() {
        return laboratorioRepository.findAll();
    }

    @Override
    public Optional<Laboratorio> getLaboratorioById(Long id) {
        return laboratorioRepository.findById(id);
    }

    @Override
    public Laboratorio createLaboratorio(Laboratorio laboratorio) {
        // Si viene asociado un usuario, validar que exista
        if (laboratorio.getUsuario() != null) {
            Long usuarioId = laboratorio.getUsuario().getId();
            if (usuarioId == null || usuarioRepository.findById(usuarioId).isEmpty()) {
                throw new RuntimeException("Usuario asociado no existe");
            }
            laboratorio.setUsuario(usuarioRepository.findById(usuarioId).get());
        }
        return laboratorioRepository.save(laboratorio);
    }

    @Override
    public Laboratorio updateLaboratorio(Long id, Laboratorio laboratorio) {
        return laboratorioRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(laboratorio.getNombre());
                    existing.setDireccion(laboratorio.getDireccion());
                    existing.setTelefono(laboratorio.getTelefono());
                    if (laboratorio.getUsuario() != null && laboratorio.getUsuario().getId() != null) {
                        usuarioRepository.findById(laboratorio.getUsuario().getId())
                                .ifPresent(existing::setUsuario);
                    }
                    return laboratorioRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado con id " + id));
    }

    @Override
    public void deleteLaboratorio(Long id) {
        laboratorioRepository.deleteById(id);
    }
}
