package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.*;
import com.udb.miniproyectodwf.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<Reporte> getAllReportes() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> getReporteById(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte createReporte(Reporte reporte) {
        // Validar y cargar todas las entidades relacionadas
        if (reporte.getDepartamento() != null && reporte.getDepartamento().getId() != null) {
            Departamento departamento = departamentoRepository.findById(reporte.getDepartamento().getId())
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
            reporte.setDepartamento(departamento);
        }

        if (reporte.getMunicipio() != null && reporte.getMunicipio().getId() != null) {
            Municipio municipio = municipioRepository.findById(reporte.getMunicipio().getId())
                    .orElseThrow(() -> new RuntimeException("Municipio no encontrado"));
            reporte.setMunicipio(municipio);
        }

        if (reporte.getEnfermedad() != null && reporte.getEnfermedad().getId() != null) {
            Enfermedad enfermedad = enfermedadRepository.findById(reporte.getEnfermedad().getId())
                    .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada"));
            reporte.setEnfermedad(enfermedad);
        }

        if (reporte.getLaboratorio() != null && reporte.getLaboratorio().getId() != null) {
            Laboratorio laboratorio = laboratorioRepository.findById(reporte.getLaboratorio().getId())
                    .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));
            reporte.setLaboratorio(laboratorio);
        }

        return reporteRepository.save(reporte);
    }

    // Método para crear reporte con nombres en lugar de IDs
    public Reporte crearReporteConNombres(Long enfermedadId, Long laboratorioId,
                                          String departamentoNombre, String municipioNombre,
                                          Integer cantidadCasos, LocalDate fechaDeteccion,
                                          String observaciones) {

        // Buscar o crear departamento
        Departamento departamento = departamentoRepository.findByNombre(departamentoNombre)
                .orElseGet(() -> {
                    Departamento nuevoDepto = new Departamento(departamentoNombre);
                    return departamentoRepository.save(nuevoDepto);
                });

        // Buscar o crear municipio
        Municipio municipio = municipioRepository.findByNombreAndDepartamentoNombre(municipioNombre, departamentoNombre)
                .orElseGet(() -> {
                    Municipio nuevoMun = new Municipio(municipioNombre, departamento);
                    return municipioRepository.save(nuevoMun);
                });

        // Obtener enfermedad y laboratorio
        Enfermedad enfermedad = enfermedadRepository.findById(enfermedadId)
                .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada"));

        Laboratorio laboratorio = laboratorioRepository.findById(laboratorioId)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        // Crear y guardar reporte
        Reporte reporte = Reporte.builder()
                .enfermedad(enfermedad)
                .laboratorio(laboratorio)
                .departamento(departamento)
                .municipio(municipio)
                .cantidadCasos(cantidadCasos)
                .fechaDeteccion(fechaDeteccion)
                .observaciones(observaciones)
                .estado("PENDIENTE")
                .build();

        return reporteRepository.save(reporte);
    }

    public Reporte createReporte(Reporte reporte, Long laboratorioId, Long enfermedadId) {
        // Implementación existente
        return createReporte(reporte);
    }

    public Reporte updateReporte(Long id, Reporte reporteDetails) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setLaboratorio(reporteDetails.getLaboratorio());
        reporte.setEnfermedad(reporteDetails.getEnfermedad());
        reporte.setDepartamento(reporteDetails.getDepartamento());
        reporte.setMunicipio(reporteDetails.getMunicipio());
        reporte.setCantidadCasos(reporteDetails.getCantidadCasos());
        reporte.setFechaDeteccion(reporteDetails.getFechaDeteccion());
        reporte.setObservaciones(reporteDetails.getObservaciones());
        reporte.setEstado(reporteDetails.getEstado());

        return reporteRepository.save(reporte);
    }

    public Reporte updateReporte(Long id, Reporte reporteDetails, Long laboratorioId) {
        return updateReporte(id, reporteDetails);
    }

    public void deleteReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }
        reporteRepository.deleteById(id);
    }

    public void deleteReporte(Long id, Long laboratorioId) {
        deleteReporte(id);
    }

    public List<Reporte> getReportesPorDepartamento(String departamento) {
        return reporteRepository.findByDepartamentoNombre(departamento);
    }

    public List<Reporte> getReportesPorLaboratorio(Long laboratorioId) {
        return reporteRepository.findByLaboratorioId(laboratorioId);
    }

    public Map<String, Object> getEstadisticasAvanzadas() {
        List<Reporte> reportes = reporteRepository.findAll();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalReportes", reportes.size());
        estadisticas.put("totalCasos",
                reportes.stream().mapToInt(Reporte::getCantidadCasos).sum());
        estadisticas.put("reportesPendientes",
                reportes.stream().filter(r -> "PENDIENTE".equals(r.getEstado())).count());
        estadisticas.put("reportesVerificados",
                reportes.stream().filter(r -> "VERIFICADO".equals(r.getEstado())).count());

        return estadisticas;
    }

    public List<Map<String, Object>> getTopEnfermedades(int limit) {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEnfermedad().getNombre(),
                        Collectors.summingInt(Reporte::getCantidadCasos)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("enfermedad", entry.getKey());
                    result.put("totalCasos", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTopDepartamentos(int limit) {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getDepartamento().getNombre(),
                        Collectors.summingInt(Reporte::getCantidadCasos)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("departamento", entry.getKey());
                    result.put("totalCasos", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getReportesPorMes(int year) {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .filter(r -> r.getFechaReporte().getYear() == year)
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getFechaReporte()).toString(),
                        Collectors.summingInt(r -> 1)
                ));
    }

    public List<Map<String, Object>> getAlertas() {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .filter(r -> r.getCantidadCasos() > 50)
                .map(r -> {
                    Map<String, Object> alerta = new HashMap<>();
                    alerta.put("id", r.getId());
                    alerta.put("enfermedad", r.getEnfermedad().getNombre());
                    alerta.put("departamento", r.getDepartamento().getNombre());
                    alerta.put("municipio", r.getMunicipio().getNombre());
                    alerta.put("casos", r.getCantidadCasos());
                    alerta.put("fecha", r.getFechaDeteccion());
                    alerta.put("nivel", r.getCantidadCasos() > 100 ? "ALTO" : "MEDIO");
                    return alerta;
                })
                .collect(Collectors.toList());
    }

    public List<Reporte> buscarReportesConFiltros(String estado, Long enfermedadId, Long laboratorioId,
                                                  LocalDate fechaInicio, LocalDate fechaFin) {
        return reporteRepository.findAll().stream()
                .filter(r -> estado == null || estado.equals(r.getEstado()))
                .filter(r -> enfermedadId == null || enfermedadId.equals(r.getEnfermedad().getId()))
                .filter(r -> laboratorioId == null || laboratorioId.equals(r.getLaboratorio().getId()))
                .filter(r -> fechaInicio == null || !r.getFechaDeteccion().isBefore(fechaInicio))
                .filter(r -> fechaFin == null || !r.getFechaDeteccion().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getEstadisticasGenerales() {
        List<Reporte> reportes = reporteRepository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReportes", reportes.size());
        stats.put("totalCasos", reportes.stream().mapToInt(Reporte::getCantidadCasos).sum());
        stats.put("promedioCasosPorReporte", reportes.isEmpty() ? 0 :
                reportes.stream().mapToInt(Reporte::getCantidadCasos).average().orElse(0.0));
        return stats;
    }

    public Map<String, Integer> getCasosPorDepartamento() {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getDepartamento().getNombre(),
                        Collectors.summingInt(Reporte::getCantidadCasos)
                ));
    }

    public Map<String, Integer> getCasosPorEnfermedad() {
        List<Reporte> reportes = reporteRepository.findAll();

        return reportes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEnfermedad().getNombre(),
                        Collectors.summingInt(Reporte::getCantidadCasos)
                ));
    }
}