package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.dto.AuthResponse;
import com.example.IsabelLi.ecommerce.dto.LoginRequest;
import com.example.IsabelLi.ecommerce.dto.RegisterRequest;
import com.example.IsabelLi.ecommerce.model.Usuario;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.IsabelLi.ecommerce.security.JwtUtil;
import com.example.IsabelLi.ecommerce.model.Rol;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request){
        if (usuarioRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("El email ya esta registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(Rol.USER);

        usuarioRepository.save(usuario);


        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        String token = jwtUtil.generateToken(usuario.getEmail(), claims.toString());

        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().name()
        );
    }

    public AuthResponse login(LoginRequest request){
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña Incorrectos"));

        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        String token = jwtUtil.generateToken(usuario.getEmail(), claims.toString());



        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().name()
        );
    }





}
