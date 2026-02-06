package com.ProjectoJava.objetos.DTO.response;

public class ImageResponseDTO {
    private Long id;
    private String url;

    public ImageResponseDTO(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
}