package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Product findById(int id);

    Boolean existsByTitleIgnoreCase(String title);
    List<Product> findByCategories_Id(Long categoryId);
    List<Product >findByCategoriesContaining(Category category);
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :catId OR c.parent.id = :catId")
    List<Product> findByCategoryIdOrParentId(@Param("catId") Long catId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Product p SET p.featured = :status WHERE p.id IN :ids")
    void updateFeaturedStatus(@Param("ids") List<Long> ids, @Param("status") boolean status);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.images " + 
            "JOIN p.categories c " +
            "WHERE c.id IN :categoryIds")
        List<Product> findByCategories_IdIn(@Param("categoryIds") List<Long> categoryIds);
        List<Product> findByOcultoFalse();
        List<Product> findByTitleContainingIgnoreCase(String title);
        List<Product> findByTitleContainingIgnoreCaseAndOcultoFalse(String title);
        List<Product> findByFechaUltimoPrecioBefore(LocalDate fechaLimite);
        List<Product> findByFechaUltimoPrecioIsNull();
}
