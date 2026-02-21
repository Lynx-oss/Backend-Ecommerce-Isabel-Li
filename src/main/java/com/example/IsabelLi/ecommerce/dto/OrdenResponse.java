package com.example.IsabelLi.ecommerce.dto;

import com.example.IsabelLi.ecommerce.model.Orden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenResponse {
    private Long id;
    private String estado;
    private BigDecimal total;
    private String direccionEnvio;
    private LocalDateTime createdAt;
    private List<ItemOrdenResponse> items;

    public OrdenResponse(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ItemOrdenResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemOrdenResponse> items) {
        this.items = items;
    }

    public static OrdenResponse fromEntity(Orden orden) {
        OrdenResponse dto = new OrdenResponse();
        dto.setId(orden.getId());
        dto.setEstado(orden.getEstado().name());
        dto.setTotal(orden.getTotal());
        dto.setDireccionEnvio(orden.getDireccionEnvio());
        dto.setCreatedAt(orden.getCreatedAt());
        dto.setItems(orden.getItems().stream()
                .map(ItemOrdenResponse::fromEntity)
                .toList());
        return dto;
    }

}
