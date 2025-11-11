package com.engenhariadesoftware.e_comercecafe.DTOs.Request;


import java.time.LocalDate;

import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioRequestDTO {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private UsuarioRoles role;
    private LocalDate createdAt;
}
