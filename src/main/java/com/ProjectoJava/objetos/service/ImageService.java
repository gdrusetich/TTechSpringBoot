package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.ImageRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.entity.Product;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + imageId));

        Product product = image.getProduct();
        if (product != null) {
            product.getImages().remove(image);
            
            if (product.getMainImage() != null && product.getMainImage().getId().equals(imageId)) {
                if (!product.getImages().isEmpty()) {
                    product.setMainImage(product.getImages().iterator().next());
                } else {
                    product.setMainImage(null); // No quedaron más fotos
                }
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

    @Transactional
    public void deleteAllImagesByProductId(Long productId) {
        List<Image> imagenes = imageRepository.findByProductId(productId);

        for (Image img : imagenes) {
            try {
                Path rutaArchivo = Paths.get("uploads").resolve(img.getUrl()).toAbsolutePath();
                Files.deleteIfExists(rutaArchivo);
            } catch (IOException e) {
                System.err.println("No se pudo borrar el archivo físico: " + img.getUrl());
            }
            imageRepository.delete(img);
        }
    }
    // ImageService.java

    @Transactional
    public void addImagesToProduct(Long productId, MultipartFile[] files) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads").resolve(fileName);
                
                Files.copy(file.getInputStream(), path);

                Image img = new Image();
                img.setUrl(fileName);
                img.setProduct(product); // Relación ManyToOne
                
                imageRepository.save(img);
            }
        }
    }

}