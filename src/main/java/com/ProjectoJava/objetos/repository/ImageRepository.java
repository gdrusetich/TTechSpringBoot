package com.ProjectoJava.objetos.repository;

import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}   