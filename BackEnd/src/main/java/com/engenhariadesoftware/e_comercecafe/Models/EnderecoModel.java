package com.engenhariadesoftware.e_comercecafe.Models;

import com.engenhariadesoftware.e_comercecafe.ValueObjects.CEP;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long idEndereco;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cep", length = 9))
    private CEP cep;

    @Column(length = 150)
    private String rua;

    @Column(length = 10)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;
}
