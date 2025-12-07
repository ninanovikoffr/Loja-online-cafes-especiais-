import React, { useState, useEffect } from 'react';
import './Carrinho.css';

import carrinhoCarrinho from "../../assets/iconecarrinhopopup.svg";
import codigodebarras from "../../assets/codigodebarras.svg";
import cartao from "../../assets/cartao.svg";
import linha from "../../assets/linha.svg";
import pixImg from "../../assets/logopix.svg";
import { FaTrash } from 'react-icons/fa';
import axios from "axios";

// Catalogo local dos produtos
const CATALOGO_PRODUTOS = {
    1: {
        nome: 'Cafe 100% Arabica',
        descricao: '250 g - moido',
        preco: 39.90,
        img: '/src/assets/cafe_arabica.svg'
    },
    2: {
        nome: 'Cafe Premium Torra Media',
        descricao: '250 g - moido',
        preco: 39.90,
        img: '/src/assets/cafe_torra.svg'
    },
    3: {
        nome: 'Cafe Gourmet Baunilha',
        descricao: '250 g - moido',
        preco: 39.90,
        img: '/src/assets/cafe_baunilha.svg'
    }
};

function Carrinho({ open, onClose }) {

    const [items, setItems] = useState([]);
    const [metodo, setMetodo] = useState("pix");
    const [addresses, setAddresses] = useState([]);
    const [selectedAddressId, setSelectedAddressId] = useState(null);

    const idUsuario = localStorage.getItem("idUsuario");
    const token = localStorage.getItem("token");

    if (token) {
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }

    // ============================================
    // LISTAR ITENS DO CARRINHO
    // ============================================
    const carregarItens = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/carrinhos/${idUsuario}/produtos`
            );

            const itensBrutos = response.data || [];

            const itensComInfo = itensBrutos.map((it) => {
                const prod = CATALOGO_PRODUTOS[it.idProduto] || {};

                return {
                    idCarrinhoItem: it.idCarrinhoItem,
                    idProduto: it.idProduto,
                    quantidade: it.quantidade,
                    nome: prod.nome || `Produto ${it.idProduto}`,
                    descricao: prod.descricao || '',
                    preco: prod.preco || 0,
                    img: prod.img || '',
                };
            });

            setItems(itensComInfo);
        } catch (err) {
            console.error("Erro ao carregar itens:", err);
        }
    };

    useEffect(() => {
        if (open) carregarItens();
        if (open) carregarEnderecos();
    }, [open]);

    // Gera um id consistente para o endereco, mesmo se backend usar chaves diferentes
    const resolveAddressId = (addr, fallback) => addr.idEndereco ?? addr.id ?? fallback;

    // ============================================
    // LISTAR ENDERECOS DO USUARIO (FRONTEND)
    // ============================================
    const carregarEnderecos = async () => {
        try {
            const resp = await axios.get(`http://localhost:8080/enderecos/listar/${idUsuario}`);
            const dados = resp.data || [];
            setAddresses(dados);

            // Mantem selecao se endereco ainda existir
            if (dados.length && selectedAddressId !== null) {
                const exists = dados.some((d, index) => resolveAddressId(d, index) === selectedAddressId);
                if (!exists) setSelectedAddressId(null);
            }
        } catch (err) {
            console.warn('Nao foi possivel carregar enderecos do backend:', err.message || err);
            setAddresses([]);
        }
    };

    // ============================================
    // ADICIONAR +1 UNIDADE
    // ============================================
    const incrementarQtd = async (item) => {
        try {
            await axios.post(
                `http://localhost:8080/carrinhos/${idUsuario}/produtos/${item.idProduto}`
            );
            carregarItens();
        } catch (err) {
            console.error("Erro ao aumentar quantidade:", err);
        }
    };


    const decrementarQtd = async (item) => {
        try {
            await axios.delete(
                `http://localhost:8080/carrinhos/${idUsuario}/produtos/${item.idProduto}`
            );
            carregarItens();
        } catch (err) {
            console.error("Erro ao diminuir quantidade:", err);
        }
    };

    // ============================================
    // REMOVER PRODUTO DO CARRINHO 
    // ============================================
    const removerItem = async (idProduto) => {
        try {
            await axios.delete(
                `http://localhost:8080/carrinhos/${idUsuario}/produtos/${idProduto}`
            );
            carregarItens();
        } catch (err) {
            console.error("Erro ao remover item:", err);
        }
    };


    // ============================================
    // FINALIZAR COMPRA
    // ============================================
    const finalizarCompra = async () => {
        if (selectedAddressId === null) {
            alert("Por favor, selecione um endereco antes de finalizar o pedido.");
            return;
        }

        try {
            const pedidoData = {
                enderecoId: selectedAddressId,
            };

            await axios.post(
                `http://localhost:8080/carrinhos/${idUsuario}/finalizar`,
                pedidoData
            );

            alert("Pedido finalizado com sucesso!");
            onClose();
        } catch (err) {
            console.error("Erro ao finalizar:", err.response || err);
            alert("Erro ao finalizar pedido. Tente novamente mais tarde.");
        }
    };


    if (!open) return null;

    const subtotal = items.reduce(
        (s, it) => s + (Number(it.preco) || 0) * (Number(it.quantidade) || 0),
        0
    );

    const total = (subtotal + 5).toFixed(2); // frete fixo 5,00

    return (
        <div className="popupcarrinho" onClick={onClose}>
            <div className="popupcar" onClick={(e) => e.stopPropagation()}>

                <button className="xiscarrinho" onClick={onClose}>X</button>

                <div className="cabecalhocar">
                    <img src={carrinhoCarrinho} alt="Carrinho" />
                    <p>Seu Carrinho</p>
                </div>

                <div className="quadradocar">

                    {/* ESQUERDA */}
                    <div className="quadradoesquerda">

                        <div className="itenscarrinho">
                            {items.map((it) => (
                                <div
                                    className="umitemcar"
                                    key={it.idCarrinhoItem ?? `${it.idProduto}-${it.quantidade}`}
                                >

                                    {it.img && (
                                        <img src={it.img} alt={it.nome} />
                                    )}

                                    <div className="informacaoitem">
                                        <div className="nomeitem">{it.nome}</div>
                                        <div className="descricaoitem">{it.descricao}</div>

                                        <div className="quantidade-container">
                                            <button
                                                className="quant-btn"
                                                onClick={() => decrementarQtd(it)}
                                            >
                                                -
                                            </button>

                                            <span className="quantidade">
                                                {it.quantidade}
                                            </span>

                                            <button
                                                className="quant-btn"
                                                onClick={() => incrementarQtd(it)}
                                            >
                                                +
                                            </button>
                                        </div>
                                    </div>

                                    <div className="precoitem">
                                        R$ {(Number(it.preco) * Number(it.quantidade)).toFixed(2)}
                                    </div>

                                    <button
                                        className="btn-remove"
                                        onClick={() => removerItem(it.idProduto)}
                                    >
                                        <FaTrash />
                                    </button>
                                </div>
                            ))}
                        </div>

                        <p className="fretecar">Frete: 5,00</p>

                        <div className="descontoscar">
                            <p>Descontos: 0,00</p>
                        </div>

                        <div className="linhacar">
                            <img src={linha} alt="Linha" />
                        </div>

                        <div className="somas">
                            <div className="totalpedido">
                                <span>Total do Pedido:</span>
                                <span className="total">R$ {total}</span>
                            </div>
                        </div>
                    </div>


                    {/* DIREITA */}
                    <div className="quadradodireita">

                        <p className="formapagamento">Forma de pagamento</p>

                        <button
                            className={`pix ${metodo === "pix" ? "selected" : ""}`}
                            onClick={() => setMetodo("pix")}
                        >
                            <p>Pix</p>
                            <img src={pixImg} alt="Pix" />
                        </button>

                        <button
                            className={`cartao ${metodo === "cartao" ? "selected" : ""}`}
                            onClick={() => setMetodo("cartao")}
                        >
                            <p>Cartao</p>
                            <img src={cartao} alt="Cartao" />
                        </button>

                        <button
                            className={`boleto ${metodo === "boleto" ? "selected" : ""}`}
                            onClick={() => setMetodo("boleto")}
                        >
                            <p>Boleto</p>
                            <img src={codigodebarras} alt="Boleto" />
                        </button>

                        <div>
                            <p className="enderecos-title">Endereco de entrega</p>

                            <div className="enderecos-list">
                                {addresses.length === 0 && (
                                    <div className="endereco-empty">Nenhum endereco cadastrado.</div>
                                )}
                                {addresses.map((addr, idx) => {
                                    const rua = addr.rua || addr.logradouro || addr.street || addr.endereco || addr.logradouroCompleto || '';
                                    const numero = addr.numero || addr.number || addr.n || '';
                                    const bairro = addr.bairro || addr.distrito || addr.bairro || '';
                                    const cidade = addr.cidade || addr.city || addr.localidade || '';
                                    const estado = addr.estado || addr.uf || '';
                                    const cep = addr.cep || addr.zip || addr.postalCode || '';

                                    const full = `${rua}${numero ? ', ' + numero : ''}${bairro ? ' - ' + bairro : ''}${cidade ? ' - ' + cidade : ''}${estado ? ' - ' + estado : ''}${cep ? ' - CEP: ' + cep : ''}`;

                                    const addrId = resolveAddressId(addr, idx);
                                    const keyId = addrId;
                                    const isSelected = selectedAddressId === addrId;

                                    return (
                                        <div
                                            key={keyId}
                                            className={`endereco-box ${isSelected ? 'selected' : ''}`}
                                            onClick={() => setSelectedAddressId(addrId)}
                                        >
                                            <div className="endereco-text">{full}</div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>


                        <button className="finalizar" onClick={finalizarCompra}>
                            Finalizar Pedido
                        </button>

                    </div>

                </div>
            </div>
        </div>
    );
}

export default Carrinho;
