package com.ProjectoJava.objetos.DTO.response;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;

public class ProductResponseDTO {
    private Long id;
    private String title;
    private double price;
    private Integer stock;
    private Category category;

    public ProductResponseDTO(Product unProducto){
        this.id = unProducto.getId();
        this.title = unProducto.getTitle();
        this.stock = unProducto.getStock();
        this.price = unProducto.getPrice();
        this.category = unProducto.getCategory();
    }

    public Long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public int getStock() {return this.stock;}
    public double getPrice() {return this.price;}
    public Category getCategory(){return this.category;}
}

