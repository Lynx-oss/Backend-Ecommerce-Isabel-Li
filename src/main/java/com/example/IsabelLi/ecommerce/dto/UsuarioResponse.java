package com.example.IsabelLi.ecommerce.dto;

import com.example.IsabelLi.ecommerce.model.Usuario;

public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String rol;

    public static UsuarioResponse fromEntity(com.example.IsabelLi.ecommerce.model.Usuario u) {
        UsuarioResponse r = new UsuarioResponse();
        r.id = u.getId();
        r.email = u.getEmail();
        r.nombre = u.getNombre();
        r.apellido = u.getApellido();
        r.telefono = u.getTelefono();
        r.direccion = u.getDireccion();
        r.rol = u.getRol().name();
        return r;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

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

    public String getRol() {
        return rol;
    }
}
