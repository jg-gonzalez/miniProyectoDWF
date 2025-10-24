package com.udb.miniproyectodwf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Builder.Default
    private Boolean activo = true;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Municipio> municipios = new ArrayList<>();

    public Departamento(String nombre) {
        this.nombre = nombre;
        this.activo = true;
    }
}