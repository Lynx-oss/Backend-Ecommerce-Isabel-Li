package com.example.IsabelLi.ecommerce.dto;

import com.example.IsabelLi.ecommerce.model.ItemOrden;

import java.math.BigDecimal;

public class ItemOrdenResponse {
    private Long id;
    private String productoNombre;
    private Long productoId;
    private String productoImagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public ItemOrdenResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoImagen() {
        return productoImagen;
    }

    public void setProductoImagen(String productoImagen) {
        this.productoImagen = productoImagen;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public static ItemOrdenResponse fromEntity(ItemOrden item) {
        ItemOrdenResponse dto = new ItemOrdenResponse();
        dto.setId(item.getId());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSubtotal(item.getPrecioUnitario());
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

}
