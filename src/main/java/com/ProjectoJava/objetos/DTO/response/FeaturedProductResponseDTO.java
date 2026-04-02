package com.ProjectoJava.objetos.DTO.response;

public record FeaturedProductResponseDTO(
    Long id, 
    Long productId, 
    String title, 
    String description,
    String imageUrl,
    Double price,
    Integer position
) { }