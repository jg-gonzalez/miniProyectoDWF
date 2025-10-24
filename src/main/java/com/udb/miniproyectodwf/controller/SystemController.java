package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.service.LaboratorioService;
import com.udb.miniproyectodwf.service.ReporteService;
import com.udb.miniproyectodwf.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@Tag(name = "6. Sistema", description = "Monitoreo y salud del sistema")
public class SystemController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Estado de salud del sistema")
    @GetMapping("/health")
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();

        try {
            // Estadísticas del sistema
            int totalUsuarios = usuarioService.getAllUsuarios().size();
            int totalLaboratorios = laboratorioService.getAllLaboratorios().size();
            int totalLaboratoriosActivos = laboratorioService.getLaboratoriosActivos().size();
            int totalReportes = reporteService.getAllReportes().size();

            health.put("status", "OK");
            health.put("timestamp", LocalDateTime.now());
            health.put("totalUsuarios", totalUsuarios);
            health.put("totalLaboratorios", totalLaboratorios);
            health.put("laboratoriosActivos", totalLaboratoriosActivos);
            health.put("totalReportes", totalReportes);
            health.put("uptime", "Running");

        } catch (Exception e) {
            health.put("status", "ERROR");
            health.put("error", e.getMessage());
        }

        return health;
    }

    @Operation(summary = "Métricas para monitoreo")
    @GetMapping("/metrics")
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        Map<String, Object> estadisticas = reporteService.getEstadisticasAvanzadas();

        metrics.put("timestamp", LocalDateTime.now());
        metrics.put("estadisticas", estadisticas);
        metrics.put("database", "MySQL - Conectado");
        metrics.put("security", "JWT - Activo");
        metrics.put("api", "Spring Boot - Operacional");

        return metrics;
    }
}