package com.udb.miniproyectodwf.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "municipios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @Builder.Default
    private Boolean activo = true;
}