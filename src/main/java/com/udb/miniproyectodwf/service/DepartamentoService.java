package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Departamento;
import com.udb.miniproyectodwf.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    public List<Departamento> getDepartamentosActivos() {
        return departamentoRepository.findByActivoTrue();
    }

    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Optional<Departamento> getDepartamentoByNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    public Departamento createDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    public Departamento createDepartamento(String nombre) {
        Departamento departamento = new Departamento();
        departamento.setNombre(nombre);
        departamento.setActivo(true);
        return departamentoRepository.save(departamento);
    }

    public Departamento updateDepartamento(Long id, Departamento departamentoDetails) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        departamento.setNombre(departamentoDetails.getNombre());
        departamento.setActivo(departamentoDetails.getActivo());

        return departamentoRepository.save(departamento);
    }

    public void deleteDepartamento(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento no encontrado");
        }
        departamentoRepository.deleteById(id);
    }

    public void desactivarDepartamento(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        departamento.setActivo(false);
        departamentoRepository.save(departamento);
    }

    public boolean existeDepartamento(String nombre) {
        return departamentoRepository.existsByNombre(nombre);
    }
}