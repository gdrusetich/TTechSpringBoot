package com.ProjectoJava.objetos.DTO.request;
import java.util.Set;
import java.time.LocalDate;
import java.util.List;


public class ProductRequestDTO {
    private String title;
    private Double price;
    private LocalDate fechaUltimoPrecio;
    private Boolean oculto = false;
    private Integer stock;
    private String description;
    private Set<Long> idCategories;
    private Long mainImage;
    private List<String> imagesNames;
    public ProductRequestDTO(String title, Double price,LocalDate fecha, Boolean oculto, Integer stock, String description, Set<Long> idCategories, Long mainImage, List<String> imagesNames) {
        this.title = title;
        this.price = price;
        this.fechaUltimoPrecio = fecha;
        this.oculto = oculto;
        this.stock = stock;
        this.description = description;
        this.idCategories = idCategories;
        this.mainImage = mainImage;
        this.imagesNames = imagesNames;
    }

    public String getTitle() {return title;}
    public double getPrice() {return price;}
    public LocalDate getFechaUltimoPrecio(){return fechaUltimoPrecio;}
    public Boolean isOculto(){return oculto;}
    public int getStock() {return stock;}
    public String getDescription() {return description;}
    public Set<Long> getCategories() {return idCategories;}
    public Long getMainImage(){return mainImage;}
    public List<String> getImageURL(){return imagesNames;}

    public void setTitle(String aTitle) {this.title = aTitle;}
    public void setPrice(double aPrice) {this.price = aPrice;}
    public void setFechaUltimoPrecio(LocalDate fecha) {this.fechaUltimoPrecio = fecha;}
    public void setOculto(Boolean oculto){this.oculto = oculto;}
    public void setStock(int stock) {this.stock = stock;}
    public void setDescription(String description) {this.description = description;}
    public void setCategories(Set<Long> idCategories) {this.idCategories = idCategories;}
    public void setImages(List<String> imagesNames) {this.imagesNames = imagesNames;}
    public void setMainImage(Long nuevaImagenId){this.mainImage = nuevaImagenId;}
}
