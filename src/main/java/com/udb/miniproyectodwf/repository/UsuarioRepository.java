package com.udb.miniproyectodwf.repository;

import com.udb.miniproyectodwf.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar un usuario por username
    Optional<Usuario> findByUsername(String username);

    // Buscar un usuario por email
    Optional<Usuario> findByEmail(String email);
}
