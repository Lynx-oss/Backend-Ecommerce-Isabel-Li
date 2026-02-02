package com.example.IsabelLi.ecommerce.controller;
import com.example.IsabelLi.ecommerce.model.EstadoOrden;
import com.example.IsabelLi.ecommerce.model.Orden;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import com.example.IsabelLi.ecommerce.service.OrdenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdenController {

    private final OrdenService ordenService;
    private final UsuarioRepository usuarioRepository;

    public OrdenController(OrdenService ordenService, UsuarioRepository usuarioRepository) {
        this.ordenService = ordenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<Orden> crearOrden(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            String direccionEnvio = request.get("direccionEnvio");

            Orden orden = ordenService.crearOrdenDesdeCarrito(usuarioId, direccionEnvio);
            return ResponseEntity.status(HttpStatus.CREATED).body(orden);
        } catch (Exception e) {
            System.out.println("Error al crear orden: " + e.getMessage());  // Agregar esto
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mis-ordenes")
    public ResponseEntity<List<Orden>> obtenerMisOrdenes(Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        List<Orden> ordenes = ordenService.obtenerOrdenesPorUsuario(usuarioId);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{ordenId}")
    public ResponseEntity<Orden> obtenerOrdenPorId(
            @PathVariable Long ordenId,
            Authentication authentication) {
        try {
            Orden orden = ordenService.obtenerOrdenPorId(ordenId);

            Long usuarioId = obtenerUsuarioId(authentication);
            if (!orden.getUsuario().getId().equals(usuarioId) && !esAdmin(authentication)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(orden);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{ordenId}/cancelar")
    public ResponseEntity<Orden> cancelarOrden(
            @PathVariable Long ordenId,
            Authentication authentication) {
        try {
            Orden orden = ordenService.obtenerOrdenPorId(ordenId);
            Long usuarioId = obtenerUsuarioId(authentication);

            // Solo el dueño puede cancelar
            if (!orden.getUsuario().getId().equals(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Orden ordenCancelada = ordenService.cancelarOrden(ordenId);
            return ResponseEntity.ok(ordenCancelada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/admin/todas")
    public ResponseEntity<List<Orden>> obtenerTodasLasOrdenes() {
        List<Orden> ordenes = ordenService.obtenerTodasLasOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @PatchMapping("/admin/{ordenId}/estado")
    public ResponseEntity<Orden> actualizarEstado(
            @PathVariable Long ordenId,
            @RequestBody Map<String, String> request) {
        try {
            EstadoOrden nuevoEstado = EstadoOrden.valueOf(request.get("estado"));
            Orden orden = ordenService.actualizarEstado(ordenId, nuevoEstado);
            return ResponseEntity.ok(orden);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin/estado/{estado}")
    public ResponseEntity<List<Orden>> obtenerOrdenesPorEstado(@PathVariable EstadoOrden estado) {
        List<Orden> ordenes = ordenService.obtenerOrdenesPorEstado(estado);
        return ResponseEntity.ok(ordenes);
    }

   private Long obtenerUsuarioId(Authentication authentication) {
    String email = authentication.getName();
    return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
            .getId();
}

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}