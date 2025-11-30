import { useLocation, Link } from 'react-router-dom';
import logo from "../../assets/Logo.svg";
import perfil_icon from "../../assets/Perfil_icon.svg"; // Ícone genérico de perfil
import perfil_admin from "../../assets/Foto_admin.svg"; // Foto do admin para a tela de admin

import { FaBars, FaSearch } from "react-icons/fa";

export function Navbar() {
  const location = useLocation(); // Captura a localização atual (URL)

  return (
    <header className="navbar">
      <div className="navbar__left">
        {/* Condicional para exibir o ícone de perfil genérico ou foto do admin */}
        {location.pathname === '/admin' ? (
          <>
            <img src={perfil_admin} className="navbar__icon-admin" alt="Perfil Admin" />
            <Link to="/admin" className="navbar__login">Meu Perfil</Link>
          </>
        ) : (
          <>
            <img src={perfil_icon} className="navbar__icon" alt="Perfil" />
            <Link to="/login" className="navbar__login">Entrar</Link>
          </>
        )}
      </div>

      <div className="navbar__center">
        <Link to="/">
          <img src={logo} alt="Logo Terroir" />
        </Link>
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
