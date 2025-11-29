package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor

public class PedidoItemResponseDTO {
    private Long idPedidoItem;
    private Long idProduto;
    private String nome;
    private Integer quantidade;
    private Double precoUnitario;
}