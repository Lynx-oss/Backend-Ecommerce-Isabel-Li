package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.model.Producto;
import com.example.IsabelLi.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
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