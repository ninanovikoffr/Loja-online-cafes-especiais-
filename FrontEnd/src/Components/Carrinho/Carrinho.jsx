import React from 'react';
import './Carrinho.css';
import carrinhoCarrinho from "../../assets/iconecarrinhopopup.svg";
import codigodebarras from "../../assets/codigodebarras.svg";
import cartao from "../../assets/cartao.svg";
import linha from "../../assets/linha.svg";
import pix from "../../assets/logopix.svg";

function Carrinho({ open, onClose, items = [] }){
    if(!open) return null;

    // simple totals
    const total = items.reduce((s,i) => s + (i.preco || 0), 0).toFixed(2);

    return (
        <div className="popupcarrinho" onClick={onClose}>
            <div className="popupcar" onClick={(e)=>e.stopPropagation()}>
                <button className="xiscarrinho" onClick={onClose}>×</button>

                <div className="cabecalhocar">
                    <img src={carrinhoCarrinho}/>
                    <p>Seu Carrinho</p>
                </div>

                <div className="quadradocar">
                    <div className="quadradoesquerda">
                        <div className="itenscarrinho">
                            {items.length === 0 ? (
                                <p className="carvazio">Seu carrinho está vazio.</p>
                            ) : (
                                items.map((it, idx) => (
                                    <div className="umitemcar" key={idx}>
                                        <img src={it.img} alt={it.nome} />
                                        <div className="informacaoitem">
                                            <div className="nomeitem">{it.nome}</div>
                                            <div className="descricaoitem">{it.descricao}</div>
                                        </div>
                                        <div className="precoitem">{(it.preco||0).toFixed(2)}</div>
                                    </div>
                                ))
                            )}
                        </div>
                        <div>
                            <p className="fretecar">Frete: 5,00</p>

                        </div>
                        <div className="descontoscar">
                            <p>Descontos: 0,00</p>

                        </div>
                        <div className="linhacar"><img src={linha}/></div>
                        <div className="somas">
                            <div className="totalpedido"><span>Total do Pedido:</span><span className="total">R$ {total}</span></div>
                        </div>
                    </div>
                    <div className="quadradodireita">
                        <p className="formapagamento">Forma de pagamento</p>
                        <div className="pix">
                            <p>Pix</p>
                            <img src={pix}/>
                        </div>
                        <div className="cartao">
                            <p>Cartão</p>
                            <img src={cartao}/>
                        </div>
                        <div className="boleto">
                            <p>Boleto</p>
                            <img src={codigodebarras}/>
                        </div>
                        <div className="endereçoentrega">

                        </div>
                        <button className="finalizar">Finalizar Pedido</button>

                    </div>
                </div>
            </div>
        </div>
    )
}

export default Carrinho;
