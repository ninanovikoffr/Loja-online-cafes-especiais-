package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.PedidoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.PedidoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.PedidoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.EnderecoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PedidoResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id).map(this::toResponse);
    }

    public PedidoResponseDTO salvar(PedidoRequestDTO pedidoRequestDTO) {
        UsuarioModel usuario = usuarioRepository.findById(pedidoRequestDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        EnderecoModel endereco = enderecoRepository.findById(pedidoRequestDTO.getIdEndereco())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        PedidoModel model = PedidoModel.builder()
                .usuario(usuario)
                .endereco(endereco)
                .status(pedidoRequestDTO.getStatus())
                .build();

        return toResponse(pedidoRepository.save(model));
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO toResponse(PedidoModel pedidoModel) {
        Long idUsuario = null;
        Long idEndereco = null;
        Double total = null;

        if (pedidoModel.getUsuario() != null) {
            idUsuario = pedidoModel.getUsuario().getIdUsuario();
        }
        if (pedidoModel.getEndereco() != null) {
            idEndereco = pedidoModel.getEndereco().getIdEndereco();
        }
        if (pedidoModel.getTotal() != null) {
            total = pedidoModel.getTotal();
        }

        return PedidoResponseDTO.builder()
                .idPedido(pedidoModel.getIdPedido())
                .status(pedidoModel.getStatus())
                .total(total)
                .idUsuario(idUsuario)
                .idEndereco(idEndereco)
                .itens(pedidoModel.getItens() != null ?
                        pedidoModel.getItens().stream().map(item -> {
                            var dto = new com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoItemResponseDTO();
                            dto.setIdPedidoItem(item.getIdPedidoItem());
                            dto.setIdProduto(item.getProduto() != null ? item.getProduto().getIdProduto() : null);
                            dto.setNome(item.getProduto() != null ? item.getProduto().getNome() : null);
                            dto.setQuantidade(item.getQuantidade());
                            dto.setPrecoUnitario(item.getPrecoUnitario());
                            return dto;
                        }).toList() : null)
                .build();
    }
}
