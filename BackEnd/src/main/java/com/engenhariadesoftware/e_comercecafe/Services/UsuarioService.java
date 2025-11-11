package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseShowDTO;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
    return usuarioRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
    }


    public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioModel model;
        model = UsuarioModel.builder()
                .nome(usuarioRequestDTO.getNome())
                .cpf(new CPF(usuarioRequestDTO.getCpf()))
                .email(new Email(usuarioRequestDTO.getEmail()))
                .senha(new Senha(usuarioRequestDTO.getSenha()))
                .role(usuarioRequestDTO.getRole())
                .createdAt(usuarioRequestDTO.getCreatedAt())
                .build();
                
        return toResponse(usuarioRepository.save(model));
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponse(UsuarioModel usuarioModel) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuarioModel.getIdUsuario())
                .nome(usuarioModel.getNome())
                .cpf(usuarioModel.getCpf().getValue())
                .email(usuarioModel.getEmail().getValue())
                .role(usuarioModel.getRole())
                .build();
    }

    public UsuarioResponseShowDTO atualizarMeusDados(Long idUsuario, UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuarioRequestDTO.getNome() != null && !usuarioRequestDTO.getNome().isBlank()) {
            usuario.setNome(usuarioRequestDTO.getNome());
        }
        if (usuarioRequestDTO.getCpf() != null && !usuarioRequestDTO.getCpf().isBlank()) {
            usuario.setCpf(new CPF(usuarioRequestDTO.getCpf()));
        }
        if (usuarioRequestDTO.getEmail() != null && !usuarioRequestDTO.getEmail().isBlank()) {
            usuario.setEmail(new Email(usuarioRequestDTO.getEmail()));
        }
        if (usuarioRequestDTO.getSenha() != null && !usuarioRequestDTO.getSenha().isBlank()) {
            usuario.setSenha(new Senha(passwordEncoder.encode(usuarioRequestDTO.getSenha())));
        }

        UsuarioModel atualizado = usuarioRepository.save(usuario);

        return UsuarioResponseShowDTO.builder()
                .idUsuario(atualizado.getIdUsuario())
                .nome(atualizado.getNome())
                .cpf(atualizado.getCpf().getValue())
                .email(atualizado.getEmail().getValue())
                .build();
    }

    public UsuarioResponseDTO atualizarDados(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuarioRequestDTO.getNome() != null && !usuarioRequestDTO.getNome().isBlank()) {
            usuario.setNome(usuarioRequestDTO.getNome());
        }
        if (usuarioRequestDTO.getCpf() != null && !usuarioRequestDTO.getCpf().isBlank()) {
            usuario.setCpf(new CPF(usuarioRequestDTO.getCpf()));
        }
        if (usuarioRequestDTO.getEmail() != null && !usuarioRequestDTO.getEmail().isBlank()) {
            usuario.setEmail(new Email(usuarioRequestDTO.getEmail()));
        }
        if (usuarioRequestDTO.getSenha() != null && !usuarioRequestDTO.getSenha().isBlank()) {
            usuario.setSenha(new Senha(passwordEncoder.encode(usuarioRequestDTO.getSenha())));
        }

        UsuarioModel atualizado = usuarioRepository.save(usuario);

        return toResponse(atualizado);
    }
}
