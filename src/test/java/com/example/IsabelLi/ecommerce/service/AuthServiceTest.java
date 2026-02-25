package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.dto.AuthResponse;
import com.example.IsabelLi.ecommerce.dto.LoginRequest;
import com.example.IsabelLi.ecommerce.dto.RegisterRequest;
import com.example.IsabelLi.ecommerce.model.Rol;
import com.example.IsabelLi.ecommerce.model.Usuario;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import com.example.IsabelLi.ecommerce.security.JwtUtil;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password123");
        registerRequest.setNombre("Ivan");
        registerRequest.setApellido("Rodriguez");
        registerRequest.setTelefono("1143242352");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@email.com");
        usuario.setPassword("encodedPassword");
        usuario.setNombre("Juan");
        usuario.setRol(Rol.USER);
    }

    @Test
    @DisplayName("Registro exitoso devuelve los tokens  los datos de usuario")
    void register_Success(){
        when(usuarioRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtUtil.generateToken("test@email.com", "USER")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("test@email.com", response.getEmail());
        assertEquals("Ivan", response.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro con email duplicado va a lanzar una exception")
    void register_DuplicateEmail_ThrowsExceptions(){
        when(usuarioRepository.existsByEmail("test@email.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("El email ya esta registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login exitoso devuelve token")
    void login_success(){
        when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@email.com", "USER")).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("test@email.com", response.getEmail());
    }

    @Test
    @DisplayName("Login con email inexistente lanza excepcion")
    void login_WrongEmail_ThrowsException(){
        when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });
        assertTrue(exception.getMessage().contains("incorrectos"));
    }
}
