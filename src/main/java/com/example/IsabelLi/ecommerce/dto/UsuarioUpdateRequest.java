package com.example.IsabelLi.ecommerce.dto;

public class UsuarioUpdateRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }
}
