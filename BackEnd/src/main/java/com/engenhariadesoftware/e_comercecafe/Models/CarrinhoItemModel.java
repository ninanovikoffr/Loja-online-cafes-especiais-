package com.engenhariadesoftware.e_comercecafe.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrinho_itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrinho_item")
    private Long idCarrinhoItem;

    @ManyToOne
    @JoinColumn(name = "id_carrinho", nullable = false)
    private CarrinhoModel carrinho;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private ProdutoModel produto;

    @Column(nullable = false)
    private Integer quantidade;
}
