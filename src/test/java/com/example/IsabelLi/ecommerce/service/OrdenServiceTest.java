package com.example.IsabelLi.ecommerce.service;

import com.example.IsabelLi.ecommerce.model.*;
import com.example.IsabelLi.ecommerce.repository.OrdenRepository;
import com.example.IsabelLi.ecommerce.repository.ProductoRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;
    @Mock
    private CarritoService carritoService;
    @Mock
    private ProductoService productoService;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Usuario usuario;
    private Producto producto;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@gmail.com");
        usuario.setNombre("Ivan");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Camisa");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setInventario(10);

        ItemCarrito item = new ItemCarrito();
        item.setProducto(producto);
        item.setCantidad(2);
        item.setPrecioUnitario(new BigDecimal("1500.00"));

        carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setItems(new ArrayList<>(List.of(item)));
    }

    @Test
    @DisplayName("Crear orden desde carrito exitosamente")
    void crearOrden_Success(){
        when(carritoService.obtenerOCrearCarrito(1L)).thenReturn(carrito);
        when(productoService.reducirInventario(anyLong(), anyInt())).thenReturn(producto);

        Orden ordenMock = new Orden(usuario);

        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenMock);

        Orden orden = ordenService.crearOrdenDesdeCarrito(1L, "calle fake 4232");

        assertNotNull(orden);
        verify(ordenRepository).save(any(Orden.class));
        verify(carritoService).vaciarCarrito(1L);
    }

    @Test
    @DisplayName("crear orden con carrito vacio lanzar exception")
    void crearOrden_CarritoVacio_LanzarException(){
        carrito.setItems(new ArrayList<>());
        when(carritoService.obtenerOCrearCarrito(1L)).thenReturn(carrito);
        assertThrows(IllegalStateException.class,() -> ordenService.crearOrdenDesdeCarrito(1L, "Calle fake 234124"));

        verify(ordenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Cancelar orden en estado PRESENTE restaura stock")
    void cancelarOrden_Success(){
        ItemOrden itemOrden = new ItemOrden();
        itemOrden.setProducto(producto);
        itemOrden.setCantidad(2);

        Orden orden = new Orden(usuario);
        orden.setId(1L);
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setItems(new ArrayList<>(List.of(itemOrden)));

        when(ordenRepository.findById(1L)).thenReturn(java.util.Optional.of((orden)));

        when(ordenRepository.save(any())).thenReturn(orden);

        Orden resultado = ordenService.cancelarOrden(1L);

        org.junit.jupiter.api.Assertions.assertEquals(EstadoOrden.CANCELADO, resultado.getEstado());
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("Cancelar orden que no esta PENDIENTE lanza exception")
    void cancelarOrden_NoEsPendiente_LanzaException(){
        Orden orden = new Orden(usuario);
        orden.setId(1L);
        orden.setEstado(EstadoOrden.ENVIADO);
        orden.setItems(new ArrayList<>());

        when(ordenRepository.findById(1L)).thenReturn(java.util.Optional.of(orden));

        assertThrows(IllegalStateException.class, () -> ordenService.cancelarOrden(1L));
    }

    @Test
    @DisplayName("Obtener ordenes por usuario devuelve lista")
    void obtenerOrdenesPorUsuario_Success(){
        when(ordenRepository.findByUsuarioIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        List<Orden> ordenes = ordenService.obtenerOrdenesPorUsuario(1L);
        assertNotNull(ordenes);

        verify(ordenRepository).findByUsuarioIdOrderByCreatedAtDesc(1L);
    }

    

}
