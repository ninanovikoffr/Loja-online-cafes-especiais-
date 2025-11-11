package com.engenhariadesoftware.e_comercecafe.Repositories;


import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    List<ProdutoModel> findByCategoria(String categoria);
}

