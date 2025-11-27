package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer estoque;
    private String imagemUrl;
}
