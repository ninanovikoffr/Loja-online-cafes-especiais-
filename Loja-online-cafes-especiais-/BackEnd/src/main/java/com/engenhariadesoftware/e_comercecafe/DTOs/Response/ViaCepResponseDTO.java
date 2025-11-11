package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

@Getter
@Setter
public class ViaCepResponseDTO {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    public ViaCepResponseDTO(String cep, String logradouro, String bairro, String localidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }
}

