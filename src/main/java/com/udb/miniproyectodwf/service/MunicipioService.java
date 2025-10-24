package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Municipio;
import com.udb.miniproyectodwf.entity.Departamento;
import com.udb.miniproyectodwf.repository.MunicipioRepository;
import com.udb.miniproyectodwf.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    public List<Municipio> getMunicipiosActivos() {
        return municipioRepository.findByActivoTrue();
    }

    public Optional<Municipio> getMunicipioById(Long id) {
        return municipioRepository.findById(id);
    }

    public Municipio createMunicipio(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    public Municipio createMunicipio(String nombre, Long departamentoId) {
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        Municipio municipio = new Municipio();
        municipio.setNombre(nombre);
        municipio.setDepartamento(departamento);
        municipio.setActivo(true);

        return municipioRepository.save(municipio);
    }

    public Municipio updateMunicipio(Long id, Municipio municipioDetails) {
        Municipio municipio = municipioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Municipio no encontrado"));

        municipio.setNombre(municipioDetails.getNombre());
        municipio.setDepartamento(municipioDetails.getDepartamento());
        municipio.setActivo(municipioDetails.getActivo());

        return municipioRepository.save(municipio);
    }

    public void deleteMunicipio(Long id) {
        if (!municipioRepository.existsById(id)) {
            throw new RuntimeException("Municipio no encontrado");
        }
        municipioRepository.deleteById(id);
    }

    public void desactivarMunicipio(Long id) {
        Municipio municipio = municipioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Municipio no encontrado"));
        municipio.setActivo(false);
        municipioRepository.save(municipio);
    }

    public List<Municipio> getMunicipiosPorDepartamento(Long departamentoId) {
        return municipioRepository.findByDepartamentoId(departamentoId);
    }

    // AGREGAR ESTE MÉTODO - es el que falta
    public List<Municipio> getMunicipiosByDepartamento(Long departamentoId) {
        return municipioRepository.findByDepartamentoId(departamentoId);
    }

    public List<Municipio> getMunicipiosPorDepartamentoNombre(String departamentoNombre) {
        return municipioRepository.findByDepartamentoNombreAndActivoTrue(departamentoNombre);
    }

    public Optional<Municipio> buscarPorNombreYDepartamento(String municipioNombre, String departamentoNombre) {
        return municipioRepository.findByNombreAndDepartamentoNombre(municipioNombre, departamentoNombre);
    }

    public boolean existeMunicipioEnDepartamento(String municipioNombre, String departamentoNombre) {
        return municipioRepository.existsByNombreAndDepartamentoNombre(municipioNombre, departamentoNombre);
    }
}