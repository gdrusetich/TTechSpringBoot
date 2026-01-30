package com.ProjectoJava.objetos.DTO.response;
import com.ProjectoJava.objetos.entity.Product;
import java.util.stream.Collectors;
import java.util.List;

public class ProductResponseDTO {
    private Long id;
    private String title;
    private double price;
    private Integer stock;
    private String description;
    private List<CategoryResponseDTO> categories;
    private List<String> images;

    public ProductResponseDTO(Product p) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.price = p.getPrice();
        this.stock = p.getStock();
        this.description = p.getDescription();
        this.images = p.getImages().stream()
                    .map(img -> img.getUrl())
                    .collect(Collectors.toList());

        this.categories = p.getCategories().stream()
                        .map(cat -> new CategoryResponseDTO(cat))
                        .collect(Collectors.toList());
    }
    

    public Long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public double getPrice() {return this.price;}
    public int getStock() {return this.stock;}
    public String getDescription() {return this.description;}
    public List<CategoryResponseDTO> getCategories(){return this.categories;}
    public List<String> getImages(){return this.images;}
}

