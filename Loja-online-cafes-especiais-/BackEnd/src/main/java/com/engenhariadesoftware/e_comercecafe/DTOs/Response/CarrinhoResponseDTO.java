package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoResponseDTO {
    private Long idCarrinho;
    private Long idUsuario;
    private List<Long> itens; // lista de IDs dos itens do carrinho
}
