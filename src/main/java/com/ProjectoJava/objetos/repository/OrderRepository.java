package com.ProjectoJava.objetos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProjectoJava.objetos.entity.Order;



public interface OrderRepository extends JpaRepository <Order,Long> {
}
