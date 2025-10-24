package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.service.UsuarioService;
import com.udb.miniproyectodwf.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Autenticación", description = "Login y registro de usuarios")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Login de usuario")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> userMap) {
        String username = userMap.get("username");
        String password = userMap.get("password");

        // Validar que vengan los datos
        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username y password son requeridos");
        }

        // Buscar usuario
        Usuario usuario = usuarioService.getUsuarioByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        // Validar password
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        // Generar token
        String token = jwtUtil.generateToken(username);

        // ✅ CORREGIDO: Especificar tipos en el HashMap
        Map<String, String> response = new HashMap<String, String>();
        response.put("token", token);
        response.put("username", username);
        response.put("role", usuario.getRole());

        return response; // Spring convierte automáticamente a JSON
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        // Validar campos
        if (usuario.getUsername() == null || usuario.getPassword() == null || usuario.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los campos son requeridos");
        }

        // Verificar si ya existe
        if (usuarioService.getUsuarioByUsername(usuario.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya existe");
        }

        return usuarioService.createUsuario(usuario); // Devuelve el objeto directamente
    }

    @Operation(summary = "Listar todos los usuarios (Solo ADMIN)")
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios(); // Directo, sin ResponseEntity
    }
}
