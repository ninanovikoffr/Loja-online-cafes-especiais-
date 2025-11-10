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

    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

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
        return PedidoResponseDTO.builder()
                .idPedido(pedidoModel.getIdPedido())
                .status(pedidoModel.getStatus())
                .idUsuario(pedidoModel.getUsuario().getIdUsuario())
                .idEndereco(pedidoModel.getEndereco().getIdEndereco())
                .build();
    }
}
