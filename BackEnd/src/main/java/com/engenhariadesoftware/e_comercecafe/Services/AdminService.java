package com.engenhariadesoftware.e_comercecafe.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;

@Service
public class AdminService {

    @Autowired UsuarioRepository usuarioRepository;
    
    public UsuarioResponseDTO tornarAdmin(Long id) {
    UsuarioModel usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    usuario.setRole(UsuarioRoles.ADMIN);

    usuarioRepository.save(usuario);

    return UsuarioResponseDTO.builder()
            .idUsuario(usuario.getIdUsuario())
            .nome(usuario.getNome())
            .cpf(usuario.getCpf().getValue())
            .email(usuario.getEmail().getValue())
            .role(usuario.getRole())
            .build();
}
}
