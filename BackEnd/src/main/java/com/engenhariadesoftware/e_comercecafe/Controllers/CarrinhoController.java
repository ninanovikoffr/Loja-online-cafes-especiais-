package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.CarrinhoService;

import java.util.List;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {

    @Autowired
    CarrinhoService carrinhoService;

    /**
     * Endpoint para listar todos os carrinhos.
     * 
     * @return Lista de todos os carrinhos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os carrinhos", description = "Recupera todos os carrinhos registrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de carrinhos recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/all")
    public List<CarrinhoResponseDTO> listarTodos() {
        return carrinhoService.listarTodos();
    }

    /**
     * Endpoint para buscar um carrinho por ID.
     * 
     * @param id - ID do carrinho a ser recuperado.
     * @return Carrinho encontrado ou resposta 404 se não encontrado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar carrinho por ID", description = "Recupera os detalhes de um carrinho específico pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrinho encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Carrinho não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoResponseDTO> buscarPorId(@PathVariable Long id) {
        return carrinhoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para criar um novo carrinho.
     * 
     * @param dto - Dados do carrinho a ser criado.
     * @return O carrinho criado.
     */
    @Operation(summary = "Criar novo carrinho", description = "Cria um novo carrinho no sistema com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrinho criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar carrinho devido a dados inválidos")
    })
    @PostMapping("/criar")
    public CarrinhoResponseDTO criar(@RequestBody CarrinhoRequestDTO dto) {
        return carrinhoService.salvar(dto);
    }

    /**
     * Endpoint para deletar um carrinho por ID.
     * 
     * @param id - ID do carrinho a ser deletado.
     * @return Resposta de sucesso ou erro ao deletar o carrinho.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar carrinho", description = "Remove um carrinho do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrinho deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Carrinho não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carrinhoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
