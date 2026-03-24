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
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + imageId));

        Product product = image.getProduct();
        if (product != null) {
            product.getImages().remove(image);
            
            if (product.getMainImage() != null && product.getMainImage().getId().equals(imageId)) {
                product.setMainImage(null);
            }
        }

        try {
            Path ruta = Paths.get("uploads").resolve(image.getUrl());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            System.err.println("Error físico al borrar: " + e.getMessage());
        }
        imageRepository.delete(image);
    }
}