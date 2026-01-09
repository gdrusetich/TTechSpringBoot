package com.ProjectoJava.objetos.DTO.request;

public class ProductRequestDTO {
    private String title;
    private double price;
    private int stock;
    private Long idCategory;
    private String imageURL;
    public ProductRequestDTO(String title, double price, int stock, Long idCategory, String imageUrl) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.idCategory = idCategory;
        this.imageURL = imageUrl;
    }

    public String getTitle() {return title;}
    public double getPrice() {return price;}
    public int getStock() {return stock;}
    public Long getCategory() {return idCategory;}
    public String getImageURL(){return imageURL;}

    public void setTitle(String aTitle) {this.title = aTitle;}
    public void setPrice(double aPrice) {this.price = aPrice;}
    public void setStock(int stock) {this.stock = stock;}
    public void setCategory(Long id) {this.idCategory = id;}
    public void setImageURL(String image) {this.imageURL = image;}
}
