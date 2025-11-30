import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; 
import logoGrandereg from "../../assets/logomarrom.svg";
import fotoreg from "../../assets/FotoRegistrar.svg";
import logogooglereg from "../../assets/google.svg";
import Input from "../../Components/Input/Input";

import "./Tela_registrar.css";


function Tela_registrar(){
    const navigate = useNavigate();
    return(
        <div className="telaregistrar">
            <div className="imagemregistrar">
                <img src={fotoreg}/>
            </div>
            <div className="parteregistrar">
                <div className="nomelogoreg">
                    <img src={logoGrandereg}/>
                    <h1 className="tituloterroirreg">TERROIR</h1>
                </div>
                <div className="partemenorregistrar">
                    <p className="registrar">Registrar</p>
                    <Input  label="E-mail:" type="email" placeholder="seu@email.com" />
                    <Input  label="Nome:" type="name" placeholder="Seu Nome" />
                    <Input  label="CPF:" type="CPF" placeholder="12345678900" />
                    <Input  label="Senha:" type="senha" placeholder="Digite sua senha" />
                    <Input  label="Confirmar senha:" type="senha" placeholder="Confirme sua senha" />

                    <button className="botaocriar" type="button" onClick={() => navigate('/')}><p className="botaocriartexto">Criar Conta</p></button>

                    <p className="jatem">Já tem uma conta? <Link to="/login" className="fazerloginreg" style={{textDecoration:'underline',color:'#51361E'}}>Fazer Login</Link></p>

                </div>
            </div>

        </div>

    );
}
export default Tela_registrar;
