package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.dto.UsuarioResponse;
import com.example.IsabelLi.ecommerce.dto.UsuarioUpdateRequest;
import com.example.IsabelLi.ecommerce.model.Usuario;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(Authentication authentication) {
        Usuario usuario = obtenerUsuario(authentication);
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(@RequestBody UsuarioUpdateRequest request, Authentication authentication){
        Usuario usuario = obtenerUsuario(authentication);
        if(request.getNombre() != null)
            usuario.setNombre(request.getNombre());
        if(request.getApellido() != null)
            usuario.setApellido(request.getApellido());
        if(request.getTelefono() != null)
            usuario.setTelefono(request.getTelefono());
        if(request.getDireccion() != null)
            usuario.setDireccion(request.getDireccion());
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> cambiarPassword(@RequestBody Map<String, String> request, Authentication authentication){
        String passwordActual = request.get("passwordActual");
        String passwordNueva = request.get("passwordNueva");

        Usuario usuario = obtenerUsuario(authentication);

        if(!passwordEncoder.matches(passwordActual, usuario.getPassword())){
            return ResponseEntity.badRequest().body(Map.of("error", "contraseña actual incorrecta"));
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }

    private Usuario obtenerUsuario(Authentication authentication){
        return usuarioRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("usuario no encontrado"));
    }

}
