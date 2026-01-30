package com.ProjectoJava.objetos.DTO.response;
import com.ProjectoJava.objetos.entity.Category;

public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;

    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        if (category.getParent() != null) {
            this.parentId = category.getParent().getId();
            this.parentName = category.getParent().getName();
        }
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public Long getParentId() {return parentId;}
    public String getParentName() {return parentName;}

    public void setName(String name) {this.name = name;}
    public void setParentId(Long parentId) {this.parentId = parentId;}
    public void setParentName(String parentName) {this.parentName = parentName;}
    
}
