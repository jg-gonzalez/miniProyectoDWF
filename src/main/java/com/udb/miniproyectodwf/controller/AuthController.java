package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.security.JwtUtil;
import com.udb.miniproyectodwf.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // INYECCIÓN POR CONSTRUCTOR (MEJOR PRÁCTICA)
    @Autowired
    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userMap) {
        try {
            String username = userMap.get("username");
            String password = userMap.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username y password son requeridos");
            }

            var userOpt = usuarioService.getUsuarioByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            Usuario user = userOpt.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }

            String token = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            if (usuarioService.getUsuarioByUsername(usuario.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("El usuario ya existe");
            }

            if (usuario.getUsername() == null || usuario.getPassword() == null || usuario.getRole() == null) {
                return ResponseEntity.badRequest().body("Todos los campos son requeridos");
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario created = usuarioService.createUsuario(usuario);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("usuario", created);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @GetMapping("/debug-users")
    public ResponseEntity<?> debugUsers() {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            Map<String, Object> response = new HashMap<>();
            response.put("total_users", usuarios.size());
            response.put("users", usuarios);
            response.put("message", usuarios.isEmpty() ? "NO HAY USUARIOS EN LA BD" : "Usuarios encontrados");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener usuarios: " + e.getMessage());
        }
    }
}