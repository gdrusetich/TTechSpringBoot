package com.ProjectoJava.objetos.service;

import java.util.ArrayList;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.repository.ProductRepository;
import exceptions.NoStockException;
import exceptions.ProductExistsException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//DATOS DE SPRING BOOT
 //BEAN 1: ProductService
 //BEAN 2:


@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository repositorio) {
        this.productRepository = repositorio;
    }

    public void agregarProducto(Product nuevoProducto) throws ProductExistsException {
        productRepository.agregarProducto(nuevoProducto);
    }

    public ArrayList<Product> listarProductos(){
        return productRepository.getAllProducts();
    }

    public Product buscarProductoPorId(int id) throws ProductNotExistsException {
        return productRepository.buscarProductoPorId(id);
    }

    public void eliminarProductoPorId(int id) throws ProductNotExistsException {
        productRepository.eliminarProductoPorId(id);
    }

    public boolean hayStock(int idProducto, int cantidad) throws ProductNotExistsException {
        return productRepository.hayStock(idProducto, cantidad);
    }

    public void descontarStock(int idProducto, int cantidad) throws ProductNotExistsException, NoStockException {
        buscarProductoPorId(idProducto).descontarStock(cantidad);
    }



}



