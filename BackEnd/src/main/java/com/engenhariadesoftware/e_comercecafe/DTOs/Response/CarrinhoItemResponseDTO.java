package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItemResponseDTO {
    private Long idCarrinhoItem;
    private Long idCarrinho;
    private Long idProduto;
    private Integer quantidade;
}
