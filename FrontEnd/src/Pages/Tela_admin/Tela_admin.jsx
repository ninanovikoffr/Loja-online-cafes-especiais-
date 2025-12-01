import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaTrash, FaEdit, FaArrowRight, FaPlusCircle } from 'react-icons/fa';
import { Navbar } from '../../Components/Navbar/Navbar';
import axios from 'axios';
import perfil_admin from "../../assets/Foto_admin.svg";
import "./Tela_admin.css";

// Dados reais serão carregados do backend
// API base - AJUSTARRRR
axios.defaults.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
axios.interceptors.request.use(config => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});


const initialProdutos = [];
const initialPedidos = [];

function Tela_admin() {
    const [nome, setNome] = useState('');
    const [descricao, setDescricao] = useState('');
    const [imagem, setImagem] = useState(null);
    const [preco, setPreco] = useState('');
    const [produtos, setProdutos] = useState(initialProdutos);
    const [pedidos, setPedidos] = useState(initialPedidos);
    const [loadingProdutos, setLoadingProdutos] = useState(false);
    const [loadingPedidos, setLoadingPedidos] = useState(false);
    const [produtoEditando, setProdutoEditando] = useState(null);
    const [usuario, setUsuario] = useState(null);
    const [nomeUsuario, setNomeUsuario] = useState("");
    const [emailUsuario, setEmailUsuario] = useState("");
    const [enderecoUsuario, setEnderecoUsuario] = useState("");

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

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('nome', nome);
        formData.append('descricao', descricao);

        const precoNumero = preco.replace('R$', '').replace(',', '.');
        formData.append('preco', precoNumero);

        if (imagem) formData.append('imagem', imagem);

        try {
            if (produtoEditando) {
                await axios.put(`/produtos/${produtoEditando}`, formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                alert('Produto atualizado com sucesso!')
            } else {
                await axios.post('/produtos/criar', formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                alert('Produto criado com sucesso!');
            }

            setProdutoEditando(null);
            setNome('');
            setDescricao('');
            setCategoria('');
            setPreco('');
            setImagem(null);

            fetchProdutos();
        } catch (err) {
            console.error("Erro ao salvar produto:", err);
            alert('Erro ao salvar o produto!');
        }
    };

    // Busca produtos do backend
    const fetchProdutos = async () => {
        setLoadingProdutos(true);
        try {
            const res = await axios.get('/produtos');
            // assumes backend returns array in res.data
            setProdutos(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('Erro ao buscar produtos:', err);
            alert('Erro ao carregar produtos');
        } finally {
            setLoadingProdutos(false);
        }
    };

    // Preencher o formulário de produto ao clicar em editar
    const carregarProdutoParaEdicao = (produto) => {
        setProdutoEditando(produto.idProduto); 
        setNome(produto.nome);
        setDescricao(produto.descricao);
        
        let precoFormatado = "";
        if (produto.preco !== null && produto.preco !== undefined) {
            precoFormatado = "R$" + Number(produto.preco)
                .toFixed(2) // força 2 casas decimais
                .replace(".", ","); 
        }
        setPreco(precoFormatado);
        
        setImagem(null);
    };

    // Deletar produto no back
    const deletarProduto = async (idProduto) => {
        const confirmar = confirm("Tem certeza que deseja excluir este produto?");
        if (!confirmar) return;

        try {
            await axios.delete(`/produtos/${idProduto}`);

            alert("Produto deletado com sucesso!");
            fetchProdutos(); // recarrega a lista
        } catch (error) {
            console.error("Erro ao deletar produto:", error);
            alert("Erro ao deletar o produto.");
        }
    };

    // Busca pedidos do backend
    const fetchPedidos = async () => {
        setLoadingPedidos(true);
        try {
            const res = await axios.get('/pedidos');
            setPedidos(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('Erro ao buscar pedidos:', err);
            alert('Erro ao carregar pedidos!');
        } finally {
            setLoadingPedidos(false);
        }
    };

    // Deleta pedidos do backend
    const deletarPedido = async (idPedido) => {
        const confirmar = confirm("Tem certeza que deseja excluir este pedido?");
        if (!confirmar) return;

        try {
            await axios.delete(`/pedidos/${idPedido}`);
            alert("Pedido deletado com sucesso!");
            fetchPedidos();
        } catch (error) {
            console.error("Erro ao deletar pedido:", error);
            alert("Erro ao deletar o pedido.");
        }
    };

    //Busca o usuário autenticado
    const fetchUsuario = async () => {
        try {
            const token = localStorage.getItem("token");

            const res = await axios.get("/usuario/me", {
                headers: { Authorization: `Bearer ${token}` }
            });

            setUsuario(res.data);
            setNomeUsuario(res.data.nome);
            setEmailUsuario(res.data.email);
            setEnderecoUsuario(res.data.endereco);
        } catch (err) {
            console.error("Erro ao carregar usuário:", err);
            alert("Erro ao carregar dados do usuário.");
        }
    };

    //Atualiza os dados do admin no backend
    const atualizarUsuario = async () => {
        const token = localStorage.getItem("token");

        try {
            const body = {
                nome: nomeUsuario,
                email: emailUsuario,
                endereco: enderecoUsuario
            };

            await axios.patch("/usuario/me/update", body, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            alert("Dados atualizados com sucesso!");

            fetchUsuario(); // recarregar dados
        } catch (err) {
            console.error("Erro ao atualizar usuário:", err);
            alert("Erro ao atualizar usuário.");
        }
    };


    useEffect(() => {
        // Ler token e role do localStorage e configurar axios
        const token = localStorage.getItem('token');
        const roleRaw = localStorage.getItem('role');
        let role = null;
        try { role = roleRaw ? JSON.parse(roleRaw) : null; } catch (e) { role = roleRaw; }

        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }

        // Se não for admin, redireciona para a home (não permitir acessar a tela admin)
        const isAdmin = 
            role === 'ADMIN' ||
            role === 'admin' ||
            (Array.isArray(role) && (role.includes('ADMIN') || role.includes('admin')));

        if (!isAdmin) {
            // se não tem token/role admin, redireciona
            // evite executar fetches protegidos
            window.alert('Acesso negado: perfil não é administrador.');
            // usamos window.location para garantir redirecionamento mesmo fora do router
            window.location.href = '/';
            return;
        }

        fetchProdutos();
        fetchPedidos();
        fetchUsuario();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

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
                        <hr className="linhaHorizontal"/>
                        <form onSubmit={handleSubmit}>
                            <label>Nome</label>
                            <input type="text" value={nome} onChange={(e) => setNome(e.target.value)}/>

                            <label>Descrição</label>
                            <textarea value={descricao} onChange={(e) => setDescricao(e.target.value)}/>

                            <div>
                                <label>Imagem</label>
                                <input type="file" id="imagem" accept="image/*" onChange={(e) => setImagem(e.target.files[0])}/>
                                <label htmlFor="imagem" className="botaoImagem">
                                    <FaPlusCircle /> Escolher Arquivo
                                </label>
                                <span id="fileName">{imagem ? imagem.name : 'Nenhum arquivo escolhido'}</span>
                            </div>

                            <label htmlFor="preco">Preço</label>
                            <input 
                                type="text" 
                                id="preco" 
                                name="preco" 
                                placeholder="R$0,00" 
                                value={preco}
                                onChange={handlePrecoChange}
                                maxLength={10}
                            />

                            <button className='botaoSalvar' type="submit">Salvar</button>
                        </form>

                    </div>
                </div>
                
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Produtos</div>
                        <hr className="linhaHorizontal"/>
                        <div className="listas">
                            {loadingProdutos ? (
                                <div className='mensagens'>Carregando produtos...</div>
                            ) : produtos.length === 0 ? (
                                <div className='mensagens'>Nenhum produto encontrado.</div>
                            ) : (
                                produtos.map(produto => (
                                <div key={produto.idProduto} className="itemLista">
                                    <span>{produto.nome}</span>
                                    <div className="acoes">
                                        <FaEye />
                                        <FaEdit onClick={() => carregarProdutoParaEdicao(produto)} />
                                        <FaTrash onClick={() => deletarProduto(produto.idProduto)} />
                                    </div>
                                </div>
                            ))
                            )}
                        </div>
                    </div>

                    <div className='secaoAdmin'>
                        <div>Pedidos</div>
                        <hr className="linhaHorizontal"/>
                        <div className="listas">
                            {loadingPedidos ? (
                                <div className='mensagens'>Carregando pedidos...</div>
                            ) : pedidos.length === 0 ? (
                                <div className='mensagens'>Nenhum pedido encontrado.</div>
                            ) : (
                                pedidos.map(pedido => (
                                    <div key={pedido.idPedido} className="itemLista">
                                        <span>#{pedido.idPedido}</span>
                                        <span>{pedido.status}</span>
                                        
                                        <div className="acoes">
                                            <FaEye style={{ cursor: "pointer" }} />
                                            <FaTrash 
                                                onClick={() => deletarPedido(pedido.id)} 
                                                style={{ cursor: "pointer" }} 
                                            />
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
  
                        
                    </div>
                </div>
                
                <div className='coluna'>
                    <div className='secaoAdmin'>
                        <div>Editar Perfil</div>
                        <hr className="linhaHorizontal"/>
                        <img className="perfilImagem" src={perfil_admin} alt="perfil"/>
                        <form>
                            <label>Nome</label>
                            <input type="text" value={nomeUsuario} onChange={(e) => setNomeUsuario(e.target.value)}/>

                            <label>E-mail</label>
                            <input type="email" value={emailUsuario} onChange={(e) => setEmailUsuario(e.target.value)}/>
                            <a className= "links" href="redefinir_senha">Redefinir senha</a>

                            <label>Endereços</label>
                            <textarea value={enderecoUsuario}onChange={(e) => setEnderecoUsuario(e.target.value)}/>
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