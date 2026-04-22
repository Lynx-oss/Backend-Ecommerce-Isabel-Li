package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.dto.ProductoRequest;
import com.example.IsabelLi.ecommerce.model.Categoria;
import com.example.IsabelLi.ecommerce.model.Producto;
import com.example.IsabelLi.ecommerce.repository.CategoriaRepository;
import com.example.IsabelLi.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    public Page<Producto> obtenerTodosPaginado(Pageable pageable){
        return productoRepository.findAll(pageable);
    }

    public Optional<Producto> obtenerPorId(Long id){
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerPorCategoria(Long categoriaId ){
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional
    public Producto crear(Producto producto){
        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto crearDesdeDTO(ProductoRequest dto) {
        if (dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setInventario(dto.getInventario());
        p.setImagenes(dto.getImagenes());
        p.setTallas(dto.getTallas());
        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            p.setCategoria(cat);
        }
        return productoRepository.save(p);
    }



    @Transactional
    public Producto actualizar(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombre(productoActualizado.getNombre());
                    productoExistente.setDescripcion(productoActualizado.getDescripcion());
                    productoExistente.setPrecio(productoActualizado.getPrecio());
                    productoExistente.setInventario(productoActualizado.getInventario());
                    productoExistente.setImagenes(productoActualizado.getImagenes());
                    productoExistente.setCategoria(productoActualizado.getCategoria());
                    return productoRepository.save(productoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Transactional
    public Producto actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setPrecio(nuevoPrecio);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public Producto actualizarInventario(Long id, int nuevoInventario) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setInventario(nuevoInventario);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public Producto reducirInventario(Long id, int cantidad) {
        return productoRepository.findById(id)
                .map(producto -> {
                    int inventarioActual = producto.getInventario();
                    if (inventarioActual < cantidad) {
                        throw new IllegalStateException("Stock insuficiente. Disponible: " + inventarioActual);
                    }
                    producto.setInventario(inventarioActual - cantidad);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
}