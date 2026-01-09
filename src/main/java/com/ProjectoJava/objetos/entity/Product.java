package com.ProjectoJava.objetos.entity;
import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false, unique = true)
    private String title;

    private double price;
    private int stock;
    @ManyToOne 
    @JoinColumn(name = "category_id") 
    private Category category;

    @Column(nullable = true, name = "image_url")
    private String imageURL;
    
    public Product(){}

    public Long getId(){return this.idProducto;}
    public String getTitle(){return this.title;}
    public double getPrice( ){return this.price;}
    public int getStock( ){return this.stock;}
    public Category getCategory(){return this.category;}
    public String getImageURL(){return this.imageURL;}

    public void setTitle(String newTitle){this.title = newTitle;}
    public void setPrice(double newPrice){this.price = newPrice;}
    public void setStock(int nuevoStock){this.stock = nuevoStock;}
    public void setCategory(Category nuevaCategoria){this.category = nuevaCategoria;}
    public void setImageURL(String nuevaImagen){this.imageURL = nuevaImagen;}

    double precioConDescuento(){
        return price - price * 0.2;
    }


}