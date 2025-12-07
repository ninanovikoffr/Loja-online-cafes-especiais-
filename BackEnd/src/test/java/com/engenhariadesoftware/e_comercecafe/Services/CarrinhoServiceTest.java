package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.PedidoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.CPF;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Email;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Preco;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:carrinho;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false"
})
@Transactional
class CarrinhoServiceTest {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private UsuarioModel criarUsuario() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome("Usuario Teste");
        usuario.setCpf(new CPF("12345678901"));
        usuario.setEmail(new Email("usuario@teste.com"));
        usuario.setSenha(new Senha("senha123"));
        usuario.setRole(UsuarioRoles.CLIENTE);
        return usuarioRepository.save(usuario);
    }

    private ProdutoModel criarProduto(String nome, double preco) {
        ProdutoModel produto = new ProdutoModel();
        produto.setNome(nome);
        produto.setDescricao("Descricao");
        produto.setPreco(new Preco(preco));
        produto.setImagemUrl("imagem.png");
        return produtoRepository.save(produto);
    }

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        carrinhoRepository.deleteAll();
        produtoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void adicionarNovoProdutoCriaCarrinho() {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produto = criarProduto("Cafe A", 10.0);

        CarrinhoResponseDTO response = carrinhoService.adicionarProduto(usuario.getIdUsuario(), produto.getIdProduto(), 2);

        assertNotNull(response);
        CarrinhoModel salvo = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, salvo.getItens().size());
        assertEquals(2, salvo.getItens().get(0).getQuantidade());
        assertEquals(produto.getIdProduto(), salvo.getItens().get(0).getProduto().getIdProduto());
    }

    @Test
    void adicionarProdutoIncrementaQuantidadeQuandoJaExiste() {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produto = criarProduto("Cafe B", 15.0);

        carrinhoService.adicionarProduto(usuario.getIdUsuario(), produto.getIdProduto(), 1);
        carrinhoService.adicionarProduto(usuario.getIdUsuario(), produto.getIdProduto(), 3);

        CarrinhoModel salvo = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, salvo.getItens().size());
        assertEquals(4, salvo.getItens().get(0).getQuantidade());
    }

    @Test
    void removerProdutoRemoveItemDoCarrinho() {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produtoA = criarProduto("Cafe A", 10.0);
        ProdutoModel produtoB = criarProduto("Cafe B", 20.0);

        carrinhoService.adicionarProduto(usuario.getIdUsuario(), produtoA.getIdProduto(), 1);
        carrinhoService.adicionarProduto(usuario.getIdUsuario(), produtoB.getIdProduto(), 2);

        carrinhoService.removerProduto(usuario.getIdUsuario(), produtoA.getIdProduto());

        CarrinhoModel salvo = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, salvo.getItens().size());
        assertEquals(produtoB.getIdProduto(), salvo.getItens().get(0).getProduto().getIdProduto());
    }

    @Test
    void removerProdutoInexistenteLancaExcecao() {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produtoA = criarProduto("Cafe A", 10.0);
        ProdutoModel produtoB = criarProduto("Cafe B", 20.0);

        carrinhoService.adicionarProduto(usuario.getIdUsuario(), produtoA.getIdProduto(), 1);

        assertThrows(RuntimeException.class, () ->
                carrinhoService.removerProduto(usuario.getIdUsuario(), produtoB.getIdProduto())
        );

        CarrinhoModel salvo = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, salvo.getItens().size());
    }
}
