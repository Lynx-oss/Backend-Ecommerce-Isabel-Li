package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.model.Categoria;
import com.example.IsabelLi.ecommerce.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerTodos(){
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }

    @Transactional
    public Categoria crear(Categoria categoria){
        if(categoriaRepository.existsByNombre(categoria.getNombre())){
            throw new IllegalArgumentException("Ya existe una categoria con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(Long id){
        if(!categoriaRepository.existsById(id)){
            throw new RuntimeException("categoria no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    @Transactional
    public @Nullable Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria categoria = obtenerPorId(id);
        categoria.setNombre(categoriaActualizada.getNombre());
        return categoriaRepository.save(categoria);

    }
}
