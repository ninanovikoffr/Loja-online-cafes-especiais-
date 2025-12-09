package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.EnderecoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.EnderecoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.EnderecoService;
import com.engenhariadesoftware.e_comercecafe.Repositories.EnderecoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private EnderecoRepository enderecoRepository;

    /**
     * Endpoint para listar todos os endereços.
     * 
     * @return Lista de todos os endereços.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os endereços", description = "Recupera todos os endereços registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereços recuperados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/listar/{idUsuario}")
    public ResponseEntity<List<EnderecoResponseDTO>> obterEnderecos(@PathVariable Long idUsuario) {
    List<EnderecoModel> enderecos = enderecoRepository.findByUsuarioIdUsuario(idUsuario);
    List<EnderecoResponseDTO> enderecoDTOs = enderecos.stream().map(endereco -> new EnderecoResponseDTO(endereco)).collect(Collectors.toList());
    return ResponseEntity.ok(enderecoDTOs);
}

    /**
     * Endpoint para buscar um endereço por ID.
     * 
     * @param id - ID do endereço a ser recuperado.
     * @return Endereço encontrado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar endereço por ID", description = "Recupera um endereço específico baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @GetMapping("/buscar{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable Long id) {
        return enderecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo endereço.
     * 
     * @param enderecoRequestDTO - Dados do endereço a ser criado.
     * @return O endereço criado.
     */
    @Operation(summary = "Criar novo endereço", description = "Adiciona um novo endereço ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar endereço devido a dados inválidos")
    })
    @PostMapping("/criar")
    public ResponseEntity<EnderecoResponseDTO> criar(@RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO response = enderecoService.salvar(enderecoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para deletar um endereço por ID.
     * 
     * @param id - ID do endereço a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar endereço", description = "Remove um endereço do sistema baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @enderecoService.pertenceAoUsuario(#id)")
    @PatchMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço existente pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<EnderecoResponseDTO> atualizar(@PathVariable Long id, @RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO response = enderecoService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar o endereço do usuário autenticado.
     * 
     * @return Resposta de sucesso ou erro.
     */
    @DeleteMapping("/delete/endereco/me")
    @Operation(summary = "Deletar endereço do usuário autenticado", description = "Remove o endereço associado ao usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço do usuário deletado com sucesso"),
        @ApiResponse(responseCode = "403", description = "Usuário não autorizado a deletar o endereço")
    })
    public ResponseEntity<Object> deletarEndereco() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
        final Long usuarioId = usuario.getIdUsuario();

        return usuario.getEnderecos().stream()
                .filter(end -> end.getIdEndereco().equals(usuarioId))
                .findFirst()
                .map(end -> {
                    enderecoService.deletar(usuarioId);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}
