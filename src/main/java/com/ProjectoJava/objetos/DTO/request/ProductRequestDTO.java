package com.ProjectoJava.objetos.DTO.request;
import com.ProjectoJava.objetos.entity.Category;

public class ProductRequestDTO {
    private String title;
    private double price;
    private int stock;
    private Long idCategory;

    public String getTitle() {return title;}
    public double getPrice() {return price;}
    public int getStock() {return stock;}
    public Long getCategory() {return idCategory;}

    public void setTitle(String aTitle) {this.title = aTitle;}
    public void setPrice(double aPrice) {this.price = aPrice;}
    public void setStock(int stock) {this.stock = stock;}
    public void setCategory(Long id) {this.idCategory = id;}
}
