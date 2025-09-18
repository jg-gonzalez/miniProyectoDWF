package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Reporte;
import java.util.List;
import java.util.Optional;

public interface ReporteService {
    List<Reporte> getAllReportes();
    Optional<Reporte> getReporteById(Long id);
    Reporte createReporte(Reporte reporte);
    Reporte updateReporte(Long id, Reporte reporte);
    void deleteReporte(Long id);

    List<Reporte> getReportesByDepartamentoNombre(String nombreDepartamento);
    List<Reporte> getReportesByLaboratorioId(Long laboratorioId);
    List<Reporte> getReportesByEnfermedadId(Long enfermedadId);
}
