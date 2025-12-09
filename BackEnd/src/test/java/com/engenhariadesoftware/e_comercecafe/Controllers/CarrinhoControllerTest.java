package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.CPF;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Email;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Preco;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:carrinho_controller_test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false"
})
@Transactional
class CarrinhoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void clean() {
        carrinhoRepository.deleteAll();
        produtoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

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

    @Test
    void adicionarProdutoDeveCriarCarrinhoERegistrarItem() throws Exception {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produto = criarProduto("Cafe A", 12.5);

        mockMvc.perform(post("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), produto.getIdProduto())
                        .param("quantidade", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        CarrinhoModel carrinho = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, carrinho.getItens().size());
        assertEquals(2, carrinho.getItens().get(0).getQuantidade());
        assertEquals(produto.getIdProduto(), carrinho.getItens().get(0).getProduto().getIdProduto());
    }

    @Test
    @WithMockUser(roles = "USER")
    void removerProdutoDeveManterApenasItensRestantes() throws Exception {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produtoA = criarProduto("Cafe A", 10.0);
        ProdutoModel produtoB = criarProduto("Cafe B", 20.0);

        mockMvc.perform(post("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), produtoA.getIdProduto())
                        .param("quantidade", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), produtoB.getIdProduto())
                        .param("quantidade", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), produtoA.getIdProduto())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        CarrinhoModel carrinho = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, carrinho.getItens().size());
        assertEquals(produtoB.getIdProduto(), carrinho.getItens().get(0).getProduto().getIdProduto());
        assertEquals(3, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    @WithMockUser(roles = "USER")
    void removerProdutoInexistenteRetornaErro() throws Exception {
        UsuarioModel usuario = criarUsuario();
        ProdutoModel produto = criarProduto("Cafe A", 10.0);

        mockMvc.perform(post("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), produto.getIdProduto())
                        .param("quantidade", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThrows(Exception.class, () ->
                mockMvc.perform(delete("/carrinhos/{idUsuario}/produtos/{idProduto}", usuario.getIdUsuario(), 999)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
        );

        CarrinhoModel carrinho = carrinhoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, carrinho.getItens().size());
    }
}
