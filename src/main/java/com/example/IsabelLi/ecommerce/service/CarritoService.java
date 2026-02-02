package com.example.IsabelLi.ecommerce.service;
import com.example.IsabelLi.ecommerce.model.Carrito;
import com.example.IsabelLi.ecommerce.model.ItemCarrito;
import com.example.IsabelLi.ecommerce.model.Producto;
import com.example.IsabelLi.ecommerce.model.Usuario;
import com.example.IsabelLi.ecommerce.repository.CarritoRepository;
import com.example.IsabelLi.ecommerce.repository.ItemCarritoRepository;
import com.example.IsabelLi.ecommerce.repository.ProductoRepository;
import com.example.IsabelLi.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          ItemCarritoRepository itemCarritoRepository,
                          ProductoRepository productoRepository,
                          UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    Carrito nuevoCarrito = new Carrito(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Transactional
    public Carrito agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getInventario() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + producto.getInventario());
        }

        Optional<ItemCarrito> itemExistente = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            if (producto.getInventario() < nuevaCantidad) {
                throw new IllegalStateException("Stock insuficiente para la cantidad solicitada");
            }

            item.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(item);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(
                    carrito,
                    producto,
                    cantidad,
                    producto.getPrecio()
            );
            carrito.addItem(nuevoItem);
            itemCarritoRepository.save(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidad(Long usuarioId, Long itemId, Integer nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new RuntimeException("Item no pertenece a este carrito");
        }

        if (item.getProducto().getInventario() < nuevaCantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }

        if (nuevaCantidad <= 0) {
            carrito.removeItem(item);
            itemCarritoRepository.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(item);
        }

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarProducto(Long usuarioId, Long itemId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new RuntimeException("Item no pertenece a este carrito");
        }

        carrito.removeItem(item);
        itemCarritoRepository.delete(item);

        return carritoRepository.save(carrito);
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        itemCarritoRepository.deleteByCarritoId(carrito.getId());
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }

    public BigDecimal calcularTotal(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        return carrito.getItems().stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}