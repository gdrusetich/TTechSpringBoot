package com.ProjectoJava.objetos.DTO.response;

public class OrderLineResponseDTO {

    private String title;
    private int cantidad;
    private double price;
    private double subtotal;

    public OrderLineResponseDTO(String nombreProducto, int cantidad, double precioUnitario, double subtotal) {
        this.title = nombreProducto;
        this.cantidad = cantidad;
        this.price = precioUnitario;
        this.subtotal = subtotal;
    }

    public String getNombreProducto() {return title;}
    public int getCantidad() {return cantidad;}
    public double getPrecioUnitario() {return price;}
    public double getSubtotal() {return subtotal;}

    public void setNombreProducto(String nombreProducto) {this.title = nombreProducto;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}
    public void setPrecioUnitario(double precioUnitario) {this.price = precioUnitario;}
    public void setSubtotal(double subtotal) {this.subtotal = subtotal;}
}
