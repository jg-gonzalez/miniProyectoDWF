package com.udb.miniproyectodwf.repository;

import com.udb.miniproyectodwf.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByDepartamentoNombre(String departamentoNombre);
    List<Reporte> findByLaboratorioId(Long laboratorioId);
}