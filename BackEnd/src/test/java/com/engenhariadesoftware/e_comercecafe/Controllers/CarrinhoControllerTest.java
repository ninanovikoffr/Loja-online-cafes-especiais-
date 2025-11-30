package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.Services.CarrinhoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes unitários para CarrinhoController, utilizando Mockito
 * para isolar a camada de serviço.
 */
@ExtendWith(MockitoExtension.class)
class CarrinhoControllerTest {

    @Mock
    private CarrinhoService carrinhoService; // Simula o serviço de carrinho

    @InjectMocks
    private CarrinhoController carrinhoController; // Injeta o serviço mockado no Controller

    private MockMvc mockMvc;

    // IDs de exemplo
    private final Long ID_USUARIO = 1L;
    private final Long ID_PRODUTO = 10L;
    private final int QUANTIDADE = 2;

    @BeforeEach
    void setUp() {
        // Configura o MockMvc para testar apenas o CarrinhoController
        mockMvc = MockMvcBuilders.standaloneSetup(carrinhoController).build();
    }

    @Test
    void testAdicionarProduto_DeveRetornarOkEChamarServico() throws Exception {
        // Simular chamada para o endpoint POST /carrinhos/{idUsuario}/produtos/{idProduto}
        mockMvc.perform(post("/carrinhos/{idUsuario}/produtos/{idProduto}", ID_USUARIO, ID_PRODUTO)
                        // Adiciona o parâmetro de quantidade
                        .param("quantidade", String.valueOf(QUANTIDADE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Verifica se o status HTTP é 200 OK

        // VERIFICAÇÃO CHAVE:
        // Verifica se o método 'adicionarProduto' foi chamado no CarrinhoService
        // com os parâmetros corretos após a requisição HTTP.
        verify(carrinhoService, times(1)).adicionarProduto(ID_USUARIO, ID_PRODUTO, QUANTIDADE);
        verifyNoMoreInteractions(carrinhoService);
    }
    
    @Test
    void testRemoverProduto_DeveRetornarOkEChamarServico() throws Exception {
        // Simular chamada para o endpoint DELETE /carrinhos/{idUsuario}/produtos/{idProduto}
        mockMvc.perform(delete("/carrinhos/{idUsuario}/produtos/{idProduto}", ID_USUARIO, ID_PRODUTO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Verifica se o status HTTP é 200 OK

        // VERIFICAÇÃO CHAVE:
        // Verifica se o método 'removerProduto' foi chamado no CarrinhoService
        // com os parâmetros corretos.
        verify(carrinhoService, times(1)).removerProduto(ID_USUARIO, ID_PRODUTO);
        verifyNoMoreInteractions(carrinhoService);
    }
}