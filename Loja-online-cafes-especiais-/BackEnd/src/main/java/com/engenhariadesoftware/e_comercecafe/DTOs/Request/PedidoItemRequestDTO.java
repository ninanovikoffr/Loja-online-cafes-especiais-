package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemRequestDTO {
    private Long idPedido;
    private Long idProduto;
    private Integer quantidade;
}
