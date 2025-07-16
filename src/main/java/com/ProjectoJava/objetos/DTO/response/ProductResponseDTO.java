package com.ProjectoJava.objetos.DTO.response;

import com.ProjectoJava.objetos.entity.Product;

public class ProductResponseDTO {
    private Long id;
    private String title;
    private double price;

    public ProductResponseDTO(Product unProducto){
        this.id = unProducto.getId();
        this.title = unProducto.getTitle();
        this.price = unProducto.getPrice();
    }

    public Long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public double getPrice() {return this.price;}
}

