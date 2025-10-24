package com.udb.miniproyectodwf.repository;

import com.udb.miniproyectodwf.entity.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnfermedadRepository extends JpaRepository<Enfermedad, Long> {

    Optional<Enfermedad> findByNombre(String nombre);
    Optional<Enfermedad> findByCodigo(String codigo);
    List<Enfermedad> findByActivaTrue();
    boolean existsByNombre(String nombre);
    boolean existsByCodigo(String codigo);

    // ✅ Obtener las enfermedades más reportadas
    @Query("SELECT e FROM Enfermedad e " +
            "LEFT JOIN Reporte r ON r.enfermedad = e " +
            "GROUP BY e ORDER BY COUNT(r) DESC")
    List<Enfermedad> findTopEnfermedades();
}
