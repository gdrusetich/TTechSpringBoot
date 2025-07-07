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

    public double precioLineaPedido(OrderLine unaLineaPedido) throws ProductNotExistsException{
        Product producto = productService.buscarProductoPorId(unaLineaPedido.getIdProducto());
        return producto.getPrecio() * unaLineaPedido.getCantidad();
    }

    public double precioTotalOrder(Order unaOrden) throws ProductNotExistsException{
        double precioOrden=0;
        for(OrderLine ol: unaOrden.getOrder()){
            precioOrden += precioLineaPedido(ol);
        }
        return precioOrden;
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
        Order ordenCreada = new Order(nuevaOrden);
        orderRepository.agregarPedidoConfirmado(ordenCreada);
        return ordenCreada;
        }

    public void agregarLineaPedido(Long idOrder, Long idProducto, int cantidadPedida) throws ProductNotExistsException {

        Product productoPedido = this.productService.buscarProductoPorId(idProducto);
        if (productoPedido == null) {
            throw new ProductNotExistsException("No existe el producto con Id: " + idProducto);
        }

        if (productService.hayStock(idProducto, cantidadPedida)) {
            OrderLine unaLineaPedido = new OrderLine(idProducto, cantidadPedida);
            Order pedidoBuscado = buscarPedido(idOrder);
            pedidoBuscado.agregarLinea(unaLineaPedido);
        }
    }

    public Order buscarPedido(Long id) throws ProductNotExistsException{
       return orderRepository.buscarPedido(id);
    }

}
