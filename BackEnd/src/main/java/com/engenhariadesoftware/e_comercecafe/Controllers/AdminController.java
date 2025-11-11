package com.engenhariadesoftware.e_comercecafe.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Services.UsuarioService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsuarioService usuarioService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/criar-admin")
    public UsuarioResponseDTO CriarAdmin(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        usuarioRequestDTO.setRole(UsuarioRoles.ADMIN);
        usuarioRequestDTO.setCreatedAt(LocalDate.now());
        usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
        return usuarioService.salvar(usuarioRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/tornar-admin")
    public UsuarioResponseDTO tornaradmin(@RequestBody Long id) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
        if (usuarioResponseDTO != null) {
            UsuarioRequestDTO usuarioRequestDTO = UsuarioRequestDTO.builder()
                    .role(UsuarioRoles.ADMIN)
                    .build();
            return usuarioService.salvar(usuarioRequestDTO);
        }
        return null;
        
    }
    



}
