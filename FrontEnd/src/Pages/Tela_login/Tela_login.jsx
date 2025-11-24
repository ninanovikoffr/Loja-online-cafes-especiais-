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
                    <Input label="E-mail:" type="email" />
                    <Input label="Senha:" type="senha" />
                    <div className="esqueceuasenha">
                        <p></p>
                        
                    </div>
                    <button className="botaoentrar"/>
                    <p className="ou"></p>
                    <button className="entrargooglelogin"/>
                    <p className="jatem"></p>
                    
                </div>
            </div>

        </div>

    );
}
export default Tela_login;