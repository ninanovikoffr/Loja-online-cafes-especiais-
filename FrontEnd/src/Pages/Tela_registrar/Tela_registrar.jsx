import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios'; 
import logoGrandereg from "../../assets/logomarrom.svg";
import fotoreg from "../../assets/FotoRegistrar.svg";
import Input from "../../Components/Input/Input";

import "./Tela_registrar.css";

function Tela_registrar(){
    const [email, setEmail] = useState('');
    const [nome, setNome] = useState('');
    const [cpf, setCpf] = useState('');
    const [senha, setSenha] = useState('');
    const [confirmarSenha, setConfirmarSenha] = useState('');
    const navigate = useNavigate();


    const handleRegister = async () => {
        if (senha !== confirmarSenha) {
            alert("As senhas não coincidem!");
            return;
        }

        const userData = {
            email,
            nome,
            cpf,
            senha,
        };

        try {
           
            const previousAuth = axios.defaults.headers.common['Authorization'];
            try {
                delete axios.defaults.headers.common['Authorization'];
            } catch (e) {
            }

            const response = await axios.post('http://localhost:8080/auth/register', userData);

            if (previousAuth) axios.defaults.headers.common['Authorization'] = previousAuth;

            if (response.status === 200) {
                alert("Conta criada com sucesso!");
                navigate('/login'); 
            }
        } catch (error) {

            try { if (axios && axios.defaults && axios.defaults.headers && previousAuth) axios.defaults.headers.common['Authorization'] = previousAuth; } catch(e){}

            alert("Erro ao criar conta. Tente novamente.");
            console.error(error);
        }
    };

    return(
        <div className="telaregistrar">
            <div className="imagemregistrar">
                <img src={fotoreg} alt="Imagem de registro" />
            </div>
            <div className="parteregistrar">
                <div className="nomelogoreg">
                    <img src={logoGrandereg} alt="Logo" />
                    <h1 className="tituloterroirreg">TERROIR</h1>
                </div>
                <div className="partemenorregistrar">
                    <p className="registrar">Registrar</p>
                    <Input  
                        label="E-mail:" 
                        type="email" 
                        placeholder="seu@email.com" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                    />
                    <Input  
                        label="Nome:" 
                        type="text" 
                        placeholder="Seu Nome" 
                        value={nome} 
                        onChange={(e) => setNome(e.target.value)} 
                    />
                    <Input  
                        label="CPF:" 
                        type="text" 
                        placeholder="12345678900" 
                        value={cpf} 
                        onChange={(e) => setCpf(e.target.value)} 
                    />
                    <Input  
                        label="Senha:" 
                        type="password" 
                        placeholder="Digite sua senha" 
                        value={senha} 
                        onChange={(e) => setSenha(e.target.value)} 
                    />
                    <Input  
                        label="Confirmar senha:" 
                        type="password" 
                        placeholder="Confirme sua senha" 
                        value={confirmarSenha} 
                        onChange={(e) => setConfirmarSenha(e.target.value)} 
                    />

                    <button 
                        className="botaocriar" 
                        type="button" 
                        onClick={handleRegister}>
                        <p className="botaocriartexto">Criar Conta</p>
                    </button>

                    <p className="jatem">
                        Já tem uma conta? 
                        <Link 
                            to="/login" 
                            className="fazerloginreg" 
                            style={{textDecoration:'underline',color:'#51361E'}}>
                            Fazer Login
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default Tela_registrar;