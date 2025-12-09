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
            .usuario(usuario)
            .build();

    return toResponse(enderecoRepository.save(model));
    }

    public EnderecoResponseDTO atualizar(Long id, EnderecoRequestDTO dto) {
        EnderecoModel endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        // Atualiza CEP apenas se fornecido e diferente
        if (dto.getCep() != null && !dto.getCep().isEmpty()) {
            ViaCepResponseDTO viaCepResponseDTO = viaCepService.consultarCep(dto.getCep());
            if (viaCepResponseDTO == null) {
                throw new RuntimeException("CEP inválido ou não encontrado");
            }
            endereco.setCep(new CEP(dto.getCep()));
            endereco.setBairro(viaCepResponseDTO.getBairro());
            endereco.setCidade(viaCepResponseDTO.getLocalidade());
            endereco.setEstado(viaCepResponseDTO.getUf());
        }

        // Atualiza rua se fornecida
        if (dto.getRua() != null && !dto.getRua().isEmpty()) {
            endereco.setRua(dto.getRua());
        }

        // Atualiza número se fornecido
        if (dto.getNumero() != null && !dto.getNumero().isEmpty()) {
            endereco.setNumero(dto.getNumero());
        }

        // Atualiza bairro se fornecido (e CEP não foi fornecido)
        if (dto.getBairro() != null && !dto.getBairro().isEmpty() && (dto.getCep() == null || dto.getCep().isEmpty())) {
            endereco.setBairro(dto.getBairro());
        }

        // Atualiza cidade se fornecida (e CEP não foi fornecido)
        if (dto.getCidade() != null && !dto.getCidade().isEmpty() && (dto.getCep() == null || dto.getCep().isEmpty())) {
            endereco.setCidade(dto.getCidade());
        }

        // Atualiza estado se fornecido (e CEP não foi fornecido)
        if (dto.getEstado() != null && !dto.getEstado().isEmpty() && (dto.getCep() == null || dto.getCep().isEmpty())) {
            endereco.setEstado(dto.getEstado());
        }

        // Atualiza complemento se fornecido
        if (dto.getComplemento() != null) {
            endereco.setComplemento(dto.getComplemento());
        }

        EnderecoModel salvo = enderecoRepository.save(endereco);
        return toResponse(salvo);
    }

    public boolean pertenceAoUsuario(Long idEndereco) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);

        if (usuario == null) {
            return false;
        }

        EnderecoModel endereco = enderecoRepository.findById(idEndereco).orElse(null);
        if (endereco == null) {
            return false;
        }

        return endereco.getUsuario().getIdUsuario().equals(usuario.getIdUsuario());
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
                .idUsuario(enderecoModel.getUsuario().getIdUsuario())
                .build();
    }
}
