package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.service.LaboratorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    public ResponseEntity<List<Laboratorio>> getAllLaboratorios() {
        return ResponseEntity.ok(laboratorioService.getAllLaboratorios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> getLaboratorioById(@PathVariable Long id) {
        return laboratorioService.getLaboratorioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Laboratorio> createLaboratorio(@Valid @RequestBody Laboratorio laboratorio) {
        Laboratorio created = laboratorioService.createLaboratorio(laboratorio);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> updateLaboratorio(@PathVariable Long id,
                                                         @Valid @RequestBody Laboratorio laboratorio) {
        Laboratorio updated = laboratorioService.updateLaboratorio(id, laboratorio);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaboratorio(@PathVariable Long id) {
        laboratorioService.deleteLaboratorio(id);
        return ResponseEntity.noContent().build();
    }
}
