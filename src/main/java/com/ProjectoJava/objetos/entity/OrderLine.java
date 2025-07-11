package com.ProjectoJava.objetos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idOrderLine;

    Long idProducto;
    int cantidad;
    public OrderLine(Long idProducto, int unaCantidad){
        this.idProducto = idProducto;
        this.cantidad = unaCantidad;
    }

    public Long getIdProducto(){ return this.idProducto;}
    public int getCantidad(){return this.cantidad;}
}
