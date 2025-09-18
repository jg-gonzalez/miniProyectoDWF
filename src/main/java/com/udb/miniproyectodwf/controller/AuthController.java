package com.udb.miniproyectodwf.controller;

import com.udb.miniproyectodwf.dto.LoginRequest;
import com.udb.miniproyectodwf.dto.LoginResponse;
import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.exception.UnauthorizedException;
import com.udb.miniproyectodwf.security.JwtUtil;
import com.udb.miniproyectodwf.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Validación de usuario y contraseña
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            Usuario usuario = usuarioService.getUsuarioByUsername(request.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("Usuario o contraseña incorrecta"));

            String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRole());

            return ResponseEntity.ok(new LoginResponse(token, usuario.getUsername(), usuario.getRole()));

        } catch (Exception e) {
            throw new UnauthorizedException("Usuario o contraseña incorrecta");
        }
    }
}
