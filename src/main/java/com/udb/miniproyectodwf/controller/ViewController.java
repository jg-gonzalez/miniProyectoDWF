package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.entity.Reporte;
import com.udb.miniproyectodwf.entity.Departamento;
import com.udb.miniproyectodwf.entity.Municipio;
import com.udb.miniproyectodwf.service.EnfermedadService;
import com.udb.miniproyectodwf.service.LaboratorioService;
import com.udb.miniproyectodwf.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private EnfermedadService enfermedadService;

    @Autowired
    private LaboratorioService laboratorioService;

    // ======================== DASHBOARD PÚBLICO ========================
    @GetMapping("/")
    public String dashboardPublico(Model model) {
        try {
            Map<String, Object> estadisticas = reporteService.getEstadisticasAvanzadas();
            var topEnfermedades = reporteService.getTopEnfermedades(5);
            var topDepartamentos = reporteService.getTopDepartamentos(5);
            var alertas = reporteService.getAlertas();

            model.addAttribute("estadisticas", estadisticas);
            model.addAttribute("topEnfermedades", topEnfermedades);
            model.addAttribute("topDepartamentos", topDepartamentos);
            model.addAttribute("alertas", alertas);
        } catch (Exception e) {
            model.addAttribute("estadisticas", Map.of(
                    "totalReportes", 0,
                    "totalCasos", 0,
                    "reportesPendientes", 0,
                    "reportesVerificados", 0
            ));
            model.addAttribute("topEnfermedades", List.of());
            model.addAttribute("topDepartamentos", List.of());
            model.addAttribute("alertas", List.of());
        }
        return "dashboard-publico";
    }

    // ======================== LOGIN ========================
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente");
        }
        return "login";
    }

    // ======================== DASHBOARD PRIVADO ========================
    @GetMapping("/admin/dashboard")
    public String dashboardPrivado(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                Map<String, Object> estadisticas = reporteService.getEstadisticasAvanzadas();
                var topEnfermedades = reporteService.getTopEnfermedades(5);
                var topDepartamentos = reporteService.getTopDepartamentos(5);
                var alertas = reporteService.getAlertas();

                model.addAttribute("username", authentication.getName());
                model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());
                model.addAttribute("estadisticas", estadisticas);
                model.addAttribute("topEnfermedades", topEnfermedades);
                model.addAttribute("topDepartamentos", topDepartamentos);
                model.addAttribute("alertas", alertas);

                return "dashboard-privado";
            } catch (Exception e) {
                return "redirect:/login?error=true";
            }
        }
        return "redirect:/login";
    }

    // ======================== PÁGINAS ADMIN CON LOGGING ========================
    @GetMapping("/admin/reportes")
    public String reportesPage(Model model, Authentication authentication) {
        System.out.println("🔍 === ACCEDIENDO A /admin/reportes ===");
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No autenticado - redirigiendo a login");
            return "redirect:/login";
        }

        try {
            System.out.println("✅ Usuario autenticado: " + authentication.getName());

            List<Reporte> reportes = reporteService.getAllReportes();
            List<Enfermedad> enfermedades = enfermedadService.getEnfermedadesActivas();
            List<Laboratorio> laboratorios = laboratorioService.getLaboratoriosActivos();

            System.out.println("📊 Reportes encontrados: " + reportes.size());
            System.out.println("🦠 Enfermedades encontradas: " + enfermedades.size());
            System.out.println("🔬 Laboratorios encontrados: " + laboratorios.size());

            model.addAttribute("reportes", reportes);
            model.addAttribute("enfermedades", enfermedades);
            model.addAttribute("laboratorios", laboratorios);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());

            System.out.println("✅ Renderizando template: reportes.html");
            return "reportes";
        } catch (Exception e) {
            System.out.println("❌ Error en /admin/reportes: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/admin/enfermedades")
    public String enfermedadesPage(Model model, Authentication authentication) {
        System.out.println("🔍 === ACCEDIENDO A /admin/enfermedades ===");
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No autenticado - redirigiendo a login");
            return "redirect:/login";
        }

        try {
            List<Enfermedad> enfermedades = enfermedadService.getAllEnfermedades();
            model.addAttribute("enfermedades", enfermedades);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());

            System.out.println("✅ Renderizando template: enfermedades.html");
            return "enfermedades";
        } catch (Exception e) {
            System.out.println("❌ Error en /admin/enfermedades: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/admin/laboratorios")
    public String laboratoriosPage(Model model, Authentication authentication) {
        System.out.println("🔍 === ACCEDIENDO A /admin/laboratorios ===");
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No autenticado - redirigiendo a login");
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            System.out.println("❌ Usuario no es ADMIN - redirigiendo");
            return "redirect:/admin/dashboard";
        }

        try {
            List<Laboratorio> laboratorios = laboratorioService.getAllLaboratorios();
            model.addAttribute("laboratorios", laboratorios);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());

            System.out.println("✅ Renderizando template: laboratorios.html");
            return "laboratorios";
        } catch (Exception e) {
            System.out.println("❌ Error en /admin/laboratorios: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/403")
    public String error403() {
        return "403";
    }

    // ======================== MÉTODOS POST ========================
    // Crear Reporte
    @PostMapping("/admin/reportes")
    public String crearReporte(@RequestParam Long enfermedadId,
                               @RequestParam Long laboratorioId,
                               @RequestParam String departamento,
                               @RequestParam String municipio,
                               @RequestParam Integer cantidadCasos,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDeteccion,
                               @RequestParam(required = false) String observaciones,
                               Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            Enfermedad enfermedad = enfermedadService.getEnfermedadById(enfermedadId)
                    .orElseThrow(() -> new RuntimeException("Enfermedad no encontrada"));

            Laboratorio laboratorio = laboratorioService.getLaboratorioById(laboratorioId)
                    .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

            Departamento depto = Departamento.builder().nombre(departamento).build();
            Municipio muni = Municipio.builder().nombre(municipio).departamento(depto).build();

            Reporte reporte = Reporte.builder()
                    .enfermedad(enfermedad)
                    .laboratorio(laboratorio)
                    .departamento(depto)
                    .municipio(muni)
                    .cantidadCasos(cantidadCasos)
                    .fechaDeteccion(fechaDeteccion)
                    .observaciones(observaciones)
                    .estado("PENDIENTE")
                    .fechaReporte(LocalDateTime.now())
                    .build();

            reporteService.createReporte(reporte);

            return "redirect:/admin/reportes?success=true";
        } catch (Exception e) {
            return "redirect:/admin/reportes?error=true";
        }
    }

    // Crear Enfermedad - ADMIN o LAB
    @PostMapping("/admin/enfermedades")
    public String crearEnfermedad(@RequestParam String nombre,
                                  @RequestParam String codigo,
                                  @RequestParam(required = false) String descripcion,
                                  @RequestParam(required = false) String sintomas,
                                  @RequestParam(required = false) String tratamiento,
                                  @RequestParam(defaultValue = "true") Boolean activa,
                                  Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdminOrLab = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_LAB"));

        if (!isAdminOrLab) {
            return "redirect:/admin/dashboard";
        }

        try {
            System.out.println("🦠 Creando nueva enfermedad: " + nombre);

            Enfermedad enfermedad = Enfermedad.builder()
                    .nombre(nombre)
                    .codigo(codigo)
                    .descripcion(descripcion)
                    .sintomas(sintomas)
                    .tratamiento(tratamiento)
                    .activa(activa)
                    .fechaCreacion(LocalDateTime.now())
                    .build();

            enfermedadService.createEnfermedad(enfermedad);

            System.out.println("✅ Enfermedad creada exitosamente por: " + authentication.getName());

            return "redirect:/admin/enfermedades?success=true";
        } catch (Exception e) {
            System.out.println("❌ Error creando enfermedad: " + e.getMessage());
            return "redirect:/admin/enfermedades?error=true";
        }
    }

    // Crear Laboratorio
    @PostMapping("/admin/laboratorios")
    public String crearLaboratorio(@RequestParam String nombre,
                                   @RequestParam(required = false) String direccion,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(defaultValue = "true") Boolean activo,
                                   Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/admin/dashboard";
        }

        try {
            Laboratorio laboratorio = Laboratorio.builder()
                    .nombre(nombre)
                    .direccion(direccion)
                    .telefono(telefono)
                    .email(email)
                    .activo(activo)
                    .fechaCreacion(LocalDateTime.now())
                    .build();

            laboratorioService.createLaboratorio(laboratorio);

            return "redirect:/admin/laboratorios?success=true";
        } catch (Exception e) {
            return "redirect:/admin/laboratorios?error=true";
        }
    }

    // ======================== MÉTODOS ELIMINAR ========================
    @PostMapping("/admin/reportes/{id}/eliminar")
    public String eliminarReporte(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            reporteService.deleteReporte(id);
            return "redirect:/admin/reportes?success=eliminado";
        } catch (Exception e) {
            return "redirect:/admin/reportes?error=eliminar";
        }
    }

    @PostMapping("/admin/enfermedades/{id}/eliminar")
    public String eliminarEnfermedad(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/admin/enfermedades?error=permisos";
        }

        try {
            enfermedadService.deleteEnfermedad(id);
            return "redirect:/admin/enfermedades?success=eliminado";
        } catch (Exception e) {
            return "redirect:/admin/enfermedades?error=eliminar";
        }
    }

    @PostMapping("/admin/laboratorios/{id}/eliminar")
    public String eliminarLaboratorio(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/admin/laboratorios?error=permisos";
        }

        try {
            laboratorioService.deleteLaboratorio(id);
            return "redirect:/admin/laboratorios?success=eliminado";
        } catch (Exception e) {
            return "redirect:/admin/laboratorios?error=eliminar";
        }
    }
}
