import React from 'react';
import './Carrinho.css';
import carrinhoCarrinho from "../../assets/iconecarrinhopopup.svg";
import codigodebarras from "../../assets/codigodebarras.svg";
import cartao from "../../assets/cartao.svg";
import linha from "../../assets/linha.svg";
import pix from "../../assets/logopix.svg";
import {FaTrash } from 'react-icons/fa';

function Carrinho({ open, onClose, items = [], onUpdateItem, onRemoveItem }) {

    if (!open) return null;

    const subtotal = items.reduce(
    (s, item) => s + item.preco * (item.quantidade || 1), 0 );

    const total = (subtotal + 5).toFixed(2); // +5 = frete fixo


    return (
        <div className="popupcarrinho" onClick={onClose}>
            <div className="popupcar" onClick={(e) => e.stopPropagation()}>

                <button className="xiscarrinho" onClick={onClose}>×</button>

                <div className="cabecalhocar">
                    <img src={carrinhoCarrinho} />
                    <p>Seu Carrinho</p>
                </div>

                <div className="quadradocar">

                    <div className="quadradoesquerda">

                        {items.map((it, idx) => (
                            <div className="umitemcar" key={idx}>

                                {/* IMAGEM */}
                                <img src={it.img} alt={it.nome} />

                                {/* INFORMAÇÕES */}
                                <div className="informacaoitem">
                                    <div className="nomeitem">{it.nome}</div>
                                    <div className="descricaoitem">{it.descricao}</div>

                                    {/* QUANTIDADE */}
                                    <div className="quantidade-container">
                                        <button
                                            className="quant-btn"
                                            onClick={() => onUpdateItem(idx, Math.max(1, (it.quantidade || 1) - 1))}
                                        >
                                            –
                                        </button>

                                        <span className="quantidade">
                                            {it.quantidade || 1}
                                        </span>

                                        <button
                                            className="quant-btn"
                                            onClick={() => onUpdateItem(idx, (it.quantidade || 1) + 1)}
                                        >
                                            +
                                        </button>
                                    </div>
                                </div>

                                {/* PREÇO DO ITEM × QUANTIDADE */}
                                <div className="precoitem">
                                    R$ {(it.preco * (it.quantidade || 1)).toFixed(2)}
                                </div>

                                {/* REMOVER ITEM */}
                                <button
                                    className="btn-remove"
                                    onClick={() => onRemoveItem(idx)}
                                >
                                    <FaTrash />
                                </button>
                            </div>
                        ))}


                        <div>
                            <p className="fretecar">Frete: 5,00</p>
                        </div>

                        <div className="descontoscar">
                            <p>Descontos: 0,00</p>
                        </div>

                        <div className="linhacar">
                            <img src={linha} />
                        </div>

                        <div className="somas">
                            <div className="totalpedido">
                                <span>Total do Pedido:</span>
                                <span className="total">R$ {total}</span>
                            </div>
                        </div>
                    </div>

                    <div className="quadradodireita">

                        <p className="formapagamento">Forma de pagamento</p>

                        <button className="pix">
                            <p>Pix</p>
                            <img src={pix} />
                        </button>

                        <button className="cartao">
                            <p>Cartão</p>
                            <img src={cartao} />
                        </button>

                        <div className="boleto">
                            <p>Boleto</p>
                            <img src={codigodebarras} />
                        </div>

                        <button className="finalizar">Finalizar Pedido</button>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default Carrinho;
