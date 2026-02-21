package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.dto.OrdenResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrdenController.class);

    public OrdenController(OrdenService ordenService, UsuarioRepository usuarioRepository) {
        this.ordenService = ordenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<OrdenResponse> crearOrden(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);
            String direccionEnvio = request.get("direccionEnvio");

            Orden orden = ordenService.crearOrdenDesdeCarrito(usuarioId, direccionEnvio);
            return ResponseEntity.status(HttpStatus.CREATED).body(OrdenResponse.fromEntity(orden));
        } catch (Exception e) {
            logger.error("Error: {} ", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mis-ordenes")
    public ResponseEntity<List<OrdenResponse>> obtenerMisOrdenes(Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        List<Orden> ordenes = ordenService.obtenerOrdenesPorUsuario(usuarioId);
        return ResponseEntity.ok(ordenes.stream().
                map(OrdenResponse::fromEntity).toList());
    }

    @GetMapping("/{ordenId}")
    public ResponseEntity<OrdenResponse> obtenerOrdenPorId(
            @PathVariable Long ordenId,
            Authentication authentication) {
        try {
            Orden orden = ordenService.obtenerOrdenPorId(ordenId);

            Long usuarioId = obtenerUsuarioId(authentication);
            if (!orden.getUsuario().getId().equals(usuarioId) && !esAdmin(authentication)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(OrdenResponse.fromEntity(orden));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{ordenId}/cancelar")
    public ResponseEntity<OrdenResponse> cancelarOrden(
            @PathVariable Long ordenId,
            Authentication authentication) {
        try {
            Orden orden = ordenService.obtenerOrdenPorId(ordenId);
            Long usuarioId = obtenerUsuarioId(authentication);

            if (!orden.getUsuario().getId().equals(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Orden ordenCancelada = ordenService.cancelarOrden(ordenId);
            return ResponseEntity.ok(OrdenResponse.fromEntity(ordenCancelada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/admin/todas")
    public ResponseEntity<List<OrdenResponse>> obtenerTodasLasOrdenes() {
        List<Orden> ordenes = ordenService.obtenerTodasLasOrdenes();
        return ResponseEntity.ok(ordenes.stream().map(OrdenResponse::fromEntity).toList());
    }

    @PatchMapping("/admin/{ordenId}/estado")
    public ResponseEntity<OrdenResponse> actualizarEstado(
            @PathVariable Long ordenId,
            @RequestBody Map<String, String> request) {
        try {
            EstadoOrden nuevoEstado = EstadoOrden.valueOf(request.get("estado"));
            Orden orden = ordenService.actualizarEstado(ordenId, nuevoEstado);
            return ResponseEntity.ok(OrdenResponse.fromEntity(orden));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin/estado/{estado}")
    public ResponseEntity<List<OrdenResponse>> obtenerOrdenesPorEstado(@PathVariable EstadoOrden estado) {
        List<Orden> ordenes = ordenService.obtenerOrdenesPorEstado(estado);
        return ResponseEntity.ok(ordenes.stream().map(OrdenResponse::fromEntity).toList());
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