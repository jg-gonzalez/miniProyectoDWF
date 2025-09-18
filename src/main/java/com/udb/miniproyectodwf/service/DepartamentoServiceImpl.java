package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Departamento;
import com.udb.miniproyectodwf.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    @Override
    public Optional<Departamento> getDepartamentoByNombre(String nombre) {
        return departamentoRepository.findAll()
                .stream()
                .filter(d -> d.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public Departamento createDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento updateDepartamento(Long id, Departamento departamento) {
        return departamentoRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(departamento.getNombre());
                    return departamentoRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con id " + id));
    }

    @Override
    public void deleteDepartamento(Long id) {
        departamentoRepository.deleteById(id);
    }
}
