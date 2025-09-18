package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Enfermedad;
import java.util.List;
import java.util.Optional;

public interface EnfermedadService {
    List<Enfermedad> getAllEnfermedades();
    Optional<Enfermedad> getEnfermedadById(Long id);
    Enfermedad createEnfermedad(Enfermedad enfermedad);
    Enfermedad updateEnfermedad(Long id, Enfermedad enfermedad);
    void deleteEnfermedad(Long id);
}
