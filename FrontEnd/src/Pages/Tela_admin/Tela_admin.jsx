import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaTrash, FaEdit } from 'react-icons/fa'; 
import { FaArrowRight } from 'react-icons/fa'; 
import { Navbar } from '../../Components/Navbar/Navbar';

import perfil_admin from "../../assets/perfil_icon.svg"; 

import "./Tela_admin.css";

// Dados simulados (Mock Data)
const produtosMock = [
    // Mais itens adicionados para simular a necessidade de scroll
    { id: 1, nome: 'Café 100% Arábica' },
    { id: 2, nome: 'Café 100% Arábica' },
    { id: 3, nome: 'Café 100% Arábica' },
    { id: 4, nome: 'Café 100% Arábica' },
    { id: 5, nome: 'Café 100% Arábica' },
    { id: 6, nome: 'Café 100% Arábica' },
    { id: 7, nome: 'Café Extra Forte' },
    { id: 8, nome: 'Grãos Premium' },
    { id: 9, nome: 'Café Descafeinado' },
    { id: 10, nome: 'Moído na Hora' },
];

const pedidosMock = [
    // Mais itens adicionados para simular a necessidade de scroll
    { id: '123456', status: 'Aberto' },
    { id: '123457', status: 'Aberto' },
    { id: '123458', status: 'Aberto' },
    { id: '123459', status: 'Finalizado' },
    { id: '123460', status: 'Finalizado' },
    { id: '123461', status: 'Finalizado' },
    { id: '123462', status: 'Finalizado' },
    { id: '123463', status: 'Finalizado' },
    { id: '123464', status: 'Aberto' },
];


function Tela_admin() {
    const renderInput = (label, className, type = "text", placeholder = "") => (
        <div className={className}>
            <p>{label}</p>
            <input className="inputprod" type={type} placeholder={placeholder} />
        </div>
    );

    return (
        <div className="tela_admin">
            <Navbar /> 

            {/* Cabeçalho da página Admin */}
            <div className="tituloadmin">
                <p className="olaAdmin">Olá Admin</p>
                {/* O ícone FaArrowRight foi movido para dentro do botão, conforme o protótipo */}
                <button className="sairbotao">
                    Sair <FaArrowRight />
                </button>
            </div>

            {/* Conteúdo principal com as três colunas */}
            <div className="quadradogrande">

                {/* 1. Adicionar Produto (Primeira Coluna) */}
                <div className="addproduto">
                    <p className="titulo_coluna">Adicionar produto</p>
                    <div className="inputsaddprod">
                        {renderInput("Nome", "nomeaddprod")}
                        {renderInput("Descrição", "descricaddprod")}
                        {renderInput("Imagem", "imgaddprod", "file")} 
                        {renderInput("Categoria", "categaddprod")}
                        {renderInput("Preço", "precoaddprod")}
                    </div>
                    <button className="btn_salvar_produto">
                        Salvar
                    </button>
                </div>

                {/* 2. Produtos e Pedidos (Segunda Coluna) */}
                <div className="segundacoluna">
                    <div className="produtoslista">
                        <p className="titulo_coluna_interna">Produtos</p>
                        {/* Scroll Container para a lista de produtos */}
                        <div className="scroll_container">
                            {produtosMock.map((produto) => (
                                <div key={produto.id} className="produto_item">
                                    <div className="produto_name">{produto.nome}</div>
                                    <div className="produto_icons">
                                        <FaEye className="icon" />
                                        <FaEdit className="icon" />
                                        <FaTrash className="icon trash_icon" />
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="pedidoslista">
                        <p className="titulo_coluna_interna">Pedidos</p>
                        {/* Scroll Container para a lista de pedidos */}
                        <div className="scroll_container">
                            {pedidosMock.map((pedido, index) => (
                                <div key={index} className={`pedido_item ${pedido.status.toLowerCase()}`}>
                                    <div className="pedido_id">{pedido.id}</div>
                                    <div className="pedido_status">{pedido.status}</div>
                                    <div className="pedido_icons">
                                        <FaEye className="icon" />
                                        <FaTrash className="icon trash_icon" />
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* 3. Editar Perfil (Terceira Coluna) */}
                <div className="editarPerfil">
                    <p className="titulo_coluna">Editar perfil</p>
                    
                    <div className="perfil_icon_container">
                        <img src={perfil_admin} alt="Admin Profile" className="perfil_admin_img" /> 
                    </div>

                    {renderInput("Nome", "nomeeditprof")}
                    <div className="email_editprof">
                         <p>E-mail</p>
                         <input className="inputprod" type="email" />
                         <button className="btn_redefinir_senha">redefinir senha</button>
                    </div>
                    
                    <p className="endereco_titulo">Endereços</p>
                    <div className="endereco_container">
                        <div className="endereco_item_header">
                            <div className="endereco_title">Casa</div>
                            <FaEdit className="icon edit_endereco_icon" /> 
                        </div>
                        <textarea className="endereco_text" defaultValue="Rua tal tal tal número tal - bairro tal tal tal, apto tal"></textarea>
                    </div>

                    <button className="btn_salvar_perfil">
                        Salvar
                    </button>
                    
                    <div className="deletar_conta_container">
                        <button className="btn_deletar_conta">
                            deletar conta
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Tela_admin;