package com.engenhariadesoftware.e_comercecafe.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.AuthResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.AuthenticationDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Infra.TokenService;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.CPF;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Email;
import com.engenhariadesoftware.e_comercecafe.ValueObjects.Senha;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TokenService tokenService;

    /**
     * Endpoint para login do usuário.
     * 
     * @param authenticationDTO - Dados de login (email e senha).
     * @return Retorna o token JWT gerado após autenticação.
     */
    @Operation(summary = "Login", description = "Realiza o login do usuário e retorna um token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido, retorna token JWT."),
        @ApiResponse(responseCode = "400", description = "Erro ao tentar fazer login com dados inválidos.")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarToken((UsuarioModel) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    /**
     * Endpoint para registro de novo usuário.
     * 
     * @param usuarioRequestDTO - Dados do novo usuário (nome, CPF, email, senha).
     * @return Retorna uma resposta de sucesso ou erro baseado na validação do registro.
     */
    @Operation(summary = "Registrar novo usuário", description = "Registra um novo usuário no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Erro no registro (ex: email já existente).")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody UsuarioRequestDTO usuarioRequestDTO){
        if(this.usuarioRepository.findByEmail_Value(usuarioRequestDTO.getEmail()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioRequestDTO.getSenha());
        UsuarioModel usuario = UsuarioModel.builder()
            .nome(usuarioRequestDTO.getNome())
            .cpf(new CPF(usuarioRequestDTO.getCpf()))
            .email(new Email(usuarioRequestDTO.getEmail()))
            .senha(new Senha(encryptedPassword))
            .role(UsuarioRoles.ADMIN)
            .createdAt(LocalDate.now())
            .build();
        this.usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }
}
