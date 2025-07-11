package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.Order;
import exceptions.ProductNotExistsException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public class OrderRepositoryAntiguo {
      private ArrayList<Order> todosLosPedidos = new ArrayList<>();

    public void agregarPedidoConfirmado(Order pedidoConfirmado){
        this.todosLosPedidos.add(pedidoConfirmado);
    }

    public ArrayList<Order> getAllOrder(){
        return todosLosPedidos;
    }

    public Order buscarPedido(Long id) throws ProductNotExistsException{
    for(Order o: todosLosPedidos){
        if(o.getIdPedido() == id){
            return o;
        }
    }
        {
            throw new ProductNotExistsException("No existe el producto con el Id: " + id);
        }
    }

}