package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {
    private String nome;
    private String descricao;
    private Double preco;   // Preco como Double no DTO
    private Integer estoque;
    private String categoria;
    private String imagemUrl;
}
