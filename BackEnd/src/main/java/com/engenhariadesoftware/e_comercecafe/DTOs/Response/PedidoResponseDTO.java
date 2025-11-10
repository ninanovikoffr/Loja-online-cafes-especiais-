package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long idPedido;
    private String status;
    private LocalDate createdAt;

    // Relacionamentos como IDs
    private Long idUsuario;
    private Long idEndereco;
    private List<Long> itensIds;
}
