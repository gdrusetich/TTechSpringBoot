package com.ProjectoJava.objetos.DTO.request;

public class ProductRequestDTO {
    private String title;
    private double price;
    private int stock;

    public String getTitle() {return title;}
    public double getPrice() {return price;}
    public int getStock() {return stock;}

    public void setTitle(String aTitle) {this.title = aTitle;}
    public void setPrice(double aPrice) {this.price = aPrice;}
    public void setStock(int stock) {this.stock = stock;}
}
