import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaTrash, FaEdit } from 'react-icons/fa'; 
import { FaArrowRight } from 'react-icons/fa';
import { FaPlusCircle } from 'react-icons/fa';
import { Navbar } from '../../Components/Navbar/Navbar';
import axios from 'axios';
import Notification from './Notification';
import perfil_admin from "../../assets/Foto_admin.svg";
import "./Tela_admin.css";

const handleSubmit = (e) => {
    e.preventDefault();

    const produto = {
        nome,
        descricao,
        categoria,
        preco: preco.replace('R$', '').replace(',', '.'), // Convertendo preço para formato numérico
        imagem: imagem ? imagem.name : null, // Enviar o nome do arquivo ou URL da imagem
    };

    axios.post('http://localhost:8080/produtos', produto)
        .then((response) => {
            console.log('Produto criado com sucesso:', response.data);
            setNotification({
                message: 'Produto criado com sucesso!',
                type: 'success',
            });
        })
        .catch((error) => {
            console.error('Erro ao criar produto:', error);
            setNotification({
                message: 'Erro ao criar o produto!',
                type: 'error',
            });
        });
};

const [nome, setNome] = useState('');
const [descricao, setDescricao] = useState('');
const [categoria, setCategoria] = useState('');
const [imagem, setImagem] = useState(null); // Para armazenar a imagem
const [preco, setPreco] = useState('');

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

    const [preco, setPreco] = useState('');

    // Função para formatar o preço
    const formatarPreco = (value) => {
        let valor = value.replace(/\D/g, '');
        if (valor.length > 2) {
            valor = 'R$' + valor.slice(0, valor.length - 2) + ',' + valor.slice(valor.length - 2);
        } else {
            valor = 'R$' + valor;
        }
        return valor;
    };

    const handlePrecoChange = (e) => {
        const formattedValue = formatarPreco(e.target.value);
        setPreco(formattedValue);
    };
    
    return (
        <div className="tela_admin">
            <Navbar /> 

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
                        <form onSubmit={handleSubmit}>
                            <label>Nome</label>
                            <input type="text" value={nome} onChange={(e) => setNome(e.target.value)}/>

                            <label>Descrição</label>
                            <textarea value={descricao} onChange={(e) => setDescricao(e.target.value)}/>

                           <form>
                                <label>Imagem</label>
                                <input type="file" id="imagem" accept="image/*" onChange={(e) => setImagem(e.target.files[0])}/>
                                <label htmlFor="imagem" className="botaoImagem">
                                    <FaPlusCircle /> Escolher Arquivo
                                </label>
                                <span id="fileName">{imagem ? imagem.name : 'Nenhum arquivo escolhido'}</span>
                            </form>

                            <label for="opcao">Categoria</label>
                            <select id="opcao" name="opcao" value={categoria} onChange={(e) => setCategoria(e.target.value)}>
                                <option value="opcao1">Cafés especiais</option>
                                <option value="opcao2">Kits de café</option>
                                <option value="opcao3">Cápsulas de café</option>
                            </select>

                            <label for="preco">Preço</label>
                            <input 
                                type="text" 
                                id="preco" 
                                name="preco" 
                                placeholder="R$0,00" 
                                value={preco}
                                onChange={handlePrecoChange}
                                maxlength="10"
                            />
                        </form>
                        <button className='botaoSalvar' type="submit"  style={{marginBottom: '-10px'}}>Salvar</button>

                    </div>
                </div>
                
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Produtos</div>
                        <hr class="linhaHorizontal"/>
                        <div className="listas">
                            {produtosMock.map(produto => (
                            <div key={produto.id} className="itemLista">
                                <span>{produto.nome}</span>
                                <div className="acoes">
                                <FaEye />
                                <FaEdit />
                                <FaTrash />
                                </div>
                            </div>
                            ))}
                        </div>
                    </div>

                    <div className='secaoAdmin'>
                        <div>Pedidos</div>
                        <hr class="linhaHorizontal"/>
                        <div className="listas">
                            {pedidosMock.map(pedido => (
                            <div key={pedido.id} className="itemLista">
                                <span>{pedido.id}</span>
                                <span>{pedido.status}</span>
                                <div className="acoes">
                                <FaEye />
                                <FaEdit />
                                <FaTrash />
                                </div>
                            </div>
                            ))}
                        </div>  
                        
                    </div>
                </div>
                
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Editar Perfil</div>
                        <hr class="linhaHorizontal"/>
                        <img className="perfilImagem" src={perfil_admin}/>
                        <form>
                            <label>Nome</label>
                            <input type="text"/>

                            <label>E-mail</label>
                            <input type="email"/>
                            <a className= "links" href="redefinir_senha">Redefinir senha</a>

                            <label>Endereços</label>
                            <textarea/>

                            <button className='botaoSalvar'>Salvar</button>
                            <a className= "links" style={{textAlign: 'center'}} href="redefinir_senha">Deletar conta</a>                           
                        </form>


                    </div>
                </div>


            </div>

                


        </div>
                    
    );
}

export default Tela_admin;