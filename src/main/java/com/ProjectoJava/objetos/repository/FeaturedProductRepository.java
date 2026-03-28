package com.ProjectoJava.objetos.repository;

import com.ProjectoJava.objetos.entity.FeaturedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeaturedProductRepository extends JpaRepository<FeaturedProduct, Long> {

    List<FeaturedProduct> findAllByOrderByPositionAsc();

    Boolean existsByProductId(Long productId);

    FeaturedProduct findByProductId(Long productId);

    List<FeaturedProduct> findAllByPositionGreaterThan(Integer position);

    @Query("SELECT MAX(fp.position) FROM FeaturedProduct fp")
    Integer findMaxPosition();}