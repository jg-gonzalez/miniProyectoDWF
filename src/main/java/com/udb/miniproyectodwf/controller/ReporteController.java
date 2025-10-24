package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Reporte;
import com.udb.miniproyectodwf.service.LaboratorioService;
import com.udb.miniproyectodwf.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public ResponseEntity<List<Reporte>> getAllReportes() {
        List<Reporte> reportes = reporteService.getAllReportes();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Long id) {
        return reporteService.getReporteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<Reporte>> getReportesPorDepartamento(@PathVariable String departamento) {
        List<Reporte> reportes = reporteService.getReportesPorDepartamento(departamento);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<Reporte>> buscarReportesConFiltros(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long enfermedadId,
            @RequestParam(required = false) Long laboratorioId,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {

        List<Reporte> reportes = reporteService.buscarReportesConFiltros(estado, enfermedadId, laboratorioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/laboratorio/{laboratorioId}")
    public ResponseEntity<List<Reporte>> getReportesPorLaboratorio(@PathVariable Long laboratorioId) {
        List<Reporte> reportes = reporteService.getReportesPorLaboratorio(laboratorioId);
        return ResponseEntity.ok(reportes);
    }

    @PostMapping
    public ResponseEntity<Reporte> createReporte(@RequestBody Reporte reporte) {
        Reporte created = reporteService.createReporte(reporte);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/laboratorio/{laboratorioId}/enfermedad/{enfermedadId}")
    public ResponseEntity<Reporte> createReporteConRelaciones(
            @RequestBody Reporte reporte,
            @PathVariable Long laboratorioId,
            @PathVariable Long enfermedadId) {

        Reporte created = reporteService.createReporte(reporte, laboratorioId, enfermedadId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Long id, @RequestBody Reporte reporte) {
        try {
            Reporte updated = reporteService.updateReporte(id, reporte);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/laboratorio/{laboratorioId}")
    public ResponseEntity<Reporte> updateReporteConLaboratorio(
            @PathVariable Long id,
            @RequestBody Reporte reporte,
            @PathVariable Long laboratorioId) {

        try {
            Reporte updated = reporteService.updateReporte(id, reporte, laboratorioId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
        try {
            reporteService.deleteReporte(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/laboratorio/{laboratorioId}")
    public ResponseEntity<Void> deleteReporteConLaboratorio(
            @PathVariable Long id,
            @PathVariable Long laboratorioId) {

        try {
            reporteService.deleteReporte(id, laboratorioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/generales")
    public ResponseEntity<?> getEstadisticasGenerales() {
        try {
            return ResponseEntity.ok(reporteService.getEstadisticasGenerales());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    @GetMapping("/estadisticas/departamentos")
    public ResponseEntity<?> getCasosPorDepartamento() {
        try {
            return ResponseEntity.ok(reporteService.getCasosPorDepartamento());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener casos por departamento: " + e.getMessage());
        }
    }

    @GetMapping("/estadisticas/enfermedades")
    public ResponseEntity<?> getCasosPorEnfermedad() {
        try {
            return ResponseEntity.ok(reporteService.getCasosPorEnfermedad());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener casos por enfermedad: " + e.getMessage());
        }
    }
}