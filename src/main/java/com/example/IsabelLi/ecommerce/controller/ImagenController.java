package com.example.IsabelLi.ecommerce.controller;

import com.example.IsabelLi.ecommerce.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final CloudinaryService cloudinaryService;
    private final static Logger logger = LoggerFactory.getLogger(ImagenController.class);

    public ImagenController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.debug("Upload Request Recibido: {} ({} bytes) ", file.getOriginalFilename(), file.getSize());

        try {
            String url = cloudinaryService.uploadImage(file, "productos");
            logger.debug("Imagen subida exitosamente: {}", url);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            logger.error("Error al subir {} ", e.getMessage());
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