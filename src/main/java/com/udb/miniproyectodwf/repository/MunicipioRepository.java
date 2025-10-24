package com.udb.miniproyectodwf.repository;

import com.udb.miniproyectodwf.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Optional<Municipio> findByNombreAndDepartamentoNombre(String nombre, String departamentoNombre);
    List<Municipio> findByDepartamentoId(Long departamentoId);
    List<Municipio> findByActivoTrue();
    List<Municipio> findByDepartamentoNombreAndActivoTrue(String departamentoNombre);
    boolean existsByNombreAndDepartamentoNombre(String nombre, String departamentoNombre);
}