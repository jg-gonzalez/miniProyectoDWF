package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Municipio;
import com.udb.miniproyectodwf.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    public List<Municipio> getMunicipiosByDepartamento(Long departamentoId) {
        return municipioRepository.findByDepartamentoId(departamentoId);
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

    public void deleteMunicipio(Long id) {
        municipioRepository.deleteById(id);
    }
}