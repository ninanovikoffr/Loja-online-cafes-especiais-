package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private UsuarioRoles role;
    private LocalDate createdAt;

    // Relacionamentos como listas de IDs
    private List<Long> enderecosIds;
    private Long carrinhoId;
    private List<Long> pedidosIds;
}
