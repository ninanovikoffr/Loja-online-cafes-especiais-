import React from "react";
import "./Input.css";

function Input({ label, type = "text", placeholder = "", value, onChange }) {
    const htmlType = type === "senha" ? "password" : type;

    return (
        <div className="componenteinput">
            {label && <p className="campo">{label}</p>}
            
            <input
                className="inputmolde"
                type={htmlType}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
            />
        </div>
    );
}

export default Input;
