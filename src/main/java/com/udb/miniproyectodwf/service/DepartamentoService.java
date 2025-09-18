package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Departamento;
import java.util.List;
import java.util.Optional;

public interface DepartamentoService {
    List<Departamento> getAllDepartamentos();
    Optional<Departamento> getDepartamentoById(Long id);
    Optional<Departamento> getDepartamentoByNombre(String nombre);
    Departamento createDepartamento(Departamento departamento);
    Departamento updateDepartamento(Long id, Departamento departamento);
    void deleteDepartamento(Long id);
}
