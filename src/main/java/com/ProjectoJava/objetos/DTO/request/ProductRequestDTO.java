package com.ProjectoJava.objetos.DTO.request;
import java.util.Set;
import java.util.List;


public class ProductRequestDTO {
    private String title;
    private double price;
    private int stock;
    private String description;
    private Set<Long> idCategories;
    private List<String> imagesNames;
    public ProductRequestDTO(String title, double price, int stock, String description, Set<Long> idCategories, List<String> imagesNames) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.idCategories = idCategories;
        this.imagesNames = imagesNames;
    }

    public String getTitle() {return title;}
    public double getPrice() {return price;}
    public int getStock() {return stock;}
    public String getDescription() {return description;}
    public Set<Long> getCategories() {return idCategories;}
    public List<String> getImageURL(){return imagesNames;}

    public void setTitle(String aTitle) {this.title = aTitle;}
    public void setPrice(double aPrice) {this.price = aPrice;}
    public void setStock(int stock) {this.stock = stock;}
    public void setDescription(String description) {this.description = description;}
    public void setCategories(Set<Long> idCategories) {this.idCategories = idCategories;}
    public void setImages(List<String> imagesNames) {this.imagesNames = imagesNames;}
}
