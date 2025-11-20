import { useNavigate, useLocation } from 'react-router-dom';
import React from "react";
import "./Input.css";

function Input({ label, type = "text"}) {
    return (
        <div className="componenteinput">
            {label && <p className="campo">{label}</p>}
            
            <input className="inputmolde" type={type}/>
        </div>
    );
}

export default Input;