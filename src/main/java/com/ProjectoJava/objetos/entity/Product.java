package com.ProjectoJava.objetos.entity;

import exceptions.NoStockException;

public class Product {
    public Product(String unNombre, double unPrecio, int unStock){
        contadorProductos++;
        this.idProducto = contadorProductos;
        this.nombre = unNombre;
        this.precio = unPrecio;
        this.stock = unStock;
    }

    private static int contadorProductos = 0;

    private int idProducto;
    private String nombre;
    private double  precio;
    private int stock;

    public void descontarStock(int cantidad) throws NoStockException {
        if(this.getStock()<cantidad){
            throw new NoStockException("No hay stock de: "+ this.getNombre());
        }
        this.stock -=cantidad;
    }

    public int getId(){return this.idProducto;}
    public String getNombre(){return this.nombre;}
    public double getPrecio( ){return this.precio;}
    public int getStock( ){return this.stock;}

    public void setNombre(String nuevoNombre){this.nombre = nuevoNombre;}
    public void setPrecio(double nuevoPrecio){this.precio = nuevoPrecio;}
    public void setStock(int nuevoStock){this.stock = nuevoStock;}




    public String mostrarInfo(){
       return this.getNombre() +" "+ this.getPrecio()+" "+this.getStock();

    }

    double precioConDescuento(){
        return precio - precio * 0.2;
    }

    String capitalize(){
        String textoMinuscula = nombre.toLowerCase();
        String textoSinEspacios = textoMinuscula.trim();
        String[] palabras = textoSinEspacios.split(" ");
        String resultado = "";
        for(int i = 0; i<palabras.length; i++){
            String palabra = palabras[i];
            resultado += palabra.substring(0, 1).toUpperCase() + palabra.substring(1) + " ";
        }
        return resultado;
    }

}