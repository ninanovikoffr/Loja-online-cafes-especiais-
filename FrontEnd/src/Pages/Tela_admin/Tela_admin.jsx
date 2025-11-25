import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaTrash, FaEdit } from 'react-icons/fa'; 
import { FaArrowRight } from 'react-icons/fa';
import { FaPlusCircle } from 'react-icons/fa';
import { Navbar } from '../../Components/Navbar/Navbar';

import perfil_admin from "../../assets/perfil_icon.svg"; 

import "./Tela_admin.css";

const produtosMock = [
    { id: 1, nome: 'Café Gourmet Baunilha' },
    { id: 2, nome: 'Café Premium Torra Média' },
    { id: 3, nome: 'Café 100% Arábica' },
    { id: 4, nome: 'Café Orgânico' },
    { id: 5, nome: 'Café Extra Forte' },
    { id: 6, nome: 'Café Moído na Hora' },
    { id: 7, nome: 'Grãos Premium' },
    { id: 8, nome: 'Café Descafeinado' },
    { id: 9, nome: 'Café Espresso' },
    { id: 10, nome: 'Café Blend Especial' },
    { id: 11, nome: 'Café com Chocolate' },
    { id: 12, nome: 'Café com Baunilha' },
    { id: 13, nome: 'Café Moído Tradicional' },
    { id: 14, nome: 'Café Premium com Canela' },
    { id: 15, nome: 'Café Orgânico com Açúcar Mascavo' },
    { id: 16, nome: 'Café Moído para Prensa Francesa' },
    { id: 17, nome: 'Grãos de Café para Moedor' },
    { id: 18, nome: 'Café Aromatizado com Amêndoas' },
    { id: 19, nome: 'Café 100% Robusta' },
    { id: 20, nome: 'Café Arábica Gourmet' },
    { id: 21, nome: 'Kit Café com 3 Sabores' },
    { id: 22, nome: 'Kit Café Completo para Espresso' },
    { id: 23, nome: 'Kit Degustação de Café Orgânico' },
    { id: 24, nome: 'Cápsulas de Café Espresso (10 unidades)' },
    { id: 25, nome: 'Cápsulas de Café Arábica' }
];


const pedidosMock = [
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
    
    return (
        <div className="tela_admin">
            <Navbar /> 

            {/* Cabeçalho da página Admin */}
            <div className="tituloadmin">
                <p className="olaAdmin">Olá Admin!</p>
                <button className="sairbotao">
                    Sair <FaArrowRight />
                </button>
            </div>

            <div className="colunas">
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Adicionar Produto</div>
                        <hr class="linhaHorizontal"/>
                        <form>
                            <label>Nome</label>
                            <input type="text"/>

                            <label>Descrição</label>
                            <textarea/>

                            
                            <label>Imagem</label>
                            <input type="file" accept="image/*"/>
                            <label className="botaoImagem"><FaPlusCircle />Escolher Arquivo</label>
                            <span id="fileName">Nenhum arquivo escolhido</span>
                        </form>



                    </div>
                </div>
                

                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Adicionar Produto</div>
                        <hr class="linhaHorizontal"/>
                        
                    </div>

                    <div className='secaoAdmin'>
                        <div>Adicionar Produto</div>
                        <hr class="linhaHorizontal"/>
                        
                    </div>
                </div>
                
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Adicionar Produto</div>
                        <hr class="linhaHorizontal"/>


                    </div>
                </div>


            </div>

                


        </div>
                    
    );
}

export default Tela_admin;