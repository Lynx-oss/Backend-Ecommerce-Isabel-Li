package com.example.IsabelLi.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int inventario;

    @ElementCollection
    @CollectionTable(name="producto_imagenes",
            joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name ="imagen_url")
    private List<String> imagenes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto() {}

    public Producto(Long id, String nombre, String descripcion, BigDecimal precio,
                    int inventario, List<String> imagenes ,Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.inventario = inventario;
        this.imagenes = imagenes;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public int getInventario() { return inventario; }

    public List<String> getImagenes() {
        return imagenes;
    }

    public Categoria getCategoria() { return categoria; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setInventario(int inventario) { this.inventario = inventario; }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}