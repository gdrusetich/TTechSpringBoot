package com.ProjectoJava.objetos.entity;
import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;
    private String title;

    private double  price;
    private int stock;


    public Product(){}

    public Long getId(){return this.idProducto;}
    public String getTitle(){return this.title;}
    public double getPrice( ){return this.price;}
    public int getStock( ){return this.stock;}

    public void setTitle(String newTitle){this.title = newTitle;}
    public void setPrice(double newPrice){this.price = newPrice;}
    public void setStock(int nuevoStock){this.stock = nuevoStock;}

    double precioConDescuento(){
        return price - price * 0.2;
    }


}