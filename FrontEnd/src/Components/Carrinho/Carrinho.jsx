import React, { useState, useEffect } from 'react';
import './Carrinho.css';
import carrinhoCarrinho from "../../assets/iconecarrinhopopup.svg";
import codigodebarras from "../../assets/codigodebarras.svg";
import cartao from "../../assets/cartao.svg";
import linha from "../../assets/linha.svg";
import pix from "../../assets/logopix.svg";


function Carrinho({ open, onClose, items = [] }){
    const [addresses, setAddresses] = useState([]);
    const [selectedId, setSelectedId] = useState(null);

    useEffect(()=>{
        try{
            const raw = localStorage.getItem('enderecos_v1');
            if(raw) setAddresses(JSON.parse(raw));
        }catch(e){ console.warn('failed to load addresses', e) }
    },[]);

    const handleRemoveAddress = (addr) => {
        setAddresses(prev => {
            const next = prev.filter(a => a.id !== addr.id);
            try{ localStorage.setItem('enderecos_v1', JSON.stringify(next)); }catch(e){}
            return next;
        });
        if(selectedId === addr.id) setSelectedId(null);
    };

    const [showAddressModal, setShowAddressModal] = useState(false);
    const [modalAddress, setModalAddress] = useState(null);

    const handleEditAddress = (addr) => {
        setModalAddress({ ...addr });
        setShowAddressModal(true);
    };

    const handleAddAddress = () => {
        setModalAddress({ apelido:'', cep:'', rua:'', numero:'', complemento:'', bairro:'', cidade:'', estado:'' });
        setShowAddressModal(true);
    };

    const handleSaveAddress = (addr) => {
        if(!addr) return;
        setAddresses(prev => {
            let next;
            if(addr.id){
                next = prev.map(p => p.id === addr.id ? addr : p);
            } else {
                const novo = { ...addr, id: Date.now() };
                next = [novo, ...prev];
                addr = novo;
            }
            try{ localStorage.setItem('enderecos_v1', JSON.stringify(next)); }catch(e){}
            return next;
        });
        setSelectedId(addr.id);
        setShowAddressModal(false);
        setModalAddress(null);
    };

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
                        <button className="pix">
                            <p>Pix</p>
                            <img src={pix}/>
                        </button>
                        <button className="cartao">
                            <p>Cartão</p>
                            <img src={cartao}/>
                        </button>
                        <div className="boleto">
                            <p>Boleto</p>
                            <img src={codigodebarras}/>
                        </div>

                        <div className="enderecoentrega">
                            
                            <div className="address-list">
                                {addresses.map(addr => (
                                    <Quadendereco
                                        key={addr.id}
                                        address={addr}
                                        onEdit={handleEditAddress}
                                        onRemove={handleRemoveAddress}
                                        onSelect={(a)=>setSelectedId(a.id)}
                                        selected={selectedId === addr.id}
                                    />
                                ))}
                            </div>
                            <button type="button" className="btn-add-address" onClick={handleAddAddress}>Adicionar endereço</button>
                        </div>

                        <button className="finalizar">Finalizar Pedido</button>

                    </div>
                </div>
                {showAddressModal && (
                    <div className="addressModal" onClick={()=>{ setShowAddressModal(false); setModalAddress(null); }}>
                        <div className="addressModalBox" onClick={(e)=>e.stopPropagation()}>
                            <h3>{modalAddress && modalAddress.id ? 'Editar Endereço' : 'Adicionar Endereço'}</h3>
                            <form onSubmit={(e)=>{ e.preventDefault(); handleSaveAddress(modalAddress); }} className="addressModalForm">
                                <input placeholder="Apelido" value={modalAddress.apelido || ''} onChange={e=>setModalAddress({...modalAddress, apelido:e.target.value})} />
                                <input placeholder="CEP" value={modalAddress.cep || ''} onChange={e=>setModalAddress({...modalAddress, cep:e.target.value})} />
                                <input placeholder="Rua" value={modalAddress.rua || ''} onChange={e=>setModalAddress({...modalAddress, rua:e.target.value})} />
                                <input placeholder="Número" value={modalAddress.numero || ''} onChange={e=>setModalAddress({...modalAddress, numero:e.target.value})} />
                                <input placeholder="Complemento" value={modalAddress.complemento || ''} onChange={e=>setModalAddress({...modalAddress, complemento:e.target.value})} />
                                <input placeholder="Bairro" value={modalAddress.bairro || ''} onChange={e=>setModalAddress({...modalAddress, bairro:e.target.value})} />
                                <input placeholder="Cidade" value={modalAddress.cidade || ''} onChange={e=>setModalAddress({...modalAddress, cidade:e.target.value})} />
                                <input placeholder="Estado (UF)" value={modalAddress.estado || ''} onChange={e=>setModalAddress({...modalAddress, estado:e.target.value})} />
                                <div className="modalActions">
                                    <button type="button" onClick={()=>{ setShowAddressModal(false); setModalAddress(null); }}>Cancelar</button>
                                    <button type="submit">Salvar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default Carrinho;
