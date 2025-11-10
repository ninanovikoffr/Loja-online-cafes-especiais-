package com.engenhariadesoftware.e_comercecafe.Repositories;

import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    UsuarioModel findByEmail_Value(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}

