package com.example.IsabelLi.ecommerce.dto;
import com.example.IsabelLi.ecommerce.model.ItemCarrito;
import java.math.BigDecimal;

public class ItemCarritoResponse {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoImagen;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
    public static ItemCarritoResponse fromEntity(ItemCarrito item) {
        ItemCarritoResponse dto = new ItemCarritoResponse();
        dto.setId(item.getId());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSubtotal(item.getSubtotal());
        if (item.getProducto() != null) {
            dto.setProductoId(item.getProducto().getId());
            dto.setProductoNombre(item.getProducto().getNombre());
            if (!item.getProducto().getImagenes().isEmpty()) {
                dto.setProductoImagen(item.getProducto().getImagenes().get(0));
            }
        }
        return dto;
    }
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }

    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }

    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getProductoImagen() { return productoImagen; }

    public void setProductoImagen(String productoImagen) { this.productoImagen = productoImagen; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

}