package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/estadisticas-avanzadas")
    public ResponseEntity<Map<String, Object>> getEstadisticasAvanzadas() {
        try {
            Map<String, Object> estadisticas = reporteService.getEstadisticasAvanzadas();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/top-enfermedades")
    public ResponseEntity<?> getTopEnfermedades(@RequestParam(defaultValue = "5") int limit) {
        try {
            return ResponseEntity.ok(reporteService.getTopEnfermedades(limit));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/top-departamentos")
    public ResponseEntity<?> getTopDepartamentos(@RequestParam(defaultValue = "5") int limit) {
        try {
            return ResponseEntity.ok(reporteService.getTopDepartamentos(limit));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/reportes-por-mes")
    public ResponseEntity<?> getReportesPorMes(@RequestParam int year) {
        try {
            return ResponseEntity.ok(reporteService.getReportesPorMes(year));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/alertas")
    public ResponseEntity<?> getAlertas() {
        try {
            return ResponseEntity.ok(reporteService.getAlertas());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumenDashboard() {
        try {
            Map<String, Object> resumen = Map.of(
                    "estadisticas", reporteService.getEstadisticasAvanzadas(),
                    "alertas", reporteService.getAlertas()
            );
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}