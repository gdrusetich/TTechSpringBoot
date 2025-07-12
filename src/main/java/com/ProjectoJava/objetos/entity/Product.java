package com.ProjectoJava.objetos.entity;
import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;
    private String nombre;
    private double  precio;
    private int stock;

    public Product(){}

    public Long getId(){return this.idProducto;}
    public String getNombre(){return this.nombre;}
    public double getPrecio( ){return this.precio;}
    public int getStock( ){return this.stock;}

    public void setNombre(String nuevoNombre){this.nombre = nuevoNombre;}
    public void setPrecio(double nuevoPrecio){this.precio = nuevoPrecio;}
    public void setStock(int nuevoStock){this.stock = nuevoStock;}

    double precioConDescuento(){
        return precio - precio * 0.2;
    }


}