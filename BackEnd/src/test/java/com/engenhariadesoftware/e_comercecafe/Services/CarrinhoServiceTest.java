package com.engenhariadesoftware.e_comercecafe.Services;

import com.engenhariadesoftware.e_comercecafe.DTOs.Response.CarrinhoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.PedidoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoItemModel;
import com.engenhariadesoftware.e_comercecafe.Models.CarrinhoModel;
import com.engenhariadesoftware.e_comercecafe.Models.PedidoModel;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Preco;
import com.engenhariadesoftware.e_comercecafe.Models.ProdutoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.CarrinhoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.PedidoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.ProdutoRepository;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoRepository pedidoRepository; // Mock necessário para o método finalizarCompra

    @InjectMocks
    private CarrinhoService carrinhoService;

    private final Long ID_USUARIO = 1L;
    private final Long ID_PRODUTO_A = 10L;
    private final Long ID_PRODUTO_B = 20L;
    private final double PRECO_PRODUTO_A = 10.0;
    private final double PRECO_PRODUTO_B = 50.0;

    private UsuarioModel mockUsuario;
    private ProdutoModel mockProdutoA;
    private ProdutoModel mockProdutoB;
    private Preco mockPrecoA;
    private Preco mockPrecoB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup básico de mocks de dependência
        mockUsuario = new UsuarioModel();
        
        // Setup do Preço A
        mockPrecoA = mock(Preco.class);
        when(mockPrecoA.getValue()).thenReturn(PRECO_PRODUTO_A); // USANDO getValue() conforme o service
        
        // Setup do Produto A
        mockProdutoA = new ProdutoModel();
        mockProdutoA.setIdProduto(ID_PRODUTO_A);
        mockProdutoA.setPreco(mockPrecoA);
        mockProdutoA.setNome("Café Premium A");

        // Setup do Preço B
        mockPrecoB = mock(Preco.class);
        when(mockPrecoB.getValue()).thenReturn(PRECO_PRODUTO_B); // USANDO getValue() conforme o service

        // Setup do Produto B
        mockProdutoB = new ProdutoModel();
        mockProdutoB.setIdProduto(ID_PRODUTO_B);
        mockProdutoB.setPreco(mockPrecoB);
        mockProdutoB.setNome("Café Raro B");

        // Configura o repositório de usuário para ser encontrado em todos os testes
        when(usuarioRepository.findById(ID_USUARIO)).thenReturn(Optional.of(mockUsuario));
        when(produtoRepository.findById(ID_PRODUTO_A)).thenReturn(Optional.of(mockProdutoA));
        when(produtoRepository.findById(ID_PRODUTO_B)).thenReturn(Optional.of(mockProdutoB));
    }

    // =========================================================================
    // Testes para Adicionar Produto
    // =========================================================================

    @Test
    void testAdicionarNovoProdutoACarrinhoExistente() {
        // Mock: Carrinho existente e vazio
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        mockCarrinho.setItens(new ArrayList<>()); 

        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));
        when(carrinhoRepository.save(any(CarrinhoModel.class))).thenReturn(mockCarrinho);

        // 4. Chamada ao método que está sendo testado
        CarrinhoResponseDTO response = carrinhoService.adicionarProduto(ID_USUARIO, ID_PRODUTO_A, 2);

        // 5. Asserções
        assertNotNull(response);
        verify(carrinhoRepository, times(1)).save(mockCarrinho); 
        assertEquals(1, mockCarrinho.getItens().size(), "Deve haver 1 item novo no carrinho.");
        assertEquals(2, mockCarrinho.getItens().get(0).getQuantidade());
    }
    
    @Test
    void testCriarNovoCarrinhoAoAdicionarProduto() {
        // Mock: Usuário não tem carrinho existente
        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.empty());

        // Simula o salvamento do novo carrinho criado
        when(carrinhoRepository.save(any(CarrinhoModel.class))).thenAnswer(invocation -> {
            CarrinhoModel novoCarrinho = invocation.getArgument(0);
            // Simula o retorno do objeto salvo com ID e a lista inicializada
            novoCarrinho.setIdCarrinho(1L); 
            if (novoCarrinho.getItens() == null) {
                novoCarrinho.setItens(new ArrayList<>());
            }
            return novoCarrinho;
        });

        // Chamada ao método
        CarrinhoResponseDTO response = carrinhoService.adicionarProduto(ID_USUARIO, ID_PRODUTO_A, 1);

        // Asserções
        assertNotNull(response);
        // Verifica se o findByUsuarioIdUsuario foi chamado
        verify(carrinhoRepository, times(1)).findByUsuarioIdUsuario(ID_USUARIO);
        // Verifica se o método save foi chamado duas vezes: uma para criar, outra para adicionar o item
        // Dependendo da implementação, pode ser chamado apenas uma vez se o item for adicionado antes do save final.
        // Se a implementação cria e salva, e depois adiciona e salva, são 2. No Service, a criação é feita no orElseGet e salva,
        // e o item é adicionado e salvo novamente no final. Então verificamos duas chamadas.
        verify(carrinhoRepository, times(2)).save(any(CarrinhoModel.class)); 
    }

    @Test
    void testAumentarQuantidadeProdutoExistente() {
        int quantidadeInicial = 5;
        int quantidadeAdicional = 3;

        // Mock: Carrinho existente com um item
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        
        CarrinhoItemModel itemExistente = new CarrinhoItemModel();
        itemExistente.setProduto(mockProdutoA);
        itemExistente.setQuantidade(quantidadeInicial); 
        
        List<CarrinhoItemModel> itens = new ArrayList<>(Collections.singletonList(itemExistente));
        mockCarrinho.setItens(itens); 

        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));
        when(carrinhoRepository.save(any(CarrinhoModel.class))).thenReturn(mockCarrinho);

        // Chamada ao método
        carrinhoService.adicionarProduto(ID_USUARIO, ID_PRODUTO_A, quantidadeAdicional);

        // Asserções
        assertEquals(1, mockCarrinho.getItens().size(), "A lista de itens deve conter apenas 1 entrada.");
        assertEquals(quantidadeInicial + quantidadeAdicional, itemExistente.getQuantidade(), "A quantidade deve ser incrementada.");
        verify(carrinhoRepository, times(1)).save(mockCarrinho); 
    }
    
    // =========================================================================
    // Testes para Remover Produto
    // =========================================================================

    @Test
    void testRemoverProdutoComSucesso() {
        // Mock: Carrinho existente com dois itens
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        
        CarrinhoItemModel itemParaRemover = new CarrinhoItemModel();
        itemParaRemover.setProduto(mockProdutoA);
        itemParaRemover.setQuantidade(1);
        
        CarrinhoItemModel itemManter = new CarrinhoItemModel();
        itemManter.setProduto(mockProdutoB);
        itemManter.setQuantidade(2);
        
        List<CarrinhoItemModel> itens = new ArrayList<>(List.of(itemParaRemover, itemManter));
        mockCarrinho.setItens(itens); 

        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));
        when(carrinhoRepository.save(any(CarrinhoModel.class))).thenReturn(mockCarrinho);

        // Verifica o estado inicial
        assertEquals(2, mockCarrinho.getItens().size());

        // Chamada ao método
        carrinhoService.removerProduto(ID_USUARIO, ID_PRODUTO_A);

        // Asserções
        assertEquals(1, mockCarrinho.getItens().size(), "Deve restar apenas um item após a remoção.");
        assertTrue(mockCarrinho.getItens().stream().noneMatch(i -> i.getProduto().getIdProduto().equals(ID_PRODUTO_A)), 
                "O produto A não deve mais estar no carrinho.");
        verify(carrinhoRepository, times(1)).save(mockCarrinho);
    }
    
    @Test
    void testRemoverProdutoNaoExistenteLancaExcecao() {
        // Mock: Carrinho existente sem o produto alvo
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        mockCarrinho.setItens(new ArrayList<>()); 
        
        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));

        // Asserções
        assertThrows(RuntimeException.class, () -> {
            carrinhoService.removerProduto(ID_USUARIO, 999L); // ID de produto inexistente
        }, "Deve lançar RuntimeException se o produto não estiver no carrinho.");
        
        // Verifica se o save nunca foi chamado
        verify(carrinhoRepository, never()).save(any(CarrinhoModel.class));
    }
    
    // =========================================================================
    // Testes para Finalizar Compra
    // =========================================================================

    @Test
    void testFinalizarCompraComSucesso() {
        // Setup do Carrinho (2 itens, total = 2*10.0 + 3*50.0 = 20.0 + 150.0 = 170.0)
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        
        CarrinhoItemModel item1 = new CarrinhoItemModel();
        item1.setProduto(mockProdutoA);
        item1.setQuantidade(2);
        
        CarrinhoItemModel item2 = new CarrinhoItemModel();
        item2.setProduto(mockProdutoB);
        item2.setQuantidade(3);
        
        List<CarrinhoItemModel> itens = new ArrayList<>(List.of(item1, item2));
        mockCarrinho.setItens(itens);

        // Mock para encontrar o carrinho
        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));
        
        // Mock para salvar o Pedido
        when(pedidoRepository.save(any())).thenAnswer(invocation -> {
            PedidoModel pedido = invocation.getArgument(0);
            pedido.setIdPedido(100L); // Simula o ID gerado
            return pedido;
        });
        
        // Mock para salvar o Carrinho limpo
        when(carrinhoRepository.save(mockCarrinho)).thenReturn(mockCarrinho);

        // Chamada ao método
        PedidoResponseDTO response = carrinhoService.finalizarCompra(ID_USUARIO);

        // 5. Asserções
        assertNotNull(response);
        assertEquals(100L, response.getIdPedido());
        assertEquals(170.0, response.getTotal(), 0.001, "O total do pedido deve ser calculado corretamente.");
        assertEquals(2, response.getItens().size(), "O pedido deve ter 2 itens.");
        
        // Verifica se o Pedido foi salvo
        verify(pedidoRepository, times(1)).save(any(PedidoModel.class));
        
        // Verifica se o Carrinho foi limpo e salvo novamente
        assertTrue(mockCarrinho.getItens().isEmpty(), "O carrinho deve ser limpo.");
        verify(carrinhoRepository, times(1)).save(mockCarrinho);
    }
    
    @Test
    void testFinalizarCompraComCarrinhoVazioLancaExcecao() {
        // Setup do Carrinho (vazio)
        CarrinhoModel mockCarrinho = new CarrinhoModel();
        mockCarrinho.setUsuario(mockUsuario);
        mockCarrinho.setItens(new ArrayList<>()); 

        // Mock para encontrar o carrinho
        when(carrinhoRepository.findByUsuarioIdUsuario(ID_USUARIO)).thenReturn(Optional.of(mockCarrinho));

        // Asserções
        assertThrows(RuntimeException.class, () -> {
            carrinhoService.finalizarCompra(ID_USUARIO);
        }, "Deve lançar RuntimeException se o carrinho estiver vazio.");
        
        // Verifica se o Pedido nunca foi salvo
        verify(pedidoRepository, never()).save(any());
        // Verifica se o Carrinho não foi salvo
        verify(carrinhoRepository, never()).save(mockCarrinho);
    }
}