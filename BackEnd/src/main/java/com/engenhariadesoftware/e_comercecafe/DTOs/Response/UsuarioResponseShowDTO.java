package com.engenhariadesoftware.e_comercecafe.DTOs.Response;

import java.time.LocalDate;

import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseShowDTO {

    private Long idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private UsuarioRoles role;
    private LocalDate createdAt;
    
}
