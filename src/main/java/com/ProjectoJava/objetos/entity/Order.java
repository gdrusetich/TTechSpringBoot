package com.ProjectoJava.objetos.entity;
import com.ProjectoJava.objetos.DTO.response.OrderLineResponseDTO;
import com.ProjectoJava.objetos.DTO.response.OrderResponseDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPedido;

    @ElementCollection
    @CollectionTable(name = "lineas_pedido", joinColumns = @JoinColumn(name = "pedido_id"))
    public List<OrderLine> pedido = new ArrayList<>();

    public Order() {
    }

    public List<OrderLine> setOrder(List<OrderLine> unaOrden){
        this.pedido = unaOrden;
        return unaOrden;
    }

    public List<OrderLine> getOrder(){return pedido;}
    public Long getIdPedido(){return idPedido;}

    public void agregarLinea(OrderLine linea) {pedido.add(linea);}

}