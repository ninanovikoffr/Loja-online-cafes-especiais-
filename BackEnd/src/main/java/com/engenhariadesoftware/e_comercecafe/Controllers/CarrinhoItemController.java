package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.CarrinhoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinho-itens")
public class CarrinhoItemController {

    @Autowired
    private CarrinhoItemService carrinhoItemService;

    @GetMapping
    public List<CarrinhoItemResponseDTO> listarTodos() {
        return carrinhoItemService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return carrinhoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CarrinhoItemResponseDTO criar(@RequestBody CarrinhoItemRequestDTO dto) {
        return carrinhoItemService.salvar(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carrinhoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
