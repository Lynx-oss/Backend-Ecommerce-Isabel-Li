package com.example.IsabelLi.ecommerce.repository;

import com.example.IsabelLi.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
