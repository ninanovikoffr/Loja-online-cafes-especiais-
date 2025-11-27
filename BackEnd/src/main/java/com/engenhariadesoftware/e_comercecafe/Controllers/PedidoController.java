package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.PedidoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Endpoint para listar todos os pedidos.
     * 
     * @return Lista de todos os pedidos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os pedidos", description = "Recupera todos os pedidos registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoService.listarTodos();
    }

    /**
     * Endpoint para buscar um pedido por ID.
     * 
     * @param id - ID do pedido a ser recuperado.
     * @return Pedido encontrado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar pedido por ID", description = "Recupera um pedido específico baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo pedido.
     * 
     * @param dto - Dados do pedido a ser criado.
     * @return O pedido criado.
     */
    @Operation(summary = "Criar novo pedido", description = "Adiciona um novo pedido ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar pedido devido a dados inválidos")
    })
    @PostMapping
    public PedidoResponseDTO criar(@RequestBody PedidoRequestDTO dto) {
        return pedidoService.salvar(dto);
    }

    /**
     * Endpoint para deletar um pedido por ID.
     * 
     * @param id - ID do pedido a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar pedido", description = "Remove um pedido do sistema baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
