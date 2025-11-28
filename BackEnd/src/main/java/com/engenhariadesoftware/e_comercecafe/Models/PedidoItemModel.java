package com.engenhariadesoftware.e_comercecafe.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedido_itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_item")
    private Long idPedidoItem;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoModel pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private ProdutoModel produto;

    @Column(name = "quantidade")
    private Integer quantidade;
}
