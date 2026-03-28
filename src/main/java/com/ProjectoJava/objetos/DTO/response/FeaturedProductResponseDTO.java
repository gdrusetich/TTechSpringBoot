package com.ProjectoJava.objetos.DTO.response;

public record FeaturedProductResponseDTO(Long id, Long productId, String title, String imageUrl,
                                        Double price,Integer position) {

    public Long id() {return id;}
    public Long productId() {return productId;}
    public String title() {return title;}
    public String imageUrl() {return imageUrl;}
    public Double price() {return price;}
    public Integer position() {return position;}
}

