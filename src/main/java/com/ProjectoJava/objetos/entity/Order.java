package com.ProjectoJava.objetos.entity;
import com.ProjectoJava.objetos.DTO.response.OrderLineResponseDTO;
import com.ProjectoJava.objetos.DTO.response.OrderResponseDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPedido;
    public List<OrderLine> pedido = new ArrayList<>();
    public static Long contadorPedidos = 0L;

    public Order(List<OrderLine> pedido) {
        this.pedido = pedido;
        this.idPedido = contadorPedidos;
        contadorPedidos++;
    }

    public List<OrderLine> getOrder(){return pedido;}
    public Long getIdPedido(){return idPedido;}

    public void agregarLinea(OrderLine linea) {pedido.add(linea);}

}