import React from "react";
import "./Input.css";

function Input({ label, type = "text", placeholder = "" }) {
    const htmlType = type === 'senha' ? 'password' : type;

    return (
        <div className="componenteinput">
            {label && <p className="campo">{label}</p>}
            <input className="inputmolde" type={htmlType}  />
        </div>
    );
}

export default Input;