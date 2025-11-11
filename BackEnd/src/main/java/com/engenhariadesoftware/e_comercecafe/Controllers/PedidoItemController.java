package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.PedidoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Services.PedidoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido-itens")
public class PedidoItemController {

    @Autowired
    private PedidoItemService pedidoItemService;

    @GetMapping
    public List<PedidoItemResponseDTO> listarTodos() {
        return pedidoItemService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PedidoItemResponseDTO criar(@RequestBody PedidoItemRequestDTO dto) {
        return pedidoItemService.salvar(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoItemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
