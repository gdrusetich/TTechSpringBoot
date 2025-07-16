package com.ProjectoJava.objetos.service;

import java.util.ArrayList;
import java.util.List;

import com.ProjectoJava.objetos.DTO.request.ProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.repository.ProductRepository;
import exceptions.NoStockException;
import exceptions.ProductExistsException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private ProductRepository productRepositoryJPA;

    @Autowired
    public ProductService(ProductRepository repositorio) {
        this.productRepositoryJPA = repositorio;
    }


    public ProductResponseDTO agregarProducto(ProductRequestDTO nuevoDTO) throws ProductExistsException {
        if (productRepositoryJPA.existsByTitleIgnoreCase(nuevoDTO.getTitle())){
            throw new ProductExistsException("Ya existe un producto con ese titulo");
        }
        Product productoNuevo = new Product();
        productoNuevo.setTitle(nuevoDTO.getTitle());
        productoNuevo.setPrice(nuevoDTO.getPrice());
        productoNuevo.setStock(nuevoDTO.getStock());
        Product productoGuardado = productRepositoryJPA.save(productoNuevo);
        return new ProductResponseDTO(productoGuardado);
    }

    public List<ProductResponseDTO> listarProductos(){
        List<Product> listaDeProductos = productRepositoryJPA.findAll();
        List<ProductResponseDTO> listaDTO = new ArrayList<>();
        for(Product p: listaDeProductos){
            ProductResponseDTO nuevoDTO = new ProductResponseDTO(p);
            listaDTO.add(nuevoDTO);
        }
        return listaDTO;
    }

    public List<ProductResponseDTO> filtrarPorPrecio(double precioMaximo){
        List<Product> listaDeProductos = productRepositoryJPA.findAll();
        List<ProductResponseDTO> productosFiltrados = new ArrayList<>();
        for(Product p: listaDeProductos){
            if(p.getPrice()<=precioMaximo) {
                ProductResponseDTO nuevoDTO = new ProductResponseDTO(p);
                productosFiltrados.add(nuevoDTO);
            }
        }
        return productosFiltrados;
    }



    public ProductResponseDTO buscarProductoPorId(Long id) throws ProductNotExistsException {
        Product productoBuscado = productRepositoryJPA.findById(id)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + id));

        return new ProductResponseDTO(productoBuscado);
    }


    public void eliminarProductoPorId(long id) throws ProductNotExistsException {
        if (!productRepositoryJPA.existsById(id)) {
            throw new ProductNotExistsException("Producto no encontrado con ID: " + id);
        }
        this.productRepositoryJPA.deleteById(id);
    }

    public boolean hayStock(Long idProducto, int cantidad) throws ProductNotExistsException {
        Product productoBuscado = productRepositoryJPA.findById(idProducto)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + idProducto));
        return productoBuscado.getStock() >= cantidad;
    }

    public void descontarStock(long idProducto, int cantidad) throws ProductNotExistsException, NoStockException {
        Product productoADescontar = productRepositoryJPA.findById(idProducto)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + idProducto));
        if (productoADescontar.getStock() < cantidad) {
            throw new NoStockException("No hay suficiente stock para: " + productoADescontar.getTitle());
        }
        productoADescontar.setStock(productoADescontar.getStock() - cantidad);
        productRepositoryJPA.save(productoADescontar); // actualiza en la DB
    }

    public Product editarPrecio(Long id, Double nuevoPrecio) throws ProductNotExistsException {
        Product productoAACtualizar = productRepositoryJPA.findById(id)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + id));
        productoAACtualizar.setPrice(nuevoPrecio);

        this.productRepositoryJPA.save(productoAACtualizar);
        return productoAACtualizar;
    }



}



