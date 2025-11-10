package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.EnderecoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.EnderecoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.ViaCepResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.EnderecoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.CEP;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<EnderecoResponseDTO> listarTodos() {
        return enderecoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<EnderecoResponseDTO> buscarPorId(Long id) {
        return enderecoRepository.findById(id).map(this::toResponse);
    }

    public EnderecoResponseDTO salvar(EnderecoRequestDTO enderecoRequestDTO) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);

    if (usuario == null) {
        throw new RuntimeException("Usuário autenticado não encontrado");
    }

    ViaCepResponseDTO viaCepResponseDTO = viaCepService.consultarCep(enderecoRequestDTO.getCep());

    if (viaCepResponseDTO == null) {
        throw new RuntimeException("CEP inválido ou não encontrado");
    }

    EnderecoModel model = EnderecoModel.builder()
            .cep(new CEP(enderecoRequestDTO.getCep()))
            .rua(viaCepResponseDTO.getLogradouro())
            .numero(enderecoRequestDTO.getNumero())
            .complemento(enderecoRequestDTO.getComplemento())
            .bairro(viaCepResponseDTO.getBairro())
            .cidade(viaCepResponseDTO.getLocalidade())
            .estado(viaCepResponseDTO.getUf())
            .isPadrao(enderecoRequestDTO.getIsPadrao())
            .usuario(usuario)
            .build();

    return toResponse(enderecoRepository.save(model));
    }


     public void deletar(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

    private EnderecoResponseDTO toResponse(EnderecoModel enderecoModel) {
        return EnderecoResponseDTO.builder()
                .idEndereco(enderecoModel.getIdEndereco())
                .cep(enderecoModel.getCep().getValue())
                .rua(enderecoModel.getRua())
                .numero(enderecoModel.getNumero())
                .complemento(enderecoModel.getComplemento())
                .bairro(enderecoModel.getBairro())
                .cidade(enderecoModel.getCidade())
                .estado(enderecoModel.getEstado())
                .isPadrao(enderecoModel.getIsPadrao())
                .idUsuario(enderecoModel.getUsuario().getIdUsuario())
                .build();
    }

    public EnderecoModel tornarPadrao(Long id) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
        Long usuarioId = usuario.getIdUsuario();

        EnderecoModel endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));
        

        if (!endereco.getUsuario().getIdUsuario().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para alterar este endereço");
        }
        endereco.setIsPadrao(true);
        return enderecoRepository.save(endereco);
        
        
    }
}
