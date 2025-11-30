import { useNavigate } from 'react-router-dom';
import React, { useEffect, useState } from "react";
import "./Navbar.css";

import logo from "../../assets/Logo.svg";
import perfil_icon from "../../assets/Perfil_icon.svg";      // cliente
import perfil_admin from "../../assets/Foto_admin.svg";       // admin

import { FaBars, FaSearch } from "react-icons/fa";

export function Navbar() {
  const navigate = useNavigate();

  const [isLogged, setIsLogged] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);

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
  }, []);

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
          <button className="navbar__login">
            Minha Conta
          </button>
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