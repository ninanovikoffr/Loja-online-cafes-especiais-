package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoItemResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoItemModel;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<CarrinhoResponseDTO> listarTodos() {
        return carrinhoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<CarrinhoResponseDTO> buscarPorId(Long id) {
        return carrinhoRepository.findById(id).map(this::toResponse);
    }

    public CarrinhoResponseDTO salvar(CarrinhoRequestDTO dto) {
        UsuarioModel usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CarrinhoModel model = CarrinhoModel.builder()
                .usuario(usuario)
                .build();

        return toResponse(carrinhoRepository.save(model));
    }

    public void deletar(Long id) {
        carrinhoRepository.deleteById(id);
    }

    @Transactional
    public CarrinhoResponseDTO adicionarProduto(Long idUsuario, Long idProduto, int quantidade) {

        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CarrinhoModel carrinho = carrinhoRepository
                .findByUsuarioIdUsuario(idUsuario)
                .orElseGet(() -> {
                    CarrinhoModel novo = new CarrinhoModel();
                    novo.setUsuario(usuario);
                    novo.setItens(new ArrayList<>());
                    return carrinhoRepository.save(novo);
                });

        ProdutoModel produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Optional<CarrinhoItemModel> existente = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getIdProduto().equals(idProduto))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setQuantidade(existente.get().getQuantidade() + quantidade);
        } else {
            CarrinhoItemModel novoItem = new CarrinhoItemModel();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setCarrinho(carrinho);
            carrinho.getItens().add(novoItem);
        }

        CarrinhoModel salvo = carrinhoRepository.save(carrinho);
        return toResponse(salvo);
    }

    @Transactional
    public CarrinhoResponseDTO removerProduto(Long idUsuario, Long idProduto) {

        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CarrinhoModel carrinho = carrinhoRepository
                .findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        CarrinhoItemModel item = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getIdProduto().equals(idProduto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produto não está no carrinho"));

        carrinho.getItens().remove(item);

        CarrinhoModel salvo = carrinhoRepository.save(carrinho);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<CarrinhoItemResponseDTO> listarItensDoCarrinho(Long idUsuario) {

        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CarrinhoModel carrinho = carrinhoRepository
                .findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        return carrinho.getItens().stream()
                .map(this::toItemResponse)
                .toList();
    }


    private CarrinhoResponseDTO toResponse(CarrinhoModel carrinho) {
        return CarrinhoResponseDTO.builder()
                .idCarrinho(carrinho.getIdCarrinho())
                .idUsuario(carrinho.getUsuario().getIdUsuario())
                .build();
    }

    private CarrinhoItemResponseDTO toItemResponse(CarrinhoItemModel item) {
        return CarrinhoItemResponseDTO.builder()
                .idCarrinhoItem(item.getIdCarrinhoItem())
                .idCarrinho(item.getCarrinho().getIdCarrinho())
                .idProduto(item.getProduto().getIdProduto())
                .quantidade(item.getQuantidade())
                .build();
    }
}
