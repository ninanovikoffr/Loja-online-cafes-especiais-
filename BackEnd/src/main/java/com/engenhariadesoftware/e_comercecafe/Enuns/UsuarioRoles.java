package com.engenhariadesoftware.e_comercecafe.Enuns;





public enum UsuarioRoles {
    ADMIN("admin"),
    CLIENTE("cliente"),
    VISITANTE("visitante");


    private String role;

    UsuarioRoles(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
