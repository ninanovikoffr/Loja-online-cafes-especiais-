import React from 'react';
import './Carrinho.css';

export default function Carrinho({ open, onClose, items = [] }){
    if(!open) return null;

    // simple totals
    const total = items.reduce((s,i) => s + (i.preco || 0), 0).toFixed(2);

    return (
        <div className="cart-overlay" onClick={onClose}>
            <div className="cart-modal" onClick={(e)=>e.stopPropagation()}>
                <button className="cart-close" onClick={onClose}>×</button>

                <div className="cart-header">
                    <h2>Seu Carrinho</h2>
                </div>

                <div className="cart-body">
                    <div className="cart-items">
                        {items.length === 0 ? (
                            <p className="empty">Seu carrinho está vazio.</p>
                        ) : (
                            items.map((it, idx) => (
                                <div className="cart-item" key={idx}>
                                    <img src={it.img} alt={it.nome} />
                                    <div className="item-info">
                                        <div className="item-name">{it.nome}</div>
                                        <div className="item-desc">{it.descricao}</div>
                                    </div>
                                    <div className="item-price">{(it.preco||0).toFixed(2)}</div>
                                </div>
                            ))
                        )}
                    </div>

                    <div className="cart-summary">
                        <div className="summary-row"><span>Total do Pedido:</span><span>R$ {total}</span></div>
                        <button className="finalizar">Finalizar Pedido</button>
                    </div>
                </div>
            </div>
        </div>
    )
}
