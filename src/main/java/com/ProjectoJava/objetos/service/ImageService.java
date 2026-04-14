package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.ImageRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.entity.Product;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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

    @Autowired
    private Cloudinary cloudinary; // Inyectamos el bean que configuramos antes

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
                    product.setMainImage(null);
                }
            }
        }

        // --- BORRADO EN LA NUBE ---
        try {
            // Extraemos el "Public ID" de la URL (es el nombre único que le da Cloudinary)
            // Ejemplo URL: .../upload/v123/nombre_archivo.jpg -> Public ID: nombre_archivo
            String url = image.getUrl();
            if (url.contains("cloudinary.com")) {
                String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            System.err.println("Error al borrar en Cloudinary: " + e.getMessage());
        }

        imageRepository.delete(image);
    }

    @Transactional
    public void deleteAllImagesByProductId(Long productId) {
        List<Image> imagenes = imageRepository.findByProductId(productId);
        for (Image img : imagenes) {
            // Reutilizamos la lógica de borrado para que limpie Cloudinary también
            deleteImage(img.getId());
        }
    }

    @Transactional
    public void addImagesToProduct(Long productId, MultipartFile[] files) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // --- SUBIDA A LA NUBE ---
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String urlCloudinary = uploadResult.get("url").toString();

                Image img = new Image();
                img.setUrl(urlCloudinary);
                img.setProduct(product);
                imageRepository.save(img);
            }
        }
    }
}