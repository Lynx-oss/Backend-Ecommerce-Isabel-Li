package com.example.IsabelLi.ecommerce.controller;
import com.example.IsabelLi.ecommerce.model.Carrito;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import com.example.IsabelLi.ecommerce.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:3000")
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepository;

    public CarritoController(CarritoService carritoService, UsuarioRepository usuarioRepository) {
        this.carritoService = carritoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito(Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregarProducto(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            Long productoId = Long.valueOf(request.get("productoId").toString());
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());

            Carrito carrito = carritoService.agregarProducto(usuarioId, productoId, cantidad);
            return ResponseEntity.ok(carrito);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            Integer nuevaCantidad = request.get("cantidad");

            Carrito carrito = carritoService.actualizarCantidad(usuarioId, itemId, nuevaCantidad);
            return ResponseEntity.ok(carrito);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Carrito> eliminarProducto(
            @PathVariable Long itemId,
            Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            Carrito carrito = carritoService.eliminarProducto(usuarioId, itemId);
            return ResponseEntity.ok(carrito);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciarCarrito(Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            carritoService.vaciarCarrito(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> obtenerTotal(Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        BigDecimal total = carritoService.calcularTotal(usuarioId);
        return ResponseEntity.ok(Map.of("total", total));
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
    }
}