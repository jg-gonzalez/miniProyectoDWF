package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Reporte;
import com.udb.miniproyectodwf.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> getAllReportes() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> getReporteById(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte createReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public Reporte createReporte(Reporte reporte, Long laboratorioId, Long enfermedadId) {
        // Aquí deberías buscar el laboratorio y enfermedad por ID y asignarlos
        // Por ahora simplemente guardamos el reporte
        return reporteRepository.save(reporte);
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
        return reporteRepository.findAll().stream()
                .filter(r -> departamento.equals(r.getDepartamento().getNombre()))
                .collect(Collectors.toList());
    }

    public List<Reporte> getReportesPorLaboratorio(Long laboratorioId) {
        return reporteRepository.findAll().stream()
                .filter(r -> laboratorioId.equals(r.getLaboratorio().getId()))
                .collect(Collectors.toList());
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
                        r -> r.getDepartamento().getNombre(),  // CORREGIDO: usar el nombre del departamento
                        Collectors.summingInt(Reporte::getCantidadCasos)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())  // CORREGIDO: usar String
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
                    alerta.put("departamento", r.getDepartamento().getNombre());  // CORREGIDO: usar nombre
                    alerta.put("municipio", r.getMunicipio().getNombre());       // CORREGIDO: usar nombre
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
                        r -> r.getDepartamento().getNombre(),  // CORREGIDO: usar nombre del departamento
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