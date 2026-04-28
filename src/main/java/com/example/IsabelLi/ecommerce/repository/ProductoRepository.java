package com.example.IsabelLi.ecommerce.repository;

import com.example.IsabelLi.ecommerce.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository  extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);

    @Query("SELECT p FROM Producto p WHERE" +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND" +
            "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
            "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
            "(:talla IS NULL OR :talla MEMBER OF p.tallas)")
    Page<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax")BigDecimal precioMax,
            @Param("talla") String talla,
            Pageable pageable


            );

}
