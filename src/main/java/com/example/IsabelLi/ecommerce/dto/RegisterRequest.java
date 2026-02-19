package com.example.IsabelLi.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "El email es requerido")
    @Email(message = "Debe proporcionar un email valido")
    private String email;

    @NotBlank(message = "la contraseña es requerida")
    @Size(min = 8, message = "la contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "el nombre es requerido")
    private String nombre;
    @NotBlank(message = "el apellido es requerido")
    private String apellido;

    private String telefono;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String nombre, String apellido, String telefono) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
