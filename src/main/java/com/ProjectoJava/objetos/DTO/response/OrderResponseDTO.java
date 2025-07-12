package com.ProjectoJava.objetos.DTO.response;
import java.util.ArrayList;
import java.util.List;

public class OrderResponseDTO {
    private List<OrderLineResponseDTO> orden = new ArrayList<>();
    private double total;

    public OrderResponseDTO() {
        this.orden = new ArrayList<>();
    }

    public List<OrderLineResponseDTO> getLineas() {
        return orden;
    }

    public void setOrden(List<OrderLineResponseDTO> unaOrden) {this.orden = unaOrden;}


    public void addOrderLineResponseDTO(OrderLineResponseDTO orderLineToAdd){
        orden.add(orderLineToAdd);
    }

    public double getTotal() {return this.total;}

    public void setTotal(double total) {this.total = total;}
}
