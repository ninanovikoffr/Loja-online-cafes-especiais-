package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.ProdutoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.ProdutoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Endpoint para listar todos os produtos.
     * 
     * @return Lista de todos os produtos.
     */
    @Operation(summary = "Listar todos os produtos", description = "Recupera todos os produtos registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos recuperados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Endpoint para buscar um produto por ID.
     * 
     * @param id - ID do produto a ser recuperado.
     * @return Produto encontrado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar produto por ID", description = "Recupera um produto específico baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo produto.
     * 
     * @param dto - Dados do produto a ser criado.
     * @return O produto criado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo produto", description = "Adiciona um novo produto ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar produto devido a dados inválidos")
    })
    @PostMapping("/criar")
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO salvo = produtoService.salvar(dto);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Endpoint para atualizar parcialmente um produto.
     * 
     * @param id - ID do produto a ser atualizado.
     * @param dto - Dados parciais do produto a serem atualizados.
     * @return O produto atualizado parcialmente.
     */
    @Operation(summary = "Atualizar parcialmente o produto", description = "Atualiza parcialmente as informações de um produto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado parcialmente com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarParcial(
            @PathVariable Long id,
            @RequestBody ProdutoRequestDTO dto
    ) {
        ProdutoResponseDTO atualizado = produtoService.atualizarParcial(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Endpoint para deletar um produto por ID.
     * 
     * @param id - ID do produto a ser deletado.
     * @return Resposta de sucesso ou erro após a exclusão.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema baseado no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
