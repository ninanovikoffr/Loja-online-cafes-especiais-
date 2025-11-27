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

    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        ProdutoModel model = ProdutoModel.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(new Preco(dto.getPreco()))
                .imagemUrl(dto.getImagemUrl())
                .build();

        return toResponse(produtoRepository.save(model));
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        ProdutoModel existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setPreco(new Preco(dto.getPreco()));
        existente.setImagemUrl(dto.getImagemUrl());

        return toResponse(produtoRepository.save(existente));
    }

    public ProdutoResponseDTO atualizarParcial(Long id, ProdutoRequestDTO dto) {
        ProdutoModel existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (dto.getNome() != null) existente.setNome(dto.getNome());
        if (dto.getDescricao() != null) existente.setDescricao(dto.getDescricao());
        if (dto.getPreco() != null) existente.setPreco(new Preco(dto.getPreco()));
        if (dto.getImagemUrl() != null) existente.setImagemUrl(dto.getImagemUrl());

        return toResponse(produtoRepository.save(existente));
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    private ProdutoResponseDTO toResponse(ProdutoModel model) {
        return ProdutoResponseDTO.builder()
                .idProduto(model.getIdProduto())
                .nome(model.getNome())
                .descricao(model.getDescricao())
                .preco(model.getPreco().getValue())
                .imagemUrl(model.getImagemUrl())
                .build();
    }
}