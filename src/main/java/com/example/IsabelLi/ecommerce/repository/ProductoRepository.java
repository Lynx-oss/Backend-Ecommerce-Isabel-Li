package com.example.IsabelLi.ecommerce.repository;

import com.example.IsabelLi.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductoRepository  extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);

}
