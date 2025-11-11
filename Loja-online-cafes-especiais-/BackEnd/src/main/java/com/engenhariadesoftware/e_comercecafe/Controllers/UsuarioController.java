package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.UsuarioRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.UsuarioResponseShowDTO;
import com.engenhariadesoftware.e_comercecafe.Enuns.UsuarioRoles;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;




@RestController
@RequestMapping("/usuario")
public class UsuarioController {


    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
        if (usuarioResponseDTO != null) {
            return ResponseEntity.ok(usuarioResponseDTO);
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
        if (usuarioResponseDTO != null) {
            UsuarioResponseDTO updatedUser = usuarioService.atualizarDados(id, usuarioRequestDTO);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public UsuarioResponseDTO criar(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        usuarioRequestDTO.setRole(UsuarioRoles.CLIENTE);
        usuarioRequestDTO.setCreatedAt(LocalDate.now());
        usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
        return usuarioService.salvar(usuarioRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

   @GetMapping("/me")
    public ResponseEntity<UsuarioResponseShowDTO> pegarMeusDados() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    
    UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
    UsuarioResponseShowDTO response = new UsuarioResponseShowDTO();
    response.setIdUsuario(usuario.getIdUsuario());
    response.setNome(usuario.getNome());
    response.setCpf(usuario.getCpf().getValue());
    response.setEmail(usuario.getEmail().getValue());
    response.setRole(usuario.getRole());    

    return ResponseEntity.ok(response);
} 

    @PatchMapping("/me/update")
    public ResponseEntity<UsuarioResponseShowDTO> atualizarMeusDados(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioResponseShowDTO updatedUser = usuarioService.atualizarMeusDados(usuario.getIdUsuario(), usuarioRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
