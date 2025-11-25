import { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import logoGrande from "../../assets/logomarrom.svg";
import fotocafes from "../../assets/fotologin.svg";
import logogoogle from "../../assets/google.svg";
import Input from "../../Components/Input/Input";

import "./Tela_login.css";

function Tela_login(){
    return(
        <div className="telalogin">
            <div className="imagemlogin">
                <img src={fotocafes}/>
            </div>
            <div className="partelogin">
                <div className="nomelogo">
                    <img src={logoGrande}/>
                    <h1 className="tituloterroir">TERROIR</h1>
                </div>
                <div className="partemenorlogin">
                    <p className="fazerlogin">Fazer Login</p>
                    <Input label="E-mail:" type="email" placeholder="seu@email.com" />
                    <Input label="Senha:" type="senha" placeholder="Digite sua senha" />

                    <div className="esqueceuasenha">
                        <p style={{margin:0}}>Esqueceu sua senha?</p>
                        <a href="#" style={{marginLeft:16,textDecoration:'underline',color:'#7a5a46'}}>Recuperar senha</a>
                    </div>

                    <button className="botaoentrar"><p className="botaoentrartexto">Entrar</p></button>

                    <p className="ou">ou</p>

                    <button className="entrargooglelogin">
                        <p className="classegooglelogin">Entrar com o google</p>
                        <img src={logogoogle} alt="google" style={{width:43,height:43}}/>
                    </button>

                    <p className="naotem">Ainda não tem uma conta? <a href="#" className="registre" style={{textDecoration:'underline',color:'#51361E'}} >Registre-se</a></p>

                </div>
            </div>

        </div>

    );
}
export default Tela_login;