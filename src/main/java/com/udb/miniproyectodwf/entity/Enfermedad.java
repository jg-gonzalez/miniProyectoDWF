package com.udb.miniproyectodwf.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "enfermedades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enfermedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "enfermedad")
    private List<Reporte> reportes;
}
