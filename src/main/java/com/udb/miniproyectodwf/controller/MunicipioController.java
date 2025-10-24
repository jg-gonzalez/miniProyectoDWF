package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Municipio;
import com.udb.miniproyectodwf.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
@CrossOrigin(origins = "*")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping
    public ResponseEntity<List<Municipio>> getAllMunicipios() {
        return ResponseEntity.ok(municipioService.getAllMunicipios());
    }

    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<Municipio>> getMunicipiosByDepartamento(@PathVariable Long departamentoId) {
        return ResponseEntity.ok(municipioService.getMunicipiosByDepartamento(departamentoId));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Municipio>> getMunicipiosActivos() {
        return ResponseEntity.ok(municipioService.getMunicipiosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Municipio> getMunicipioById(@PathVariable Long id) {
        return municipioService.getMunicipioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) {
        return ResponseEntity.ok(municipioService.createMunicipio(municipio));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Long id) {
        municipioService.deleteMunicipio(id);
        return ResponseEntity.noContent().build();
    }
}