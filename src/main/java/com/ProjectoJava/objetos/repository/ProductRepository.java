package com.ProjectoJava.objetos.repository;
import exceptions.ProductNotExistsException;
import exceptions.ProductExistsException;
import com.ProjectoJava.objetos.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ProductRepository {

    ArrayList<Product> todosLosProductos = new ArrayList<>();


    public void agregarProducto(Product unProducto) throws ProductExistsException {
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

    public void mostrarInfoDeTodosLosProductos(){
        for(Product producto : todosLosProductos){
            producto.mostrarInfo();
        }
    }


    public String mostrarInfoDeUnProducto(int idProductoSolicitado) throws ProductNotExistsException{
        Product productoEncontrado = this.buscarProductoPorId(idProductoSolicitado);
        if(productoEncontrado == null ){
            throw new ProductNotExistsException("No existe ese producto");
        } else {
            return productoEncontrado.mostrarInfo();
        }
    }




    public Product mostrarProducto(String nombre){
        for(Product p : todosLosProductos){
            if(p.getNombre().equalsIgnoreCase(nombre)){
                p.mostrarInfo();
                return p;
            }
        }
        System.out.println("No se encontró el producto " + nombre);
        return null;
    }

    public Product buscarProducto(String nombre) throws ProductNotExistsException{
        for(Product p : todosLosProductos){
            if(p.getNombre().equalsIgnoreCase(nombre)){
                return p;
            }
        }
        return null;
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

    public boolean existeProducto(String nombre){
        for(Product p : todosLosProductos){
            if(p.getNombre().equalsIgnoreCase(nombre)){
                return true;
            }
        }
        return false;
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