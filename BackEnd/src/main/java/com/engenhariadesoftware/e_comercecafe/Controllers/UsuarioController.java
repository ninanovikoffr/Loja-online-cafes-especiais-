package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseShowDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.Services.UsuarioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    /**
     * Endpoint para listar todos os usuários.
     * 
     * @return Lista de todos os usuários.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os usuários", description = "Recupera todos os usuários registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários recuperados com sucesso")
    })
    @GetMapping("/all")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    /**
     * Endpoint para buscar um usuário por ID.
     * 
     * @param id - ID do usuário a ser recuperado.
     * @return Usuário encontrado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por ID", description = "Recupera um usuário específico baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
        if (usuarioResponseDTO != null) {
            return ResponseEntity.ok(usuarioResponseDTO);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para atualizar os dados de um usuário.
     * 
     * @param id - ID do usuário a ser atualizado.
     * @param usuarioRequestDTO - Dados a serem atualizados.
     * @return Usuário atualizado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar usuário", description = "Atualiza as informações de um usuário existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
        if (usuarioResponseDTO != null) {
            UsuarioResponseDTO updatedUser = usuarioService.atualizarDados(id, usuarioRequestDTO);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para criar um novo usuário.
     * 
     * @param usuarioRequestDTO - Dados do novo usuário a ser criado.
     * @return Usuário criado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar usuário devido a dados inválidos")
    })
    @PostMapping("/create")
    public UsuarioResponseDTO criar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        usuarioRequestDTO.setRole(UsuarioRoles.CLIENTE);
        usuarioRequestDTO.setCreatedAt(LocalDate.now());
        usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
        return usuarioService.salvar(usuarioRequestDTO);
    }

    /**
     * Endpoint para deletar um usuário por ID.
     * 
     * @param id - ID do usuário a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para pegar os dados do usuário autenticado.
     * 
     * @return Dados do usuário autenticado.
     */
    @Operation(summary = "Pegar dados do usuário autenticado", description = "Recupera os dados do usuário que está autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do usuário recuperados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseShowDTO> pegarMeusDados() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
        UsuarioResponseShowDTO response = new UsuarioResponseShowDTO();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNome(usuario.getNome());
        response.setCpf(usuario.getCpf().getValue());
        response.setEmail(usuario.getEmail().getValue());
        response.setRole(usuario.getRole());    

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para atualizar os dados do usuário autenticado.
     * 
     * @param usuarioRequestDTO - Dados a serem atualizados.
     * @return Usuário atualizado ou resposta 404 se não encontrado.
     */
    @Operation(summary = "Atualizar dados do usuário autenticado", description = "Atualiza os dados do usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do usuário atualizados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/me/update")
    public ResponseEntity<UsuarioResponseShowDTO> atualizarMeusDados(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioResponseShowDTO updatedUser = usuarioService.atualizarMeusDados(usuario.getIdUsuario(), usuarioRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
