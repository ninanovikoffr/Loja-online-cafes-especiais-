import { useNavigate } from 'react-router-dom';
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Navbar.css";

import logo from "../../assets/Logo.svg";
import perfil_icon from "../../assets/Perfil_icon.svg";      
import perfil_admin from "../../assets/Foto_admin.svg";      

import { FaBars, FaSearch, FaArrowRight } from "react-icons/fa";

export function Navbar() {
  const navigate = useNavigate();

  const [isLogged, setIsLogged] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [nomeUsuario, setNomeUsuario] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");
    const roleRaw = localStorage.getItem("role");
    let roles = [];

    try {
      roles = roleRaw ? JSON.parse(roleRaw) : [];
    } catch {
      roles = [];
    }

    setIsLogged(!!token);
    setIsAdmin(roles.includes("ADMIN"));
    
      if (token) {
        const nomeArmazenado = localStorage.getItem("nomeUsuario");
        if (nomeArmazenado) {
          setNomeUsuario(nomeArmazenado);
        } else {
          const idUsuario = localStorage.getItem("idUsuario");
          if (idUsuario) {
            fetchNomeUsuario(idUsuario);
          }
        }
      }
  }, []);

  const fetchNomeUsuario = async (idUsuario) => {
    try {
      const response = await axios.get(`http://localhost:8080/usuario/${idUsuario}`);
      const nome = response.data.nome;
      setNomeUsuario(nome);
      localStorage.setItem("nomeUsuario", nome);
    } catch (error) {
      console.error("Erro ao buscar nome do usuário:", error);
      setNomeUsuario("Usuário");
    }
  };

  const handleClick = () => {
    if (!isLogged) {
      navigate("/login");
      return;
    }

    if (isAdmin) {
      navigate("/admin");
      return;
    }
  };

  const handleLogout = async () => {
    const confirmLogout = window.confirm("Tem certeza que deseja sair?");
    if (!confirmLogout) return;

    try {
      await axios.post('http://localhost:8080/auth/logout');
    } catch (error) {
      console.error("Erro ao fazer logout no backend:", error);
    } finally {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('idUsuario');
      localStorage.removeItem('nomeUsuario');
      try { delete axios.defaults.headers.common['Authorization']; } catch (e) { }
      navigate('/');
      window.location.reload();
    }
  };

  return (
    <header className="navbar">
      <div className="navbar__left">

        {isLogged && isAdmin ? (
          <img
            src={perfil_admin}
            className="navbar__icon"
            alt="Admin"
            style={{ cursor: "pointer" }}
            onClick={() => navigate("/admin")}
          />
        ) : (
          <img
            src={perfil_icon}
            className="navbar__icon"
            alt="Perfil"
            style={{ cursor: "pointer" }}
            onClick={handleClick}
          />
        )}

        
        {!isLogged && (
          <button className="navbar__login" onClick={() => navigate('/login')}>
            Entrar
          </button>
        )}

        {isLogged && (
          <div className="navbar__user-container">
            <button className="navbar__login">
              {nomeUsuario || "Minha Conta"}
            </button>
            <button 
              className="navbar__logout-btn" 
              onClick={handleLogout}
              title="Sair"
            >
              Sair <FaArrowRight />
            </button>
          </div>
        )}
      </div>

      <div className="navbar__center">
        <img src={logo} alt="Logo Terroir" />
      </div>

      <div className="navbar__right">
        <div className="navbar__search">
          <input type="text" placeholder="Buscar" />
          <button type="button">
            <FaSearch className="search__icon" />
          </button>
        </div>
        <FaBars className="navbar__menu" />
      </div>
    </header>
  );
}