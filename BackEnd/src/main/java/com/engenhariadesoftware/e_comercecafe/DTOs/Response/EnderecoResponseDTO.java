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
    private Boolean isPadrao;
    private Long idUsuario;
}
