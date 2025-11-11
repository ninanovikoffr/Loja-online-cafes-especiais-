package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.CarrinhoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CarrinhoResponseDTO> listarTodos() {
        return carrinhoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<CarrinhoResponseDTO> buscarPorId(Long id) {
        return carrinhoRepository.findById(id).map(this::toResponse);
    }

    public CarrinhoResponseDTO salvar(CarrinhoRequestDTO carrinhoRequestDTO) {
        UsuarioModel usuario = usuarioRepository.findById(carrinhoRequestDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CarrinhoModel model = CarrinhoModel.builder()
                .usuario(usuario)
                .build();

        return toResponse(carrinhoRepository.save(model));
    }

    public void deletar(Long id) {
        carrinhoRepository.deleteById(id);
    }

    private CarrinhoResponseDTO toResponse(CarrinhoModel carrinhoModel) {
        return CarrinhoResponseDTO.builder()
                .idCarrinho(carrinhoModel.getIdCarrinho())
                .idUsuario(carrinhoModel.getUsuario().getIdUsuario())
                .build();
    }
}
