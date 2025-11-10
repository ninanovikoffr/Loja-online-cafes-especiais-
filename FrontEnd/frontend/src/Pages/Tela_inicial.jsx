import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { Navbar } from '../../Components/Navbar/Navbar';

import fundo from "../../assets/Fundo.svg";

import "./Tela_inicial.css";

function Tela_inicial() {
  return (
    <div className="tela_inicial">
     <Navbar />

      {/* Banner principal */}
      <div className="fundo_titulo">
        <img src={fundo} className="fundo_imagem" alt="Fundo de café" />
        <h1 className="titulo_terroir">TERROIR</h1>
      </div>

      {/* Seção Populares */}
      <div className='populares'>
        <div className='populares_text'>Populares</div>
        <div className='colunas_populares'>
          <div className='secao'>
            <div className='cafes_text'>Café Gourmet Baunilha</div>
            <img src="/src/assets/cafe_baunilha.svg" className='foto_cafe' alt="Café gourmet baunilha" />
            <button className='botao_comprar'>Comprar</button>
          </div>

          <div className='secao'>
            <div className='cafes_text'>Café Premium Torra Média</div>
            <img src="/src/assets/cafe_torra.svg" className='foto_cafe' alt="Café premium torra média" />
            <button className='botao_comprar'>Comprar</button>
          </div>

          <div className='secao'>
            <div className='cafes_text'>Café 100% Arábica</div>
            <img src="/src/assets/cafe_arabica.svg" className='foto_cafe' alt="Café 100% arábica" />
            <button className='botao_comprar'>Comprar</button>
          </div>
        </div>
      </div>

      {/* Seção Explore por Categorias */}
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

      {/* Botão Flutuante do Carrinho */}
      <button className='botao_flutuante'> 
          <img src="/src/assets/Carrinho_icon.svg"/>
      </button>



      
    </div>
  );
}

export default Tela_inicial;
