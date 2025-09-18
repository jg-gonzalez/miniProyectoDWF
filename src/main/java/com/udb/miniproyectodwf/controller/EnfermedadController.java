package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Enfermedad;
import com.udb.miniproyectodwf.service.EnfermedadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades")
public class EnfermedadController {

    private final EnfermedadService enfermedadService;

    public EnfermedadController(EnfermedadService enfermedadService) {
        this.enfermedadService = enfermedadService;
    }

    @GetMapping
    public ResponseEntity<List<Enfermedad>> getAllEnfermedades() {
        return ResponseEntity.ok(enfermedadService.getAllEnfermedades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enfermedad> getEnfermedadById(@PathVariable Long id) {
        return enfermedadService.getEnfermedadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Enfermedad> createEnfermedad(@Valid @RequestBody Enfermedad enfermedad) {
        Enfermedad created = enfermedadService.createEnfermedad(enfermedad);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enfermedad> updateEnfermedad(@PathVariable Long id,
                                                       @Valid @RequestBody Enfermedad enfermedad) {
        Enfermedad updated = enfermedadService.updateEnfermedad(id, enfermedad);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnfermedad(@PathVariable Long id) {
        enfermedadService.deleteEnfermedad(id);
        return ResponseEntity.noContent().build();
    }
}
