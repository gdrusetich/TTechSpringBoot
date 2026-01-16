package com.ProjectoJava.objetos.DTO.response;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;

public class ProductResponseDTO {
    private Long id;
    private String title;
    private double price;
    private Integer stock;
    private String description;
    private Category category;
    private String imageURL;

    public ProductResponseDTO(Product unProducto){
        this.id = unProducto.getId();
        this.title = unProducto.getTitle();
        this.price = unProducto.getPrice();
        this.stock = unProducto.getStock();
        this.description = unProducto.getDescription();
        this.category = unProducto.getCategory();
        this.imageURL = unProducto.getImageURL();
    }

    public Long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public double getPrice() {return this.price;}
    public int getStock() {return this.stock;}
    public String getDescription() {return this.description;}
    public Category getCategory(){return this.category;}
    public String getImageURL(){return this.imageURL;}
}

