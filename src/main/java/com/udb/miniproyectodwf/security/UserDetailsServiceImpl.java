package com.udb.miniproyectodwf.security;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // REMUEVE el "ROLE_" si ya lo tiene
        String role = usuario.getRole();
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5); // Remueve "ROLE_"
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(role) // Spring agregará automáticamente "ROLE_"
                .build();
    }
}