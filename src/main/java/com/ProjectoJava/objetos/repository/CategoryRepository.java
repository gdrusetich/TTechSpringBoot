package com.ProjectoJava.objetos.repository;

import com.ProjectoJava.objetos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();
    List<Category> findByParent_Id(Long idParent);
    List<Category> findAllByOrderByNameAsc();
    Optional<Category> findByName(String name); 
}