package com.ProjectoJava.objetos.DTO.response;

import com.ProjectoJava.objetos.entity.Product;

public class ProductResponseDTO {
    private Long id;
    private String nombre;
    private double precio;

    public ProductResponseDTO(Product unProducto){
        this.id = unProducto.getId();
        this.nombre = unProducto.getNombre();
        this.precio = unProducto.getPrecio();
    }

    public Long getId() {return this.id;}
    public String getNombre() {return this.nombre;}
    public double getPrecio() {return this.precio;}
}

