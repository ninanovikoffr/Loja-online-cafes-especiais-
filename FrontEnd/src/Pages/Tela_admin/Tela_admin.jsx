import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaTrash, FaEdit, FaArrowRight, FaPlusCircle, FaTimes } from 'react-icons/fa';
import { Navbar } from '../../Components/Navbar/Navbar';
import axios from 'axios';
import perfil_admin from "../../assets/Foto_admin.svg";
import "./Tela_admin.css";


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
    const navigate = useNavigate()
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
    const [enderecos, setEnderecos] = useState([]);
    const [enderecosEdicao, setEnderecosEdicao] = useState({});
    const [modalAberto, setModalAberto] = useState(false);
    const [enderecoEmEdicao, setEnderecoEmEdicao] = useState(null);

 
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

        const precoNumero = preco ? preco.replace('R$', '').replace('.', '').replace(',', '.') : null;
        
        const token = localStorage.getItem('token');
        const roleRaw = localStorage.getItem('role');
        let roles = null;
        try { roles = roleRaw ? JSON.parse(roleRaw) : null; } catch (e) { roles = roleRaw; }
        const isAdmin = roles === 'ADMIN' || (Array.isArray(roles) && roles.includes('ADMIN')) || roles === 'admin';

        if (!isAdmin) {
            alert('Ação restrita: é necessário usuário ADMIN para criar produtos.');
            return;
        }

        try {
            let imagemUrl = null;

            // Se houver imagem selecionada, faz upload
            if (imagem) {
                const formData = new FormData();
                formData.append('file', imagem);

                const uploadConfig = { 
                    headers: { 
                        Authorization: token ? `Bearer ${token}` : ''
                        // NÃO defina Content-Type manualmente para FormData!
                    } 
                };
                const uploadRes = await axios.post('/produtos/upload-imagem', formData, uploadConfig);
                imagemUrl = uploadRes.data.imagemUrl || uploadRes.data.url;
            }

            const body = {
                nome,
                descricao,
                preco: precoNumero ? parseFloat(precoNumero) : null,
                imagemUrl: imagemUrl
            };

            const config = { headers: { Authorization: token ? `Bearer ${token}` : '' } };

            if (produtoEditando) {
                await axios.patch(`/produtos/atualizar/${produtoEditando}`, body, config);
                alert('Produto atualizado com sucesso!')
            } else {
                await axios.post('/produtos/criar', body, config);
                alert('Produto criado com sucesso!');
            }

            setProdutoEditando(null);
            setNome('');
            setDescricao('');
            setPreco('');
            setImagem(null);

            fetchProdutos();
        } catch (err) {
            console.error("Erro ao salvar produto:", err);
            alert('Erro ao salvar o produto!');
        }
    };

    const fetchProdutos = async () => {
        setLoadingProdutos(true);
        try {
            const res = await axios.get('/produtos');
            setProdutos(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('Erro ao buscar produtos:', err);
            alert('Erro ao carregar produtos');
        } finally {
            setLoadingProdutos(false);
        }
    };

    const carregarProdutoParaEdicao = (produto) => {
        setProdutoEditando(produto.idProduto); 
        setNome(produto.nome);
        setDescricao(produto.descricao);
        
        let precoFormatado = "";
        if (produto.preco !== null && produto.preco !== undefined) {
            precoFormatado = "R$" + Number(produto.preco)
                .toFixed(2) 
                .replace(".", ","); 
        }
        setPreco(precoFormatado);
        
        setImagem(null);
    };

    const deletarProduto = async (idProduto) => {
        const confirmar = confirm("Tem certeza que deseja excluir este produto?");
        if (!confirmar) return;

        try {
            await axios.delete(`/produtos/deletar/${idProduto}`);

            alert("Produto deletado com sucesso!");
            fetchProdutos(); 
        } catch (error) {
            console.error("Erro ao deletar produto:", error);
            alert("Erro ao deletar o produto.");
        }
    };

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

    const fetchUsuario = async () => {
        try {
            const token = localStorage.getItem("token");

            const res = await axios.get("/usuario/me", {
                headers: { Authorization: `Bearer ${token}` }
            });

            setUsuario(res.data);
            setNomeUsuario(res.data.nome || "");
            setEmailUsuario(res.data.email || "");
        } catch (err) {
            console.error("Erro ao carregar usuário:", err);
            alert("Erro ao carregar dados do usuário.");
        }
    };

    const fetchEnderecos = async () => {
        try {
            const idUsuario = localStorage.getItem("idUsuario");
            if (!idUsuario) {
                setEnderecos([]);
                setEnderecosEdicao({});
                return;
            }

            const resp = await axios.get(`/enderecos/listar/${idUsuario}`);
            const lista = Array.isArray(resp.data) ? resp.data : [];
            setEnderecos(lista);

            const mapaEdicao = {};
            lista.forEach((end) => {
                mapaEdicao[end.idEndereco] = {
                    rua: end.rua || end.logradouro || end.street || end.endereco || "",
                    numero: end.numero || end.number || "",
                    bairro: end.bairro || end.distrito || "",
                    cidade: end.cidade || end.city || end.localidade || "",
                    estado: end.estado || end.uf || "",
                    cep: end.cep || "",
                    complemento: end.complemento || ""
                };
            });
            setEnderecosEdicao(mapaEdicao);
        } catch (err) {
            console.error("Erro ao carregar enderecos:", err);
            setEnderecos([]);
            setEnderecosEdicao({});
        }
    };

    const handleEnderecoChange = (id, campo, valor) => {
        setEnderecosEdicao((prev) => ({
            ...prev,
            [id]: {
                ...(prev[id] || {}),
                [campo]: valor
            }
        }));
    };

    const abrirModalEdicao = async (endereco) => {
        try {
            // Busca os dados completos do endereço da API
            const response = await axios.get(`/enderecos/${endereco.idEndereco}`);
            const enderecoCompleto = response.data;
            
            // Atualiza o mapa de edição com os dados completos
            setEnderecosEdicao((prev) => ({
                ...prev,
                [endereco.idEndereco]: {
                    rua: enderecoCompleto.rua || enderecoCompleto.logradouro || enderecoCompleto.street || enderecoCompleto.endereco || "",
                    numero: enderecoCompleto.numero || enderecoCompleto.number || "",
                    bairro: enderecoCompleto.bairro || enderecoCompleto.distrito || "",
                    cidade: enderecoCompleto.cidade || enderecoCompleto.city || enderecoCompleto.localidade || "",
                    estado: enderecoCompleto.estado || enderecoCompleto.uf || "",
                    cep: enderecoCompleto.cep || "",
                    complemento: enderecoCompleto.complemento || ""
                }
            }));
            
            setEnderecoEmEdicao(enderecoCompleto);
            setModalAberto(true);
        } catch (err) {
            console.error("Erro ao carregar endereço:", err);
            // Fallback: usa os dados do endereço passado
            setEnderecoEmEdicao(endereco);
            setModalAberto(true);
        }
    };

    const fecharModal = () => {
        setModalAberto(false);
        setEnderecoEmEdicao(null);
    };

    const salvarEndereco = async (id) => {
        const dados = enderecosEdicao[id] || {};
        if (!dados.cep || !dados.numero || !dados.rua || !dados.bairro || !dados.estado) {
            alert("Informe rua, número, bairro, estado e CEP para salvar.");
            return;
        }

        try {
            // Encontra o endereço original para comparar
            const enderecoOriginal = enderecos.find(e => e.idEndereco === id);
            
            // Cria um objeto com apenas os dados que foram alterados
            const dadosAlterados = {};
            
            // Compara cada campo e adiciona ao objeto se foi alterado
            const camposParaVerificar = ['rua', 'numero', 'bairro', 'cidade', 'estado', 'cep', 'complemento'];
            
            camposParaVerificar.forEach(campo => {
                const valorOriginal = enderecoOriginal?.[campo] || enderecoOriginal?.[
                    campo === 'rua' ? 'logradouro' : 
                    campo === 'bairro' ? 'distrito' :
                    campo === 'cidade' ? 'localidade' :
                    campo === 'estado' ? 'uf' : campo
                ] || "";
                
                const valorAtual = dados[campo] || "";
                
                // Se o valor foi alterado, adiciona ao objeto de atualização
                if (valorOriginal !== valorAtual) {
                    dadosAlterados[campo] = valorAtual;
                }
            });
            
            // Se nenhum campo foi alterado, avisa o usuário
            if (Object.keys(dadosAlterados).length === 0) {
                alert("Nenhuma alteração foi detectada.");
                return;
            }
            
            // Envia apenas os dados que foram alterados
            await axios.patch(`/enderecos/atualizar/${id}`, dadosAlterados);

            alert("Endereço atualizado com sucesso!");
            fecharModal();
            fetchEnderecos();
        } catch (err) {
            console.error("Erro ao atualizar endereço:", err);
            alert("Erro ao atualizar endereço.");
        }
    };

    const atualizarUsuario = async () => {
        try {
            const body = {
                nome: nomeUsuario,
                email: emailUsuario
            };

            await axios.patch("/usuario/me/update", body);

            alert("Dados atualizados com sucesso!");

            // Atualiza o nome no localStorage para refletir na Navbar
            localStorage.setItem("nomeUsuario", nomeUsuario);

            fetchUsuario();
        } catch (err) {
            console.error("Erro ao atualizar usuário:", err);
            alert("Erro ao atualizar usuário.");
        }
    };

    useEffect(() => {
        if (modalAberto && enderecoEmEdicao) {
            console.log("Modal aberto com endereço:", enderecoEmEdicao);
            console.log("Dados no state:", enderecosEdicao[enderecoEmEdicao.idEndereco]);
        }
    }, [modalAberto, enderecoEmEdicao, enderecosEdicao]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const roleRaw = localStorage.getItem('role');
        let role = null;
        try { role = roleRaw ? JSON.parse(roleRaw) : null; } catch (e) { role = roleRaw; }

        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }

        const isAdmin = 
            role === 'ADMIN' ||
            role === 'admin' ||
            (Array.isArray(role) && (role.includes('ADMIN') || role.includes('admin')));

        if (!isAdmin) {
            window.alert('Acesso negado: perfil não é administrador.');
            window.location.href = '/';
            return;
        }

        fetchProdutos();
        fetchPedidos();
        fetchUsuario();
        fetchEnderecos();
    }, []);

    const handleLogoutAdmin = async () => {
        const confirmLogout = window.confirm("Tem certeza que deseja sair?");
        if (!confirmLogout) return;

        try {
            // Chama o endpoint de logout no backend
            await axios.post('http://localhost:8080/auth/logout');
        } catch (error) {
            console.error("Erro ao fazer logout no backend:", error);
        } finally {
            // Remove dados do localStorage
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            localStorage.removeItem('nomeUsuario');
            try { delete axios.defaults.headers.common['Authorization']; } catch (e) { }
            navigate('/');
            window.location.reload();
        }
    };

    return (
        <div className="tela_admin">
            <Navbar />

            <div className="tituloadmin">
                <p className="olaAdmin">Olá Admin!</p>
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
                                                onClick={() => deletarPedido(pedido.idPedido)} 
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
                        <div className='secaoRolagem'>
                            <img className="perfilImagem" src={perfil_admin} alt="perfil"/>
                            <form onSubmit={(e) => { e.preventDefault(); atualizarUsuario(); }}>
                                <label>Nome</label>
                                <input type="text" value={nomeUsuario} onChange={(e) => setNomeUsuario(e.target.value)}/>

                                <label>E-mail</label>
                                <input type="email" value={emailUsuario} onChange={(e) => setEmailUsuario(e.target.value)}/>
                                <a className= "links" href="redefinir_senha">Redefinir senha</a>

                                <label>Endereços</label>
                                <div className="enderecosList">
                                    {enderecos.length === 0 && (
                                        <div className="mensagens">Nenhum endereço cadastrado.</div>
                                    )}
                                    {enderecos.map((addr, idx) => {
                                        const rua = addr.rua || addr.logradouro || addr.street || addr.endereco || '';
                                        const numero = addr.numero || addr.number || '';
                                        const bairro = addr.bairro || addr.distrito || '';
                                        const cidade = addr.cidade || addr.city || addr.localidade || '';
                                        const estado = addr.estado || addr.uf || '';
                                        const cep = addr.cep || '';

                                        const full = `${rua}${numero ? ', ' + numero : ''}${bairro ? ' - ' + bairro : ''}${cidade ? ' - ' + cidade : ''}${estado ? ' - ' + estado : ''}${cep ? ' - CEP: ' + cep : ''}`;

                                        return (
                                            <div key={idx} className="endereco-box-admin">
                                                <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                                                    <span>{full || 'Endereço sem informações'}</span>
                                                    <FaEdit 
                                                        onClick={() => abrirModalEdicao(addr)} 
                                                        style={{cursor: 'pointer', color: '#4C351F'}}
                                                        size="1.8em"
                                                    />
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                                <button className='botaoSalvar' type="submit">Salvar</button>
                                <a className= "links" style={{textAlign: 'center'}} href="redefinir_senha">Deletar conta</a>                           
                            </form>
                        </div>
                    </div>
                </div>


            </div>

            {/* Modal para editar endereço */}
            {modalAberto && enderecoEmEdicao && (
                <div className="modalOverlay">
                    <div className="modalContent">
                        <div className="modalHeader">
                            <h2>Editar Endereço</h2>
                            <FaTimes onClick={fecharModal} style={{cursor: 'pointer', fontSize: '24px'}} />
                        </div>
                        
                        <div className="modalBody">
                            <div className="modalRow">
                                <div className="modalCol">
                                    <label>Rua</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.rua || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'rua', e.target.value)}
                                        placeholder="Digite a rua"
                                    />
                                </div>

                                <div className="modalCol modalColSmall">
                                    <label>Número</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.numero || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'numero', e.target.value)}
                                        placeholder="Número"
                                    />
                                </div>
                            </div>

                            <div className="modalRow">
                                <div className="modalCol">
                                    <label>Bairro</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.bairro || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'bairro', e.target.value)}
                                        placeholder="Digite o bairro"
                                    />
                                </div>

                                <div className="modalCol">
                                    <label>Cidade</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.cidade || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'cidade', e.target.value)}
                                        placeholder="Digite a cidade"
                                    />
                                </div>

                                <div className="modalCol modalColSmall">
                                    <label>Estado</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.estado || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'estado', e.target.value)}
                                        placeholder="UF"
                                        maxLength="2"
                                    />
                                </div>
                            </div>

                            <div className="modalRow">
                                <div className="modalCol modalColSmall">
                                    <label>CEP</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.cep || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'cep', e.target.value)}
                                        placeholder="CEP"
                                    />
                                </div>

                                <div className="modalCol">
                                    <label>Complemento</label>
                                    <input 
                                        type="text" 
                                        value={enderecosEdicao[enderecoEmEdicao.idEndereco]?.complemento || ''}
                                        onChange={(e) => handleEnderecoChange(enderecoEmEdicao.idEndereco, 'complemento', e.target.value)}
                                        placeholder="Complemento (opcional)"
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="modalFooter">
                            <button className="btnCancelar" onClick={fecharModal}>Cancelar</button>
                            <button className="btnSalvar" onClick={() => salvarEndereco(enderecoEmEdicao.idEndereco)}>Salvar</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Tela_admin;