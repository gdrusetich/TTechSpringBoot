package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.FeaturedProductRepository;
import com.ProjectoJava.objetos.DTO.response.FeaturedProductResponseDTO;
import com.ProjectoJava.objetos.entity.FeaturedProduct;
import com.ProjectoJava.objetos.entity.Product;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeaturedProductService {

    @Autowired
    private FeaturedProductRepository featuredRepository;

    @Autowired
    private ProductRepository productRepository;

 
    public List<FeaturedProduct> isFeaturedProducts() {
        return featuredRepository.findAllByOrderByPositionAsc();
    }

    @Transactional
    public void addFeatured(Long productId) {
        if (featuredRepository.existsByProductId(productId)) {
            throw new RuntimeException("El producto ya está destacado");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Integer lastPos = featuredRepository.findMaxPosition();
        int nextPos = (lastPos == null) ? 1 : lastPos + 1;
        FeaturedProduct featured = new FeaturedProduct(product, nextPos);
        featuredRepository.save(featured);
    }

    @Transactional
    public void removeFeatured(Long productId) {
        FeaturedProduct toRemove = featuredRepository.findByProductId(productId);
        
        if (toRemove != null) {
            int removedPos = toRemove.getPosition();
            featuredRepository.delete(toRemove);
            
            List<FeaturedProduct> following = featuredRepository.findAllByPositionGreaterThan(removedPos);
            for (FeaturedProduct fp : following) {
                fp.setPosition(fp.getPosition() - 1);
            }
            featuredRepository.saveAll(following);
        }
    }

    private FeaturedProductResponseDTO convertToDTO(FeaturedProduct fp) {
        Product p = fp.getProduct();
        String urlFinal = "/images/default.jpg"; // Solo como último recurso desesperado
        if (p.getMainImage() != null && p.getMainImage().getUrl() != null) {
            urlFinal = p.getMainImage().getUrl();
        } 
        else if (p.getImages() != null && !p.getImages().isEmpty()) {
            urlFinal = p.getImages().iterator().next().getUrl();
        }
        return new FeaturedProductResponseDTO(
                fp.getId(),
                p.getId(),
                p.getTitle(), 
                p.getDescription(), // <--- PASAR LA DESCRIPCIÓN AQUÍ
                urlFinal,
                p.getPrice(),
                fp.getPosition()
            );
    }

    public List<FeaturedProductResponseDTO> getAllFeaturedDTOs() {
        return featuredRepository.findAllByOrderByPositionAsc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public void updatePosition(Long productId, int newPosition) {
        FeaturedProduct target = featuredRepository.findByProductId(productId);
        if (target == null) throw new RuntimeException("El producto no está en destacados");

        int oldPosition = target.getPosition();
        if (oldPosition == newPosition) return;

        List<FeaturedProduct> allFeatured = featuredRepository.findAll();

        for (FeaturedProduct fp : allFeatured) {
            if (fp.getProduct().getId().equals(productId)) continue;

            int currentPos = fp.getPosition();
            if (newPosition < oldPosition) {
                if (currentPos >= newPosition && currentPos < oldPosition) {
                    fp.setPosition(currentPos + 1);
                }
            } 
            else {
                if (currentPos > oldPosition && currentPos <= newPosition) {
                    fp.setPosition(currentPos - 1);
                }
            }
        }

        target.setPosition(newPosition);        
        featuredRepository.saveAll(allFeatured);
    }

}