import { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import logoGrande from "../../assets/logomarrom.svg";
import fotocafes from "../../assets/fotologin.svg";
import logogoogle from "../../assets/google.svg";

import "./Tela_login.css";

function Tela_login(){
    return(
        <div className="telalogin">
            <div className="imagemlogin">

            </div>
            <div className="partelogin">
                <div className="nomelogo">

                </div>
                <div className="partemenorlogin">
                    <p className="fazerlogin"></p>
                    

                </div>
            </div>

        </div>

    );
}
export default Tela_login;