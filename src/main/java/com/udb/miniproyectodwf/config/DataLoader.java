package com.udb.miniproyectodwf.config;

import com.udb.miniproyectodwf.entity.*;
import com.udb.miniproyectodwf.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Override
    public void run(String... args) throws Exception {
        crearUsuariosIniciales();
        crearDepartamentosYMunicipios();
        crearEnfermedades();
        crearLaboratoriosEjemplo();
    }

    private void crearUsuariosIniciales() {
        // Crear usuario ADMIN si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin / admin123");
        }

        // Crear usuario LAB si no existe
        if (usuarioRepository.findByUsername("lab1").isEmpty()) {
            Usuario lab = new Usuario();
            lab.setUsername("lab1");
            lab.setPassword(passwordEncoder.encode("lab123"));
            lab.setRole("LAB");
            usuarioRepository.save(lab);
            System.out.println("✅ Usuario LAB creado: lab1 / lab123");
        }
    }

    private void crearDepartamentosYMunicipios() {
        if (departamentoRepository.count() == 0) {
            System.out.println("📊 Creando departamentos y municipios...");

            // Departamentos de El Salvador
            List<Departamento> departamentos = Arrays.asList(
                    Departamento.builder().nombre("Ahuachapán").activo(true).build(),
                    Departamento.builder().nombre("Santa Ana").activo(true).build(),
                    Departamento.builder().nombre("Sonsonate").activo(true).build(),
                    Departamento.builder().nombre("La Libertad").activo(true).build(),
                    Departamento.builder().nombre("San Salvador").activo(true).build(),
                    Departamento.builder().nombre("Chalatenango").activo(true).build(),
                    Departamento.builder().nombre("Cuscatlán").activo(true).build(),
                    Departamento.builder().nombre("La Paz").activo(true).build(),
                    Departamento.builder().nombre("Cabañas").activo(true).build(),
                    Departamento.builder().nombre("San Vicente").activo(true).build(),
                    Departamento.builder().nombre("Usulután").activo(true).build(),
                    Departamento.builder().nombre("San Miguel").activo(true).build(),
                    Departamento.builder().nombre("Morazán").activo(true).build(),
                    Departamento.builder().nombre("La Unión").activo(true).build()
            );

            departamentos = departamentoRepository.saveAll(departamentos);

            // Crear algunos municipios de ejemplo
            Departamento sanSalvador = departamentos.stream()
                    .filter(d -> d.getNombre().equals("San Salvador"))
                    .findFirst().get();

            Municipio municipio1 = Municipio.builder()
                    .nombre("San Salvador")
                    .departamento(sanSalvador)
                    .activo(true)
                    .build();

            Municipio municipio2 = Municipio.builder()
                    .nombre("Soyapango")
                    .departamento(sanSalvador)
                    .activo(true)
                    .build();

            municipioRepository.saveAll(Arrays.asList(municipio1, municipio2));

            System.out.println("✅ " + departamentos.size() + " departamentos creados");
        }
    }

    private void crearEnfermedades() {
        if (enfermedadRepository.count() == 0) {
            System.out.println("🦠 Creando enfermedades base...");

            List<Enfermedad> enfermedades = Arrays.asList(
                    Enfermedad.builder()
                            .nombre("Dengue")
                            .codigo("A90")
                            .descripcion("Enfermedad viral transmitida por el mosquito Aedes aegypti")
                            .sintomas("Fiebre alta, dolor de cabeza, dolor muscular y articular, erupciones cutáneas")
                            .tratamiento("Hidratación, acetaminofén para la fiebre, reposo")
                            .activa(true)
                            .build(),

                    Enfermedad.builder()
                            .nombre("COVID-19")
                            .codigo("U07.1")
                            .descripcion("Enfermedad respiratoria viral causada por el virus SARS-CoV-2")
                            .sintomas("Fiebre, tos seca, cansancio, pérdida del gusto u olfato")
                            .tratamiento("Aislamiento, medicamentos para síntomas, en casos graves hospitalización")
                            .activa(true)
                            .build(),

                    Enfermedad.builder()
                            .nombre("Influenza")
                            .codigo("J09")
                            .descripcion("Gripe estacional causada por virus de la influenza")
                            .sintomas("Fiebre, escalofríos, tos, dolor de garganta, congestión nasal")
                            .tratamiento("Reposo, líquidos, medicamentos antivirales")
                            .activa(true)
                            .build(),

                    Enfermedad.builder()
                            .nombre("Zika")
                            .codigo("A92.8")
                            .descripcion("Enfermedad viral transmitida por mosquitos")
                            .sintomas("Fiebre leve, sarpullido, dolor articular, conjuntivitis")
                            .tratamiento("Reposo, hidratación, medicamentos para el dolor")
                            .activa(true)
                            .build(),

                    Enfermedad.builder()
                            .nombre("Chikungunya")
                            .codigo("A92.0")
                            .descripcion("Enfermedad viral transmitida por mosquitos")
                            .sintomas("Fiebre, dolor articular severo, dolor muscular, dolor de cabeza")
                            .tratamiento("Reposo, hidratación, medicamentos para el dolor y la fiebre")
                            .activa(true)
                            .build()
            );

            enfermedadRepository.saveAll(enfermedades);
            System.out.println("✅ " + enfermedades.size() + " enfermedades creadas");
        }
    }

    private void crearLaboratoriosEjemplo() {
        if (laboratorioRepository.count() == 0) {
            System.out.println("🔬 Creando laboratorios de ejemplo...");

            List<Laboratorio> laboratorios = Arrays.asList(
                    Laboratorio.builder()
                            .nombre("Laboratorio Central de Salud Pública")
                            .direccion("Final Av. Dr. Emilio Álvarez y Calle Dr. Eugenio Aguilar, San Salvador")
                            .telefono("2222-2222")
                            .email("labcentral@minsal.gob.sv")
                            .activo(true)
                            .build(),

                    Laboratorio.builder()
                            .nombre("Laboratorio Regional Occidental")
                            .direccion("Santa Ana, El Salvador")
                            .telefono("2440-0000")
                            .email("laboccidental@minsal.gob.sv")
                            .activo(true)
                            .build(),

                    Laboratorio.builder()
                            .nombre("Laboratorio Regional Oriental")
                            .direccion("San Miguel, El Salvador")
                            .telefono("2661-0101")
                            .email("laboriental@minsal.gob.sv")
                            .activo(true)
                            .build()
            );

            laboratorioRepository.saveAll(laboratorios);
            System.out.println("✅ " + laboratorios.size() + " laboratorios creados");
        }
    }
}