package com.ProjectoJava.objetos.entity;

public class OrderLine {
    Product producto;
    int cantidad;
    public OrderLine(Product unProducto, int unaCantidad){
        this.producto = unProducto;
        this.cantidad = unaCantidad;
    }

    public Product getProducto(){return this.producto;}
    public int getIdProducto(){ return this.producto.getId();
    }
    public int getCantidad(){return this.cantidad;}
    public double getPrecio(){return this.producto.getPrecio() * this.cantidad;}
}
