package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Laboratorio;
import java.util.List;
import java.util.Optional;

public interface LaboratorioService {
    List<Laboratorio> getAllLaboratorios();
    Optional<Laboratorio> getLaboratorioById(Long id);
    Laboratorio createLaboratorio(Laboratorio laboratorio);
    Laboratorio updateLaboratorio(Long id, Laboratorio laboratorio);
    void deleteLaboratorio(Long id);
}
