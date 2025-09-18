package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Departamento;
import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.entity.Reporte;
import com.udb.miniproyectodwf.repository.DepartamentoRepository;
import com.udb.miniproyectodwf.repository.EnfermedadRepository;
import com.udb.miniproyectodwf.repository.LaboratorioRepository;
import com.udb.miniproyectodwf.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final EnfermedadRepository enfermedadRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final DepartamentoRepository departamentoRepository;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                              EnfermedadRepository enfermedadRepository,
                              LaboratorioRepository laboratorioRepository,
                              DepartamentoRepository departamentoRepository) {
        this.reporteRepository = reporteRepository;
        this.enfermedadRepository = enfermedadRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<Reporte> getAllReportes() {
        return reporteRepository.findAll();
    }

    @Override
    public Optional<Reporte> getReporteById(Long id) {
        return reporteRepository.findById(id);
    }

    @Override
    public Reporte createReporte(Reporte reporte) {
        // Validar que enfermedades/laboratorio/departamento existan
        if (reporte.getEnfermedad() == null || reporte.getEnfermedad().getId() == null) {
            throw new RuntimeException("Enfermedad inválida en el reporte");
        }
        if (reporte.getLaboratorio() == null || reporte.getLaboratorio().getId() == null) {
            throw new RuntimeException("Laboratorio inválido en el reporte");
        }
        if (reporte.getDepartamento() == null || reporte.getDepartamento().getId() == null) {
            throw new RuntimeException("Departamento inválido en el reporte");
        }

        Enfermedad enf = enfermedadRepository.findById(reporte.getEnfermedad().getId())
                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada"));
        Laboratorio lab = laboratorioRepository.findById(reporte.getLaboratorio().getId())
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
        Departamento dep = departamentoRepository.findById(reporte.getDepartamento().getId())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        reporte.setEnfermedad(enf);
        reporte.setLaboratorio(lab);
        reporte.setDepartamento(dep);

        return reporteRepository.save(reporte);
    }

    @Override
    public Reporte updateReporte(Long id, Reporte reporte) {
        return reporteRepository.findById(id)
                .map(existing -> {
                    if (reporte.getEnfermedad() != null && reporte.getEnfermedad().getId() != null) {
                        Enfermedad enf = enfermedadRepository.findById(reporte.getEnfermedad().getId())
                                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada"));
                        existing.setEnfermedad(enf);
                    }
                    if (reporte.getLaboratorio() != null && reporte.getLaboratorio().getId() != null) {
                        Laboratorio lab = laboratorioRepository.findById(reporte.getLaboratorio().getId())
                                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
                        existing.setLaboratorio(lab);
                    }
                    if (reporte.getDepartamento() != null && reporte.getDepartamento().getId() != null) {
                        Departamento dep = departamentoRepository.findById(reporte.getDepartamento().getId())
                                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
                        existing.setDepartamento(dep);
                    }
                    existing.setFecha(reporte.getFecha());
                    existing.setCasos(reporte.getCasos());
                    existing.setActivo(reporte.isActivo());
                    return reporteRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id " + id));
    }

    @Override
    public void deleteReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public List<Reporte> getReportesByDepartamentoNombre(String nombreDepartamento) {
        return reporteRepository.findAll()
                .stream()
                .filter(r -> r.getDepartamento() != null &&
                        nombreDepartamento.equalsIgnoreCase(r.getDepartamento().getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reporte> getReportesByLaboratorioId(Long laboratorioId) {
        return reporteRepository.findAll()
                .stream()
                .filter(r -> r.getLaboratorio() != null &&
                        r.getLaboratorio().getId().equals(laboratorioId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reporte> getReportesByEnfermedadId(Long enfermedadId) {
        return reporteRepository.findAll()
                .stream()
                .filter(r -> r.getEnfermedad() != null &&
                        r.getEnfermedad().getId().equals(enfermedadId))
                .collect(Collectors.toList());
    }
}
