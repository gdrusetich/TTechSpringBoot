package com.ProjectoJava.objetos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ProjectoJava.objetos.DTO.ProductResponseDTO;
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
    private ProductRepository productRepositoryJPA;

    @Autowired
    public ProductService(ProductRepository repositorio) {
        this.productRepositoryJPA = repositorio;
    }


    public Product agregarProducto(Product nuevoProducto) throws ProductExistsException {
        if (productRepositoryJPA.existsByNombreIgnoreCase(nuevoProducto.getNombre())){
            throw new ProductExistsException("Ya existe un producto con ese nombre");
        }
        productRepositoryJPA.save(nuevoProducto);
        return nuevoProducto;
    }

    public List<Product> listarProductos(){
        return productRepositoryJPA.findAll();
    }

    public Product buscarProductoPorId(long id) throws ProductNotExistsException {
        return productRepositoryJPA.findById(id)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + id));
    }


    public void eliminarProductoPorId(long id) throws ProductNotExistsException {
        if (!productRepositoryJPA.existsById(id)) {
            throw new ProductNotExistsException("Producto no encontrado con ID: " + id);
        }
        this.productRepositoryJPA.deleteById(id);
    }

    public boolean hayStock(long idProducto, int cantidad) throws ProductNotExistsException {
        Product producto = buscarProductoPorId(idProducto);
        return producto.getStock() >= cantidad;
    }

    public void descontarStock(long idProducto, int cantidad) throws ProductNotExistsException, NoStockException {
        Product producto = buscarProductoPorId(idProducto);
        if (producto.getStock() < cantidad) {
            throw new NoStockException("No hay suficiente stock para: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        productRepositoryJPA.save(producto); // actualiza en la DB
    }

    public Product editarPrecio(Long id, Double nuevoPrecio) throws ProductNotExistsException {
        Product encontrado = this.buscarProductoPorId(id);
        encontrado.setPrecio(nuevoPrecio);

        this.productRepositoryJPA.save(encontrado);
        return encontrado;
    }



}



