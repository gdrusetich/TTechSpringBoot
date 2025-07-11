package com.ProjectoJava.objetos.DTO.response;

public class OrderLineResponseDTO {

    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public OrderLineResponseDTO(String nombreProducto, int cantidad, double precioUnitario, double subtotal) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public String getNombreProducto() {return nombreProducto;}
    public int getCantidad() {return cantidad;}
    public double getPrecioUnitario() {return precioUnitario;}
    public double getSubtotal() {return subtotal;}

    public void setNombreProducto(String nombreProducto) {this.nombreProducto = nombreProducto;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}
    public void setPrecioUnitario(double precioUnitario) {this.precioUnitario = precioUnitario;}
    public void setSubtotal(double subtotal) {this.subtotal = subtotal;}
}
