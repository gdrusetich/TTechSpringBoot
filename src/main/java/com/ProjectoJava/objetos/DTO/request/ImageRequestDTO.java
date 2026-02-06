package com.ProjectoJava.objetos.DTO.request;

public class ImageRequestDTO {
    private Long id;
    private String url;

    public ImageRequestDTO(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    
}
