package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    private Long idUsuario;
    private Long idEndereco;
    private String status; // "aguardando", "em_preparo", "enviado", "entregue", "cancelado"
}
