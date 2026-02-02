package com.example.IsabelLi.ecommerce.service;
import com.example.IsabelLi.ecommerce.model.*;
import com.example.IsabelLi.ecommerce.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final CarritoService carritoService;
    private final ProductoService productoService;

    public OrdenService(OrdenRepository ordenRepository,
                        CarritoService carritoService,
                        ProductoService productoService) {
        this.ordenRepository = ordenRepository;
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    @Transactional
    public Orden crearOrdenDesdeCarrito(Long usuarioId, String direccionEnvio) {
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuarioId);

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        Orden orden = new Orden(carrito.getUsuario());
        orden.setDireccionEnvio(direccionEnvio);

        for (ItemCarrito itemCarrito : carrito.getItems()) {
            Producto producto = itemCarrito.getProducto();

            if (producto.getInventario() < itemCarrito.getCantidad()) {
                throw new IllegalStateException(
                        "Stock insuficiente para " + producto.getNombre() +
                                ". Disponible: " + producto.getInventario()
                );
            }

            ItemOrden itemOrden = new ItemOrden(
                    orden,
                    producto,
                    itemCarrito.getCantidad(),
                    itemCarrito.getPrecioUnitario()
            );
            orden.addItem(itemOrden);

            productoService.reducirInventario(producto.getId(), itemCarrito.getCantidad());
        }

        orden.calcularTotal();

        Orden ordenGuardada = ordenRepository.save(orden);

        // Vaciar carrito
        carritoService.vaciarCarrito(usuarioId);

        return ordenGuardada;
    }

    public List<Orden> obtenerOrdenesPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    public Orden obtenerOrdenPorId(Long ordenId) {
        return ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @Transactional
    public Orden actualizarEstado(Long ordenId, EstadoOrden nuevoEstado) {
        Orden orden = obtenerOrdenPorId(ordenId);
        orden.setEstado(nuevoEstado);
        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden cancelarOrden(Long ordenId) {
        Orden orden = obtenerOrdenPorId(ordenId);

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar órdenes en estado PENDIENTE"
            );
        }

        for (ItemOrden item : orden.getItems()) {
            Producto producto = item.getProducto();
            producto.setInventario(producto.getInventario() + item.getCantidad());
        }

        orden.setEstado(EstadoOrden.CANCELADO);
        return ordenRepository.save(orden);
    }

    public List<Orden> obtenerTodasLasOrdenes() {
        return ordenRepository.findAll();
    }

    public List<Orden> obtenerOrdenesPorEstado(EstadoOrden estado) {
        return ordenRepository.findByEstado(estado);
    }
}