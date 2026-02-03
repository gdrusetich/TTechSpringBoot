package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Product findById(int id);

    boolean existsByTitleIgnoreCase(String title);
    List<Product> findByCategories_Id(Long categoryId);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :catId OR c.parent.id = :catId")
    List<Product> findByCategoryIdOrParentId(@Param("catId") Long catId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.images " + 
            "JOIN p.categories c " +
            "WHERE c.id IN :categoryIds")
        List<Product> findByCategories_IdIn(@Param("categoryIds") List<Long> categoryIds);
    
}
