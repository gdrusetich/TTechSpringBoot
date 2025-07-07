package com.ProjectoJava.objetos.entity;

public class OrderLine {
    Long idProducto;
    int cantidad;
    public OrderLine(Long idProducto, int unaCantidad){
        this.idProducto = idProducto;
        this.cantidad = unaCantidad;
    }

    public Long getIdProducto(){ return this.idProducto;}
    public int getCantidad(){return this.cantidad;}
}
