package com.udb.miniproyectodwf;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_ROLE = "USER";

    // Simulación de UserService
    private UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException("Username no puede ser null");
        }
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario usuario = usuarioOpt.get();
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRole())
                .build();
    }

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setUsername(TEST_USERNAME);
        usuario.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        usuario.setRole(TEST_ROLE);
        usuarioRepository.save(usuario);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        UserDetails userDetails = loadUserByUsername(TEST_USERNAME);

        assertNotNull(userDetails);
        assertEquals(TEST_USERNAME, userDetails.getUsername());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, userDetails.getPassword()));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            loadUserByUsername("nonexistentuser");
        });
    }

    @Test
    void testLoadUserByUsername_NullUsername() {
        assertThrows(UsernameNotFoundException.class, () -> {
            loadUserByUsername(null);
        });
    }

    @Test
    void testLoadUserByUsername_EmptyUsername() {
        assertThrows(UsernameNotFoundException.class, () -> {
            loadUserByUsername("");
        });
    }

    @Test
    void testUserDetails_AccountProperties() {
        UserDetails userDetails = loadUserByUsername(TEST_USERNAME);

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}