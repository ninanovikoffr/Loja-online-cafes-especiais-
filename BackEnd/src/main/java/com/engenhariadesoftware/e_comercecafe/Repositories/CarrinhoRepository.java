package com.engenhariadesoftware.e_comercecafe.Repositories;

import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, Long> {
    Optional<CarrinhoModel> findByUsuarioIdUsuario(Long idUsuario);
}
