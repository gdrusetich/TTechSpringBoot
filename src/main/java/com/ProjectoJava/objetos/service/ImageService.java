package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.ImageRepository;
import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.entity.Product;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public void deleteImage(Long imageId) {
        // 1. Buscar la imagen
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + imageId));

        // 2. Limpiar las referencias en el Producto (Crucial para evitar 404)
        Product product = image.getProduct();
        if (product != null) {
            // La quitamos de la galería
            product.getImages().remove(image);
            
            // Si era la foto de portada, la reseteamos a null
            if (product.getMainImage() != null && product.getMainImage().getId().equals(imageId)) {
                product.setMainImage(null);
            }
            // No hace falta hacer productRepository.save porque @Transactional lo hace al final
        }

        // 3. Borrar el archivo físico del disco
        try {
            Path ruta = Paths.get("uploads").resolve(image.getUrl());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            // Logueamos el error pero permitimos que continúe el borrado en DB
            System.err.println("Error físico al borrar: " + e.getMessage());
        }

        // 4. Borrar de la base de datos
        imageRepository.delete(image);
    }
}