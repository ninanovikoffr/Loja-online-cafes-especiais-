package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long idProduto;
    private String nome;
    private String descricao;
    private Double preco;
    private String imagemUrl;

    // Relacionamentos como listas de IDs
    private List<Long> carrinhoItensIds;
    private List<Long> pedidoItensIds;
}
