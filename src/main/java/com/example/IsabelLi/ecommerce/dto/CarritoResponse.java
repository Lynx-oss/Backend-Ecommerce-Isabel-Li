package com.example.IsabelLi.ecommerce.dto;
import com.example.IsabelLi.ecommerce.model.Carrito;
import java.util.List;

public class CarritoResponse {
    private Long id;
    private List<ItemCarritoResponse> items;
    public static CarritoResponse fromEntity(Carrito carrito) {
        CarritoResponse dto = new CarritoResponse();
        dto.setId(carrito.getId());
        dto.setItems(carrito.getItems().stream()
                .map(ItemCarritoResponse::fromEntity)
                .toList());
        return dto;
    }


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public List<ItemCarritoResponse> getItems() { return items; }

    public void setItems(List<ItemCarritoResponse> items) { this.items = items; }

}