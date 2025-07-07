package com.ProjectoJava.objetos.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPedido;
    public ArrayList<OrderLine> pedido = new ArrayList<>();
    public static Long contadorPedidos = 0L;

    public Order(ArrayList<OrderLine> pedido) {
        this.pedido = pedido;
        this.idPedido = contadorPedidos;
        contadorPedidos++;
    }

    public ArrayList<OrderLine> getOrder(){
        return pedido;

    }

    public Long getIdPedido(){
        return idPedido;
    }


    public void asignarId(){
        contadorPedidos++;
        this.idPedido = contadorPedidos;
    }

    public void agregarLinea(OrderLine linea) {
        pedido.add(linea);
    }


    public void asignarIdPedido() {
        contadorPedidos++;
        this.idPedido += contadorPedidos;
    }
}