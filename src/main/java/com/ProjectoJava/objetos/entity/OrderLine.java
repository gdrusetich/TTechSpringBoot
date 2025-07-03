package com.ProjectoJava.objetos.entity;

public class OrderLine {
    int idProducto;
    int cantidad;
    public OrderLine(int idProducto, int unaCantidad){
        this.idProducto = idProducto;
        this.cantidad = unaCantidad;
    }

    public int getIdProducto(){ return this.idProducto;}
    public int getCantidad(){return this.cantidad;}
}
