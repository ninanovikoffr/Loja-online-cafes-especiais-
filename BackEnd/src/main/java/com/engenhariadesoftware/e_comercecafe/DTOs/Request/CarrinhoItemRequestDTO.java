package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItemRequestDTO {
    private Long idCarrinho;
    private Long idProduto;
    private Integer quantidade;
}
