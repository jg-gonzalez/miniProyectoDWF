package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Laboratorio;
import com.udb.miniproyectodwf.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@CrossOrigin(origins = "*")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public ResponseEntity<List<Laboratorio>> getAllLaboratorios() {
        List<Laboratorio> laboratorios = laboratorioService.getAllLaboratorios();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Laboratorio>> getLaboratoriosActivos() {
        List<Laboratorio> laboratorios = laboratorioService.getLaboratoriosActivos();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> getLaboratorioById(@PathVariable Long id) {
        return laboratorioService.getLaboratorioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Laboratorio> createLaboratorio(@RequestBody Laboratorio laboratorio) {
        Laboratorio created = laboratorioService.createLaboratorio(laboratorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> updateLaboratorio(@PathVariable Long id, @RequestBody Laboratorio laboratorio) {
        try {
            Laboratorio updated = laboratorioService.updateLaboratorio(id, laboratorio);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaboratorio(@PathVariable Long id) {
        try {
            laboratorioService.deleteLaboratorio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<Laboratorio> getLaboratorioByUsuario(@PathVariable String username) {
        return laboratorioService.getLaboratorioByUsuarioUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{laboratorioId}/asignar-usuario/{usuarioId}")
    public ResponseEntity<Laboratorio> asignarUsuario(@PathVariable Long laboratorioId, @PathVariable Long usuarioId) {
        try {
            Laboratorio laboratorio = laboratorioService.asignarUsuario(laboratorioId, usuarioId);
            return ResponseEntity.ok(laboratorio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}