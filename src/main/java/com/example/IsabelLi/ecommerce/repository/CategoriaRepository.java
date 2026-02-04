package com.example.IsabelLi.ecommerce.repository;

import com.example.IsabelLi.ecommerce.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);

    boolean existsByNombre(String nombre);


}
