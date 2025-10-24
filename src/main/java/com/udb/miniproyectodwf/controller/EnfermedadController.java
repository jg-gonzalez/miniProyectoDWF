package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.service.EnfermedadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades")
@Tag(name = "3. Enfermedades", description = "Catálogo de enfermedades - Solo ADMIN")
public class EnfermedadController {

    @Autowired
    private EnfermedadService enfermedadService;

    @Operation(summary = "Obtener todas las enfermedades")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Enfermedad> getAllEnfermedades() {
        return enfermedadService.getAllEnfermedades();
    }

    @Operation(summary = "Obtener enfermedades activas")
    @GetMapping("/activas")
    public List<Enfermedad> getEnfermedadesActivas() {
        return enfermedadService.getEnfermedadesActivas();
    }

    @Operation(summary = "Obtener enfermedad por ID")
    @GetMapping("/{id}")
    public Enfermedad getEnfermedadById(@PathVariable Long id) {
        return enfermedadService.getEnfermedadById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enfermedad no encontrada"));
    }

    @Operation(summary = "Crear nueva enfermedad")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Enfermedad createEnfermedad(@RequestBody Enfermedad enfermedad) {
        try {
            return enfermedadService.createEnfermedad(enfermedad);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Actualizar enfermedad")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Enfermedad updateEnfermedad(@PathVariable Long id, @RequestBody Enfermedad enfermedad) {
        try {
            return enfermedadService.updateEnfermedad(id, enfermedad);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Eliminar enfermedad")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEnfermedad(@PathVariable Long id) {
        try {
            enfermedadService.deleteEnfermedad(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}