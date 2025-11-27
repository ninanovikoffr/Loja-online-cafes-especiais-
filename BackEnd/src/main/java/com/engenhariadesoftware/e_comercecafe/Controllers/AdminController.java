package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Endpoint para criar um novo administrador.
     * 
     * @param usuarioRequestDTO - Objeto contendo os dados do novo administrador.
     * @return Retorna os dados do administrador criado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo administrador", description = "Cria um novo usuário com a role de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/criar-admin")
    public UsuarioResponseDTO CriarAdmin(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        usuarioRequestDTO.setRole(UsuarioRoles.ADMIN);
        usuarioRequestDTO.setCreatedAt(LocalDate.now());
        usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
        return usuarioService.salvar(usuarioRequestDTO);
    }

    /**
     * Endpoint para tornar um usuário existente um administrador.
     * 
     * @param id - ID do usuário que será promovido a administrador.
     * @return Retorna os dados do usuário promovido ou null se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tornar um usuário administrador", description = "Promove um usuário existente para a role de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário promovido a administrador com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
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
