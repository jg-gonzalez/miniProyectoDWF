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

    // USAR EL CAMPO 'casos' QUE ES EL QUE EXISTE EN LA BD
    @Min(value = 1, message = "Debe haber al menos 1 caso")
    @Column(name = "casos", nullable = false)
    private Integer cantidadCasos;

    @NotNull(message = "La fecha de detección es obligatoria")
    @Column(name = "fecha_deteccion", nullable = false)
    private LocalDate fechaDeteccion;

    private String observaciones;

    @Builder.Default
    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte = LocalDateTime.now();

    @Builder.Default
    @Column(name = "estado")
    private String estado = "PENDIENTE";

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;

    // Constructor personalizado actualizado
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
        this.activo = true;
    }

    // Constructor para el método crearReporteConNombres
    public Reporte(Laboratorio laboratorio, Enfermedad enfermedad, Departamento departamento,
                   Municipio municipio, Integer cantidadCasos, LocalDate fechaDeteccion,
                   String observaciones) {
        this.laboratorio = laboratorio;
        this.enfermedad = enfermedad;
        this.departamento = departamento;
        this.municipio = municipio;
        this.cantidadCasos = cantidadCasos;
        this.fechaDeteccion = fechaDeteccion;
        this.observaciones = observaciones;
        this.fechaReporte = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.activo = true;
    }
}