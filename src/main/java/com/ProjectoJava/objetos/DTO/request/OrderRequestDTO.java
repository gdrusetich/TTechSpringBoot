package com.ProjectoJava.objetos.DTO.request;

import java.util.ArrayList;

public class OrderRequestDTO {
    private ArrayList<OrderLineRequestDTO> orderLines;

    public ArrayList<OrderLineRequestDTO> getOrderLines() {return orderLines;}

    public void setOrderLines(ArrayList<OrderLineRequestDTO> orderLines) {this.orderLines = orderLines;}
}
