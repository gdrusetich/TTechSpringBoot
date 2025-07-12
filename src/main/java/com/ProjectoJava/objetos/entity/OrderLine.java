package com.ProjectoJava.objetos.entity;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderLine {
    Long idProducto;
    int cantidad;

    public OrderLine(){
    }

    public void setIdProducto(Long unId){this.idProducto = unId;}
    public void setcantidad(int unaCantidad){this.cantidad = unaCantidad;}


    public Long getIdProducto(){ return this.idProducto;}
    public int getCantidad(){return this.cantidad;}
}
