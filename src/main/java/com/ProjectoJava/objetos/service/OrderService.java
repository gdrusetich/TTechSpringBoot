package com.ProjectoJava.objetos.service;
import com.ProjectoJava.objetos.entity.OrderLine;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Order;
import com.ProjectoJava.objetos.repository.OrderRepository;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {
    private ProductService productService;
    private  OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public ArrayList<Order> listarPedidos(){
        return orderRepository.getAllOrder();
    }

    public Order crearPedido(ArrayList<OrderLine> nuevaOrden ) throws ProductNotExistsException, NoStockException {
        for(OrderLine lp: nuevaOrden){
            Product unProducto = productService.buscarProductoPorId(lp.getIdProducto());

            if(!productService.hayStock(unProducto.getId(),lp.getCantidad())){
                throw new NoStockException("No hay stock de: " + unProducto.getNombre());
            }

        }

        for(OrderLine lp: nuevaOrden){
            productService.descontarStock(lp.getIdProducto(), lp.getCantidad());
            }
        Order crearOrden = new Order(nuevaOrden);
        return crearOrden;
        }

    public void agregarLineaPedido(int idOrder, int idProducto, int cantidadPedida) throws ProductNotExistsException {

        Product productoPedido = this.productService.buscarProductoPorId(idProducto);
        if (productoPedido == null) {
            throw new ProductNotExistsException("No existe el producto con Id: " + idProducto);
        }

        if (productService.hayStock(idProducto, cantidadPedida)) {
            OrderLine unaLineaPedido = new OrderLine(productoPedido, cantidadPedida);
            Order pedidoBuscado = buscarPedido(idOrder);
            pedidoBuscado.agregarLinea(unaLineaPedido);
        }
    }

    public Order buscarPedido(int id) throws ProductNotExistsException{
       return orderRepository.buscarPedido(id);
    }

}
