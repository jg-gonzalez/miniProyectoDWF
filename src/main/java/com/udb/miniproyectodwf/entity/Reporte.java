package com.udb.miniproyectodwf.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Enfermedad enfermedad;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Departamento departamento;

    @Column(nullable = false)
    private LocalDate fecha;

    @Min(1)
    private int casos;

    private boolean activo = true; // true = válido, false = corregido/eliminado
}
