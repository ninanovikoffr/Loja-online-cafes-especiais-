package com.engenhariadesoftware.e_comercecafe.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.engenhariadesoftware.e_comercecafe.DTOs.Response.ViaCepResponseDTO;

@Service
public class ViaCepService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Construtor que permite injeção personalizada de `RestTemplate`.
     * 
     * @param restTemplate instância de RestTemplate a ser usada
     */
    public ViaCepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Consulta o serviço ViaCEP com o CEP informado e retorna os dados de endereço correspondentes.
     *
     * @param cep CEP a ser consultado
     * @return DTO com informações de logradouro, bairro, cidade, estado, etc.
     */
    public ViaCepResponseDTO consultarCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, ViaCepResponseDTO.class);
    }
}
