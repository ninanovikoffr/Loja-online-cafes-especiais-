package com.engenhariadesoftware.e_comercecafe.Models;

import com.engenhariadesoftware.e_comercecafe.ValueObjects.Preco;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "preco", nullable = false))
    private Preco preco;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(name = "imagem_url", length = 255)
    private String imagemUrl;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    //Relacionamentos
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoItemModel> carrinhoItens;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItemModel> pedidoItens;
}
