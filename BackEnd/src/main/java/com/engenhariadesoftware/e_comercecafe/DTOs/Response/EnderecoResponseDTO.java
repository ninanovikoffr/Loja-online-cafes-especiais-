package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponseDTO {
    private Long idEndereco;
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private Long idUsuario;



    public EnderecoResponseDTO(com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel enderecoModel) {
        this.idEndereco = enderecoModel.getIdEndereco();
        this.cep = enderecoModel.getCep() != null ? enderecoModel.getCep().getValue() : null;
        this.rua = enderecoModel.getRua();
        this.numero = enderecoModel.getNumero();
        this.complemento = enderecoModel.getComplemento();
        this.bairro = enderecoModel.getBairro();
        this.cidade = enderecoModel.getCidade();
        this.estado = enderecoModel.getEstado();
        this.idUsuario = enderecoModel.getUsuario() != null ? enderecoModel.getUsuario().getIdUsuario() : null;
    }
}