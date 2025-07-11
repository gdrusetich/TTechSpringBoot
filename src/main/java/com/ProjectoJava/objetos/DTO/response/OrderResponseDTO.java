package com.ProjectoJava.objetos.DTO.response;

import com.ProjectoJava.objetos.DTO.response.OrderLineResponseDTO;
import com.ProjectoJava.objetos.entity.OrderLine;

import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private List<OrderLineResponseDTO> orden;
    private double total;


    public void setOrden(List<OrderLineResponseDTO> items) {this.orden = items;}
    public void addOrderLineResponseDTO(OrderLineResponseDTO orderLineToAdd){
        orden.add(orderLineToAdd);
    }
    public void setTotal(double total) {this.total = total;}
}
