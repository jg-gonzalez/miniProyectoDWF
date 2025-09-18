package com.udb.miniproyectodwf.service;

import com.udb.miniproyectodwf.entity.Usuario;
import com.udb.miniproyectodwf.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUsuario(Long id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(usuario.getUsername());
                    existing.setEmail(usuario.getEmail());
                    existing.setPassword(usuario.getPassword());
                    existing.setRol(usuario.getRol());
                    return usuarioRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
