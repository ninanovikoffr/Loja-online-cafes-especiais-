package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.PedidoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.PedidoItemModel;
import com.engenhariadesoftware.e_comercecafe.Models.PedidoModel;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.PedidoItemRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.PedidoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoItemService {

    @Autowired
    private PedidoItemRepository pedidoItemRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<PedidoItemResponseDTO> listarTodos() {
        return pedidoItemRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<PedidoItemResponseDTO> buscarPorId(Long id) {
        return pedidoItemRepository.findById(id).map(this::toResponse);
    }

    public PedidoItemResponseDTO salvar(PedidoItemRequestDTO pedidoItemRequestDTO) {
        PedidoModel pedido = pedidoRepository.findById(pedidoItemRequestDTO.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        ProdutoModel produto = produtoRepository.findById(pedidoItemRequestDTO.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        PedidoItemModel model = PedidoItemModel.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(pedidoItemRequestDTO.getQuantidade())
                .build();

        return toResponse(pedidoItemRepository.save(model));
    }

    public void deletar(Long id) {
        pedidoItemRepository.deleteById(id);
    }

    private PedidoItemResponseDTO toResponse(PedidoItemModel pedidoItemModel) {
        return PedidoItemResponseDTO.builder()
                .idPedidoItem(pedidoItemModel.getIdPedidoItem())
                .quantidade(pedidoItemModel.getQuantidade())
                .idPedido(pedidoItemModel.getPedido().getIdPedido())
                .idProduto(pedidoItemModel.getProduto().getIdProduto())
                .build();
    }
}
