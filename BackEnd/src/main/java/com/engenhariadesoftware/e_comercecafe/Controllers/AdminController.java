package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Services.AdminService;
import com.engenhariadesoftware.e_comercecafe.Services.UsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    AdminService adminservice;

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

    @PatchMapping("/tornar-admin/{id}")
    public UsuarioResponseDTO tornarAdmin(@PathVariable Long id) {
        return adminservice.tornarAdmin(id);
}

}
