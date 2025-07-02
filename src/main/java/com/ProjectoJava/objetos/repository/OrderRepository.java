package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.Order;
import com.ProjectoJava.objetos.entity.OrderLine;
import com.ProjectoJava.objetos.entity.Product;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Scanner;

@Repository
public class OrderRepository {
      private ArrayList<Order> todosLosPedidos = new ArrayList<>();

    public void agregarPedidoConfirmado(Order pedidoConfirmado){
        this.todosLosPedidos.add(pedidoConfirmado);
    }

    public ArrayList<Order> getAllOrder(){
        return todosLosPedidos;
    }

    public Order buscarPedido(int id) throws ProductNotExistsException{
    for(Order o: todosLosPedidos){
        if(o.getIdPedido() == id){
            return o;
        }
    }
        {
            throw new ProductNotExistsException("No existe el producto con el Id: " + id);
        }
    }

    public void confirmarPedido(Order pedidoAConfirmar) throws NoStockException{
        for(OrderLine lp : pedidoAConfirmar.getOrder()){
            try{
                lp.getProducto().descontarStock(lp.getCantidad());
                pedidoAConfirmar.asignarIdPedido();
            }
            catch(NoStockException e){
                e.getMessage();
            }
        }
        pedidoAConfirmar.asignarId();
    }



}