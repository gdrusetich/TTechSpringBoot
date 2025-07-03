package com.ProjectoJava.objetos.entity;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.OrderRepository;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import exceptions.ProductExistsException;

import java.util.ArrayList;
import java.util.Scanner;

public class Order {
    int idPedido;
    public ArrayList<OrderLine> pedido = new ArrayList<>();
    public static int contadorPedidos = 0;

    public Order(ArrayList<OrderLine> pedido) {
        this.pedido = pedido;
        this.idPedido = contadorPedidos;
        contadorPedidos++;
    }

    public ArrayList<OrderLine> getOrder(){
        return pedido;

    }

    public int getIdPedido(){
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