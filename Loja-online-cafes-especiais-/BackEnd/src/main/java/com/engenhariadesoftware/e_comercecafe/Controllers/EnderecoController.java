package com.engenhariadesoftware.e_comercecafe.Controllers;

import com.engenhariadesoftware.e_comercecafe.DTOs.Request.EnderecoRequestDTO;
import com.engenhariadesoftware.e_comercecafe.DTOs.Response.EnderecoResponseDTO;
import com.engenhariadesoftware.e_comercecafe.Models.EnderecoModel;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;
import com.engenhariadesoftware.e_comercecafe.Repositories.UsuarioRepository;
import com.engenhariadesoftware.e_comercecafe.Services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EnderecoService enderecoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<EnderecoResponseDTO> listarTodos() {
        return enderecoService.listarTodos();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable Long id) {
        return enderecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/tornar-padrao/{id}")
    public ResponseEntity<EnderecoModel> tornarPadrao(@PathVariable Long id) {
        EnderecoModel response = enderecoService.tornarPadrao(id);
        return ResponseEntity.ok(response);
    }

   @PostMapping("/criar")
    public ResponseEntity<EnderecoResponseDTO> criar(@RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO response = enderecoService.salvar(enderecoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/endereco/me")
    public ResponseEntity<Object> deletarEndereco(@PathVariable Long id) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    UsuarioModel usuario = usuarioRepository.findByEmail_Value(email);
    final Long usuarioId = usuario.getIdUsuario();

   
    return usuario.getEnderecos().stream()
            .filter(end -> end.getIdEndereco().equals(usuarioId))
            .findFirst()
            .map(end -> {
                enderecoService.deletar(usuarioId);
                return ResponseEntity.noContent().build();
            })
            .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
}

}
