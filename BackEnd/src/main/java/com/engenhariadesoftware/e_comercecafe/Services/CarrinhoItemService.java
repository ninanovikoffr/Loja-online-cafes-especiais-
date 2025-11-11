package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoItemRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoItemModel;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoItemRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoItemService {

    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<CarrinhoItemResponseDTO> listarTodos() {
        return carrinhoItemRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<CarrinhoItemResponseDTO> buscarPorId(Long id) {
        return carrinhoItemRepository.findById(id).map(this::toResponse);
    }

    public CarrinhoItemResponseDTO salvar(CarrinhoItemRequestDTO carrinhoItemRequestDTO) {
        CarrinhoModel carrinho = carrinhoRepository.findById(carrinhoItemRequestDTO.getIdCarrinho())
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        ProdutoModel produto = produtoRepository.findById(carrinhoItemRequestDTO.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        CarrinhoItemModel model = CarrinhoItemModel.builder()
                .carrinho(carrinho)
                .produto(produto)
                .quantidade(carrinhoItemRequestDTO.getQuantidade())
                .build();

        return toResponse(carrinhoItemRepository.save(model));
    }

    public void deletar(Long id) {
        carrinhoItemRepository.deleteById(id);
    }

    private CarrinhoItemResponseDTO toResponse(CarrinhoItemModel carrinhoItemModel) {
        return CarrinhoItemResponseDTO.builder()
                .idCarrinhoItem(carrinhoItemModel.getIdCarrinhoItem())
                .quantidade(carrinhoItemModel.getQuantidade())
                .idCarrinho(carrinhoItemModel.getCarrinho().getIdCarrinho())
                .idProduto(carrinhoItemModel.getProduto().getIdProduto())
                .build();
    }
}
