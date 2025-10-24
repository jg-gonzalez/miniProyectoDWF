package com.udb.miniproyectodwf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "enfermedad_id", nullable = false)
    private Enfermedad enfermedad;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @Min(value = 1, message = "Debe haber al menos 1 caso")
    private Integer cantidadCasos;

    @NotNull(message = "La fecha de detección es obligatoria")
    @Column(name = "fecha_deteccion")
    private LocalDate fechaDeteccion;

    private String observaciones;

    @Builder.Default
    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte = LocalDateTime.now();

    @Builder.Default
    @Column(name = "estado")
    private String estado = "PENDIENTE";

    // Constructor personalizado
    public Reporte(Laboratorio laboratorio, Enfermedad enfermedad, Departamento departamento,
                   Municipio municipio, Integer cantidadCasos, LocalDate fechaDeteccion) {
        this.laboratorio = laboratorio;
        this.enfermedad = enfermedad;
        this.departamento = departamento;
        this.municipio = municipio;
        this.cantidadCasos = cantidadCasos;
        this.fechaDeteccion = fechaDeteccion;
        this.fechaReporte = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
}