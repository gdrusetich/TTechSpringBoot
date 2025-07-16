package com.ProjectoJava.objetos.service;
import com.ProjectoJava.objetos.DTO.request.OrderLineRequestDTO;
import com.ProjectoJava.objetos.DTO.response.OrderLineResponseDTO;
import com.ProjectoJava.objetos.DTO.response.OrderResponseDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.OrderLine;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Order;
import com.ProjectoJava.objetos.repository.OrderRepository;
import exceptions.NoStockException;
import exceptions.OrderNotExistsException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private ProductService productService;
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public double precioLineaPedido(OrderLine unaLineaPedido) throws ProductNotExistsException{
        ProductResponseDTO producto = productService.buscarProductoPorId(unaLineaPedido.getIdProducto());
        return producto.getPrice() * unaLineaPedido.getCantidad();
    }

    public double precioTotalOrder(Order unaOrden) throws ProductNotExistsException{
        double precioOrden=0;
        for(OrderLine ol: unaOrden.getOrder()){
            precioOrden += this.precioLineaPedido(ol);
        }
        return precioOrden;
    }

    public List<Order> list(){
        return orderRepository.findAll();
    }

    public List<OrderResponseDTO> listarPedidos() throws OrderNotExistsException, ProductNotExistsException {
        List<OrderResponseDTO> listaOrderResponseDTO = new ArrayList<>();
        for(Order o : list()){
            OrderResponseDTO orderResponseDTO = buscarOrderReturnDTO(o.getIdPedido());
            listaOrderResponseDTO.add(orderResponseDTO);
        }
        return listaOrderResponseDTO;
    }

    public OrderLineResponseDTO orderLineToDTO(OrderLine unaOL) throws ProductNotExistsException {
        String titleProduct = productService.buscarProductoPorId(unaOL.getIdProducto()).getTitle();
        int cantidad = unaOL.getCantidad();
        double precioUnit = productService.buscarProductoPorId(unaOL.getIdProducto()).getPrice();
        double subTotal = precioUnit * cantidad;
        OrderLineResponseDTO OLRespDTO = new OrderLineResponseDTO(titleProduct, cantidad, precioUnit, subTotal);
    return OLRespDTO;
    }

    public List<OrderLineResponseDTO> listaOrderLineToDTO(List<OrderLine> listaOL) throws ProductNotExistsException{
        List<OrderLineResponseDTO> listOrderLineResponseDTO = new ArrayList<OrderLineResponseDTO>();
        for(OrderLine ol: listaOL){
            OrderLineResponseDTO olRespDTO = orderLineToDTO(ol);
            listOrderLineResponseDTO.add(olRespDTO);
        }
        return listOrderLineResponseDTO;
    }

    public List<OrderResponseDTO> ordenesMasCarasQue(double precioMinimo) throws OrderNotExistsException, ProductNotExistsException {
        List<OrderResponseDTO> ordenes = listarPedidos();
        List<OrderResponseDTO> ordenesCaras = new ArrayList<>();
        for(OrderResponseDTO o: ordenes){
            if(o.getTotal()>=precioMinimo){
                ordenesCaras.add(o);
            }
        }
        return ordenesCaras;

    }

    public List<OrderResponseDTO> ordenesMasBaratasQue(double precioMaximo) throws OrderNotExistsException, ProductNotExistsException {
        List<OrderResponseDTO> ordenes = listarPedidos();
        List<OrderResponseDTO> ordenesBaratas = new ArrayList<>();
        for(OrderResponseDTO o: ordenes){
            if(o.getTotal()<=precioMaximo){
                ordenesBaratas.add(o);
            }
        }
        return ordenesBaratas;
    }

    public Order crearPedido(List<OrderLineRequestDTO> nuevaOrden ) throws ProductNotExistsException, NoStockException {
        for(OrderLineRequestDTO lp: nuevaOrden){
            ProductResponseDTO unProducto = productService.buscarProductoPorId(lp.getIdProduct());
            if(!productService.hayStock(unProducto.getId(),lp.getCantidad())){
                throw new NoStockException("No hay stock de: " + unProducto.getTitle());
            }
        }

        for(OrderLineRequestDTO lp: nuevaOrden){
            productService.descontarStock(lp.getIdProduct(), lp.getCantidad());
            }


        List<OrderLine> lineasDeOrden = new ArrayList<>();

        for(OrderLineRequestDTO dto : nuevaOrden){
            OrderLine orderLine = new OrderLine();
            orderLine.setIdProducto(dto.getIdProduct());
            orderLine.setcantidad(dto.getCantidad());
            lineasDeOrden.add(orderLine);
        }

        Order ordenCreada = new Order();
        ordenCreada.setOrder(lineasDeOrden);
        orderRepository.save(ordenCreada);

        return ordenCreada;
        }

    public void agregarLineaPedidoAOrden(Long idOrder, Long idProducto, int cantidadPedida) throws ProductNotExistsException, OrderNotExistsException {

        ProductResponseDTO productoPedido = this.productService.buscarProductoPorId(idProducto);
        if (productoPedido == null) {
            throw new ProductNotExistsException("No existe el producto con Id: " + idProducto);
        }

        if (productService.hayStock(idProducto, cantidadPedida)) {
            OrderLine unaLineaPedido = new OrderLine();
            unaLineaPedido.setIdProducto(idProducto);
            unaLineaPedido.setcantidad(cantidadPedida);
            Order pedidoBuscado = this.buscarPedido(idOrder);
            pedidoBuscado.agregarLinea(unaLineaPedido);
            orderRepository.save(pedidoBuscado);
        }
    }

    public OrderResponseDTO buscarOrderReturnDTO(Long idOrder) throws OrderNotExistsException, ProductNotExistsException {
        Order ordenBuscada = buscarPedido(idOrder);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        double total = 0;
        for(OrderLine ol:ordenBuscada.getOrder()){
            ProductResponseDTO p = productService.buscarProductoPorId(ol.getIdProducto());
            double subtotal = p.getPrice()*ol.getCantidad();
            OrderLineResponseDTO olDTO = new OrderLineResponseDTO(p.getTitle(), ol.getCantidad(), p.getPrice(), subtotal);
            orderResponseDTO.addOrderLineResponseDTO(olDTO);
            total +=subtotal;
        }
        orderResponseDTO.setTotal(total);
        return orderResponseDTO;
    }

    public Order buscarPedido(Long id) throws OrderNotExistsException {
       Order ordenBuscada = orderRepository.findById(id).orElseThrow(()-> new OrderNotExistsException("Orden no encontrada con ID "+ id));
       return ordenBuscada;
    }


}
