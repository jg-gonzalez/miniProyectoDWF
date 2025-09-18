package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Reporte;
import com.udb.miniproyectodwf.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> getAllReportes() {
        return ResponseEntity.ok(reporteService.getAllReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Long id) {
        return reporteService.getReporteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reporte> createReporte(@Valid @RequestBody Reporte reporte) {
        Reporte created = reporteService.createReporte(reporte);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Long id,
                                                 @Valid @RequestBody Reporte reporte) {
        Reporte updated = reporteService.updateReporte(id, reporte);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
        reporteService.deleteReporte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departamento/{nombre}")
    public ResponseEntity<List<Reporte>> getReportesByDepartamento(@PathVariable String nombre) {
        return ResponseEntity.ok(reporteService.getReportesByDepartamentoNombre(nombre));
    }

    @GetMapping("/laboratorio/{id}")
    public ResponseEntity<List<Reporte>> getReportesByLaboratorio(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.getReportesByLaboratorioId(id));
    }

    @GetMapping("/enfermedad/{id}")
    public ResponseEntity<List<Reporte>> getReportesByEnfermedad(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.getReportesByEnfermedadId(id));
    }
}
