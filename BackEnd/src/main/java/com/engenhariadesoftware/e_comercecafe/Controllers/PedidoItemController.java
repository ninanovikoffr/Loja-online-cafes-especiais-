package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.PedidoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.PedidoItemService;

import java.util.List;

@RestController
@RequestMapping("/pedido-itens")
public class PedidoItemController {

    @Autowired
    private PedidoItemService pedidoItemService;

    /**
     * Endpoint para listar todos os itens do pedido.
     * 
     * @return Lista de todos os itens no pedido.
     */
    @Operation(summary = "Listar todos os itens do pedido", description = "Recupera todos os itens de pedidos registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Itens do pedido recuperados com sucesso")
    })
    @GetMapping
    public List<PedidoItemResponseDTO> listarTodos() {
        return pedidoItemService.listarTodos();
    }

    /**
     * Endpoint para buscar um item de pedido por ID.
     * 
     * @param id - ID do item do pedido a ser recuperado.
     * @return Item do pedido encontrado ou resposta 404 se não encontrado.
     */
    @Operation(summary = "Buscar item do pedido por ID", description = "Recupera um item específico de pedido baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item do pedido encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item do pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo item no pedido.
     * 
     * @param dto - Dados do item a ser adicionado ao pedido.
     * @return O item do pedido criado.
     */
    @Operation(summary = "Criar item no pedido", description = "Adiciona um novo item ao pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item do pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar item devido a dados inválidos")
    })
    @PostMapping
    public PedidoItemResponseDTO criar(@RequestBody PedidoItemRequestDTO dto) {
        return pedidoItemService.salvar(dto);
    }

    /**
     * Endpoint para deletar um item do pedido por ID.
     * 
     * @param id - ID do item do pedido a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @Operation(summary = "Deletar item do pedido", description = "Remove um item do pedido baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item do pedido deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item do pedido não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
