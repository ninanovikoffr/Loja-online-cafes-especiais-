import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; 
import logoGrande from "../../assets/logomarrom.svg";
import fotocafes from "../../assets/fotologin.svg";
import logogoogle from "../../assets/google.svg";
import Input from "../../Components/Input/Input";
import axios from 'axios';
import { jwtDecode } from "jwt-decode";

import "./Tela_login.css";

function Tela_login(){
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');

    const handleLogin = async () => {
        const loginData = { email, senha };

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginData),
            });

            if (!response.ok) {
                throw new Error("Falha no login");
            }

            const data = await response.json();

            // backend devolve algo tipo { token: "..." }
            const token =
                data.token ||
                data.accessToken ||
                data.access_token ||
                data.jwt ||
                null;

            if (!token) {
                throw new Error("Token não encontrado na resposta do backend");
            }

            // salva o token
            localStorage.setItem("token", token);
            axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

            const decoded = jwtDecode(token);

            // O role veio como string ou array?
            let roles = decoded.role || decoded.roles || [];

            if (!Array.isArray(roles)) {
                roles = [roles]; // garante que sempre seja array
            }

            localStorage.setItem("role", JSON.stringify(roles));

            // validação
            const isAdmin = roles.includes("ADMIN");

            if (isAdmin) {
                navigate("/admin");
            } else {
                navigate("/");
            }

        } catch (error) {
            console.error(error);
            alert(
                "Erro ao fazer login: " +
                    (error.message || "verifique o console do navegador")
            );
        }
    };

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
                    <Input label="E-mail:" type="email" placeholder="seu@email.com"
                            value={email} onChange={(e) => setEmail(e.target.value)}/>
                        <Input label="Senha:" type="password" placeholder="Digite sua senha"
                            value={senha} onChange={(e) => setSenha(e.target.value)}/>

                    <div className="esqueceuasenha">
                        <p style={{margin:0}}>Esqueceu sua senha?</p>
                        <a href="#" style={{marginLeft:16,textDecoration:'underline',color:'#7a5a46'}}>Recuperar senha</a>
                    </div>

                    <button className="botaoentrar" type="button" onClick={handleLogin}><p className="botaoentrartexto">Entrar</p></button>

                    <p className="ou">ou</p>

                    <button className="entrargooglelogin">
                        <p className="classegooglelogin">Entrar com o google</p>
                        <img src={logogoogle} alt="google" style={{width:43,height:43}}/>
                    </button>

                    <p className="naotem">Ainda não tem uma conta? <Link to="/registrar" className="registre" style={{textDecoration:'underline',color:'#51361E'}}>Registre-se</Link></p>

                </div>
            </div>

        </div>

    );
}
export default Tela_login;