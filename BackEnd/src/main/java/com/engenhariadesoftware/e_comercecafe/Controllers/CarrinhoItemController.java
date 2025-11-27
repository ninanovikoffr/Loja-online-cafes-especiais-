package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.CarrinhoItemService;

import java.util.List;

@RestController
@RequestMapping("/carrinho-itens")
public class CarrinhoItemController {

    @Autowired
    private CarrinhoItemService carrinhoItemService;

    /**
     * Endpoint para listar todos os itens do carrinho.
     * 
     * @return Lista de todos os itens no carrinho.
     */
    @Operation(summary = "Listar todos os itens do carrinho", description = "Recupera todos os itens registrados nos carrinhos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Itens do carrinho recuperados com sucesso")
    })
    @GetMapping
    public List<CarrinhoItemResponseDTO> listarTodos() {
        return carrinhoItemService.listarTodos();
    }

    /**
     * Endpoint para buscar um item de carrinho por ID.
     * 
     * @param id - ID do item do carrinho a ser recuperado.
     * @return O item do carrinho encontrado ou uma resposta 404 caso não encontrado.
     */
    @Operation(summary = "Buscar item do carrinho por ID", description = "Recupera um item específico de carrinho baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item do carrinho encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return carrinhoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo item no carrinho.
     * 
     * @param dto - Dados do item a ser adicionado ao carrinho.
     * @return O item do carrinho criado.
     */
    @Operation(summary = "Criar item no carrinho", description = "Adiciona um novo item ao carrinho.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item do carrinho criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar item devido a dados inválidos")
    })
    @PostMapping
    public CarrinhoItemResponseDTO criar(@RequestBody CarrinhoItemRequestDTO dto) {
        return carrinhoItemService.salvar(dto);
    }

    /**
     * Endpoint para deletar um item do carrinho por ID.
     * 
     * @param id - ID do item do carrinho a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @Operation(summary = "Deletar item do carrinho", description = "Remove um item do carrinho baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item do carrinho deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carrinhoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
