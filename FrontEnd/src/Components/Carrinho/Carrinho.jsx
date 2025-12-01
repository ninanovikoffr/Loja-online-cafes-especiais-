import React, { useState, useEffect } from 'react';
import './Carrinho.css';

import carrinhoCarrinho from "../../assets/iconecarrinhopopup.svg";
import codigodebarras from "../../assets/codigodebarras.svg";
import cartao from "../../assets/cartao.svg";
import linha from "../../assets/linha.svg";
import pix from "../../assets/logopix.svg";
import { FaTrash } from 'react-icons/fa';
import axios from "axios";

// Catálogo local dos produtos
const CATALOGO_PRODUTOS = {
    1: {
        nome: 'Café 100% Arábica',
        descricao: '250 g - moído',
        preco: 39.90,
        img: '/src/assets/cafe_arabica.svg'
    },
    2: {
        nome: 'Café Premium Torra Média',
        descricao: '250 g - moído',
        preco: 39.90,
        img: '/src/assets/cafe_torra.svg'
    },
    3: {
        nome: 'Café Gourmet Baunilha',
        descricao: '250 g - moído',
        preco: 39.90,
        img: '/src/assets/cafe_baunilha.svg'
    }
};

function Carrinho({ open, onClose }) {

    const [items, setItems] = useState([]);
    const [metodo, setMetodo] = useState("pix");

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
    }, [open]);


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
        try {
            await axios.post(
                `http://localhost:8080/carrinhos/${idUsuario}/finalizar`
            );

            alert("Pedido finalizado com sucesso!");
            onClose();
        } catch (err) {
            console.error("Erro ao finalizar:", err.response || err);
            alert("Erro ao finalizar pedido (verifique se seu usuário tem permissão e se o token é válido).");
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

                <button className="xiscarrinho" onClick={onClose}>×</button>

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
                                                –
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
                            <img src={pix} alt="Pix" />
                        </button>

                        <button
                            className={`cartao ${metodo === "cartao" ? "selected" : ""}`}
                            onClick={() => setMetodo("cartao")}
                        >
                            <p>Cartão</p>
                            <img src={cartao} alt="Cartão" />
                        </button>

                        <button
                            className={`boleto ${metodo === "boleto" ? "selected" : ""}`}
                            onClick={() => setMetodo("boleto")}
                        >
                            <p>Boleto</p>
                            <img src={codigodebarras} alt="Boleto" />
                        </button>

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
