import { useNavigate, useLocation } from 'react-router-dom';
import React from "react";
import "./Navbar.css";

import logo from "../../assets/Logo.svg";
import perfil_icon from "../../assets/Perfil_icon.svg";

import { FaBars, FaSearch } from "react-icons/fa";

export function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar__left">
        <img src={perfil_icon} className="navbar__icon" alt="Perfil" />
        <button className="navbar__login">Entrar</button>
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
