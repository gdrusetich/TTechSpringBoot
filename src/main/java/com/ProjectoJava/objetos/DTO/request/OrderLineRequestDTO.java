package com.ProjectoJava.objetos.DTO.request;

public class OrderLineRequestDTO {
    private Long idProduct;
    private int cantidad;


    public Long getIdProduct() {return idProduct;}
    public int getCantidad() {return cantidad;}

    public void setIdProduct(Long idProduct) {this.idProduct = idProduct;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

}
