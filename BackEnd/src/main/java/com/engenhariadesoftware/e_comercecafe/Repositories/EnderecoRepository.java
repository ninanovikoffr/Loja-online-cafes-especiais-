package com.engenhariadesoftware.e_comercecafe.Repositories;

import com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoModel, Long> {
    List<EnderecoModel> findByUsuarioIdUsuario(Long idUsuario);
}
