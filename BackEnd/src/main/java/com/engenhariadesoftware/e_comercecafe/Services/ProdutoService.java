package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.ProdutoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.ProdutoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Preco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProdutoResponseDTO> buscarPorId(Long id) {
        return produtoRepository.findById(id).map(this::toResponse);
    }

    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {
        ProdutoModel model = ProdutoModel.builder()
                .nome(produtoRequestDTO.getNome())
                .descricao(produtoRequestDTO.getDescricao())
                .preco(new Preco(produtoRequestDTO.getPreco()))
                .estoque(produtoRequestDTO.getEstoque())
                .categoria(produtoRequestDTO.getCategoria())
                .imagemUrl(produtoRequestDTO.getImagemUrl())
                .build();

        return toResponse(produtoRepository.save(model));
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }

    private ProdutoResponseDTO toResponse(ProdutoModel produtoModel) {
        return ProdutoResponseDTO.builder()
                .idProduto(produtoModel.getIdProduto())
                .nome(produtoModel.getNome())
                .descricao(produtoModel.getDescricao())
                .preco(produtoModel.getPreco().getValue())
                .estoque(produtoModel.getEstoque())
                .categoria(produtoModel.getCategoria())
                .imagemUrl(produtoModel.getImagemUrl())
                .build();
    }
}
