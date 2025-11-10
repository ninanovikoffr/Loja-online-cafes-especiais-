package com.engenhariadesoftware.e_comercecafe.Repositories;

import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItemModel, Long> {
    
}

