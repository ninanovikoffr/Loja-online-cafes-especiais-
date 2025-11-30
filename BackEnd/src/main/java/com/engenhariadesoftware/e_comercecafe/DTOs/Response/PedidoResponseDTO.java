package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long idPedido;
    private String status;
    private Double total;

    private Long idUsuario;
    private Long idEndereco;
    private List<PedidoItemResponseDTO> itens;
}
