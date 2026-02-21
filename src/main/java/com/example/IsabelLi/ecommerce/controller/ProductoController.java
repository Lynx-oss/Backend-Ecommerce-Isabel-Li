package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.dto.ProductoResponse;
import com.example.IsabelLi.ecommerce.model.Producto;
import com.example.IsabelLi.ecommerce.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos.stream().map(ProductoResponse::fromEntity).toList()
        );
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

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crear(producto);
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
