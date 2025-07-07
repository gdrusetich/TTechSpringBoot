package com.ProjectoJava.objetos.repository;
import exceptions.ProductNotExistsException;
import exceptions.ProductExistsException;
import com.ProjectoJava.objetos.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ProductRepositoryAntiguo {

    ArrayList<Product> todosLosProductos = new ArrayList<>();


    public void add(Product unProducto) throws ProductExistsException {
        for(Product productoExistente : todosLosProductos){
            if(productoExistente.getNombre().equalsIgnoreCase(unProducto.getNombre())){
                throw new ProductExistsException("Ya existe un producto con ese nombre.");
            }
        }
        this.todosLosProductos.add(unProducto);
    }

    public ArrayList<Product> getAllProducts(){
        return todosLosProductos;
    }

    public Product buscarProductoPorId(int id) throws ProductNotExistsException{
        for(Product p : todosLosProductos){
            if(p.getId() == id) {
                return p;
            }
        }
        {
            throw new ProductNotExistsException("No existe el producto con Id: " + id);
        }
    }


    public boolean hayStock(int idProductoSolicitado, int stockSolicitado) throws ProductNotExistsException{
        Product productoEncontrado = this.buscarProductoPorId(idProductoSolicitado);
        if(productoEncontrado.getStock() >= stockSolicitado){
            return true;
        } else {
            return false;
        }
    }

    public void eliminarProductoPorId(int id) throws ProductNotExistsException {
        this.buscarProductoPorId(id);
        if(this.buscarProductoPorId(id) == null){
            throw new ProductNotExistsException("No existe ese producto");
        }
      else
        {
            todosLosProductos.remove(this.buscarProductoPorId(id));
        }
    }
}