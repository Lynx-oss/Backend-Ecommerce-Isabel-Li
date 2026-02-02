package com.example.IsabelLi.ecommerce.repository;

import com.example.IsabelLi.ecommerce.model.EstadoOrden;
import com.example.IsabelLi.ecommerce.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    List<Orden> findByEstado(EstadoOrden estado);

}
