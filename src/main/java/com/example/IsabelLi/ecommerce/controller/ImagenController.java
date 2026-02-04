package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "http://localhost:3000")
public class ImagenController {

    private final CloudinaryService cloudinaryService;

    public ImagenController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        System.out.println("=== UPLOAD REQUEST RECIBIDO ===");
        System.out.println("Archivo: " + file.getOriginalFilename());
        System.out.println("Tamaño: " + file.getSize());
        try {
            String url = cloudinaryService.uploadImage(file, "productos");
            System.out.println("URL generada: " + url);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            System.out.println("Error al subir: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al subir imagen: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam String publicId) {
        try {
            cloudinaryService.deleteImage(publicId);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}