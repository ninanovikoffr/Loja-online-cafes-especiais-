import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { Navbar } from '../../Components/Navbar/Navbar';
import Carrinho from '../../Components/Carrinho/Carrinho';
import axios from "axios";
import { FaSignOutAlt } from 'react-icons/fa';


import fundo from "../../assets/Fundo.svg";

import "./Tela_inicial.css";

function Tela_inicial() {
  const navigate = useNavigate();
  const [cartOpen, setCartOpen] = useState(false);
  const [produtos, setProdutos] = useState([]);
  const idUsuario = localStorage.getItem("idUsuario");

  // Buscar produtos do banco
  useEffect(() => {
    const fetchProdutos = async () => {
      try {
        const res = await axios.get('http://localhost:8080/produtos');
        setProdutos(Array.isArray(res.data) ? res.data : []);
      } catch (error) {
        console.error("Erro ao buscar produtos:", error);
        setProdutos([]);
      }
    };

    fetchProdutos();
  }, []);
  
  const handleLogout = async () => {
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
      localStorage.removeItem('idUsuario');
      localStorage.removeItem('nomeUsuario');
      try { delete axios.defaults.headers.common['Authorization']; } catch (e) { }
      navigate('/');
      window.location.reload();
    }
  };
  
  const addToCart = async (idProduto) => {
    try {
      await axios.post(
        `http://localhost:8080/carrinhos/${idUsuario}/produtos/${idProduto}`
      );

      setCartOpen(true); // abre o carrinho após adicionar
    } catch (error) {
      console.error("Erro ao adicionar produto:", error);
      alert("Erro ao adicionar produto ao carrinho.");
    }
  };

  console.log("ID do usuário salvo:", idUsuario);


  return (
    <div className="tela_inicial">
     <Navbar />
      <div className="fundo_titulo">
        <img src={fundo} className="fundo_imagem" alt="Fundo de café" />
        <h1 className="titulo_terroir">TERROIR</h1>
      </div>

      <div className='populares'>
        <div className='populares_text'>Populares</div>
        <div className='colunas_populares'>
          <div className='secao'>
            <div className='cafes_text'>Café Gourmet Baunilha</div>
            <img src="/src/assets/cafe_baunilha.svg" className='foto_cafe' alt="Café gourmet baunilha" />
            <button className='botao_comprar' onClick={() => addToCart(1)}>Comprar</button>
          </div>

          <div className='secao'>
            <div className='cafes_text'>Café Premium Torra Média</div>
            <img src="/src/assets/cafe_torra.svg" className='foto_cafe' alt="Café premium torra média" />
            <button className='botao_comprar' onClick={() => addToCart(2)}>Comprar</button>
          </div>

          <div className='secao'>
            <div className='cafes_text'>Café 100% Arábica</div>
            <img src="/src/assets/cafe_arabica.svg" className='foto_cafe' alt="Café 100% arábica" />
            <button className='botao_comprar' onClick={() => addToCart(3)}>Comprar</button>
          </div>
        </div>
      </div>

      <div className='categorias'>
        <div className='categorias_text'>Explore por categorias</div>
        <div className='colunas_categorias'>
          
          <div className='card_categoria'>
            <img src="/src/assets/Capsulas.svg" alt="Cápsulas de café" className='foto_categoria' />
            <div className='info_categoria'>
              <div className='titulo_card'>Cápsulas de café</div>
              <div className='descricao_card'>Praticidade e sabor em cada dose.</div>
            </div>
          </div>

          <div className='card_categoria'>
            <img src="/src/assets/Kits.svg" alt="Kits de café" className='foto_categoria' />
            <div className='info_categoria'>
              <div className='titulo_card'>Kits de café</div>
              <div className='descricao_card'>Tudo para o preparo perfeito.</div>
            </div>
          </div>

          <div className='card_categoria'>
            <img src="/src/assets/Especiais.svg" alt="Cafés especiais" className='foto_categoria' />
            <div className='info_categoria'>
              <div className='titulo_card'>Cafés especiais</div>
              <div className='descricao_card'>O melhor da seleção gourmet.</div>
            </div>
          </div>
        </div>
      </div>

        <button className='botao_flutuante' onClick={()=>setCartOpen(true)}> 
          <img src="/src/assets/Carrinho_icon.svg"/>
        </button>

      <Carrinho
        open={cartOpen}
        onClose={() => setCartOpen(false)}
      />

    </div>
  );
}

export default Tela_inicial;
