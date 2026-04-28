package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.dto.ProductoRequest;
import com.example.IsabelLi.ecommerce.dto.ProductoResponse;
import com.example.IsabelLi.ecommerce.model.Producto;
import com.example.IsabelLi.ecommerce.service.ProductoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {

    private final ProductoService productoService;
    private final static Logger logger = LoggerFactory.getLogger(ProductoController.class);


    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponse>> ObtenerTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoResponse> productos = productoService.obtenerTodosPaginado(pageable).map(ProductoResponse::fromEntity);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(p -> ResponseEntity.ok(ProductoResponse.fromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponse>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(productos.stream().map(ProductoResponse::fromEntity).toList());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoResponse>> buscar (
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false)BigDecimal precioMin,
            @RequestParam(required = false)BigDecimal precioMax,
            @RequestParam(required = false) String talla,
            @RequestParam(defaultValue =  "0") int page,
            @RequestParam(defaultValue = "12") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoResponse> resultado = productoService.buscarConFiltros(nombre, precioMin, precioMax, talla, pageable)
                .map(ProductoResponse::fromEntity);
        return ResponseEntity.ok(resultado);
    }


    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest dto) {
        try {
            Producto nuevoProducto = productoService.crearDesdeDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.fromEntity(nuevoProducto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(ProductoResponse.fromEntity(productoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/inventario")
    public ResponseEntity<ProductoResponse> actualizarInventario(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            Producto productoActualizado = productoService.actualizarInventario(id, cantidad);
            return ResponseEntity.ok(ProductoResponse.fromEntity(productoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        logger.info("=== DELETE REQUEST RECIBIDO ===");
        logger.debug("Eliminando producto ID: {}" , id);
        try {
            productoService.eliminar(id);
            logger.info("Producto eliminado exitosamente");
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.error("Error: Producto tiene órdenes asociadas");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("error", "No se puede eliminar el producto porque tiene órdenes asociadas"));
        } catch (RuntimeException e) {
            logger.error("Error al eliminar: {} " , e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
