package com.udb.miniproyectodwf;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_ROLE = "USER";

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }


    private Usuario registrarUsuario(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede ser nulo o vacío");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password no puede ser nulo o vacío");
        }

        // Verificar si usuario ya existe
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRole(role);
        return usuarioRepository.save(usuario);
    }

    @Test
    void testRegistrarUsuario_Success() {
        Usuario usuario = registrarUsuario(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        assertNotNull(usuario);
        assertNotNull(usuario.getId());
        assertEquals(TEST_USERNAME, usuario.getUsername());
        assertEquals(TEST_ROLE, usuario.getRole());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, usuario.getPassword()));
    }

    @Test
    void testRegistrarUsuario_DuplicateUsername() {
        registrarUsuario(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        assertThrows(RuntimeException.class, () -> {
            registrarUsuario(TEST_USERNAME, "otherpassword", "ADMIN");
        });
    }

    @Test
    void testRegistrarUsuario_NullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            registrarUsuario(TEST_USERNAME, null, TEST_ROLE);
        });
    }

    @Test
    void testRegistrarUsuario_EmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            registrarUsuario(TEST_USERNAME, "", TEST_ROLE);
        });
    }

    @Test
    void testRegistrarUsuario_NullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            registrarUsuario(null, TEST_PASSWORD, TEST_ROLE);
        });
    }
}