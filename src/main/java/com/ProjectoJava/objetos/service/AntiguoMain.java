package com.ProjectoJava.objetos.service;

public class AntiguoMain {

    /*
    * package com.ProjectoJava.objetos;

import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.OrderRepository;
import com.ProjectoJava.objetos.entity.Product;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import exceptions.ProductExistsException;

import com.ProjectoJava.objetos.service.Menu;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClaseSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaseSpringbootApplication.class, args);

		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu();
		boolean salir = false;
		//menu.mostrarMenu();

		while(!salir){
			menu.mostrarMenu();
			int opcionElegida = Integer.parseInt(scanner.nextLine());

			switch(opcionElegida){
				case 1 :
					System.out.println("Ingrese el nombre del Producto.");
					String nombreDelProducto = scanner.nextLine();

					System.out.println("Ingrese el precio del Producto.");
					double precioDelProducto = scanner.nextDouble();
					scanner.nextLine(); // Limpia el buffer

					System.out.println("Ingrese el stock del Producto.");
					int stockDelProducto = scanner.nextInt();
					scanner.nextLine(); // Limpia el buffer

					Product nuevoProducto = new Product(nombreDelProducto, precioDelProducto, stockDelProducto);
					try{
						ProductRepository.getInstancia().agregarProducto(nuevoProducto);
					} catch (ProductExistsException e) {
						System.out.println("Error: "+e.getMessage());
					}
					break;
				case 2:
					ProductRepository.getInstancia().mostrarTodosLosProductos();
					break;
				case 3:
					System.out.println("Ingrese el nombre del producto a actualizar.");
					String nombreProducto = scanner.nextLine();
					//scanner.nextLine();
					try{
						Product producto = ProductRepository.getInstancia().buscarProducto(nombreProducto);
						if(producto != null) {
							System.out.println("Ingrese su nuevo nombre.");
							String nuevoNombre = scanner.nextLine();
							//scanner.nextLine();

							System.out.println("Ingrese su nuevo precio.");
							double nuevoPrecio = scanner.nextDouble();
							//scanner.nextLine();

							System.out.println("Ingrese su nuevo stock.");
							int nuevoStock = scanner.nextInt();
							scanner.nextLine();

							producto.setNombre(nuevoNombre);
							producto.setPrecio(nuevoPrecio);
							producto.setStock(nuevoStock);
							break;
						}
					} catch (ProductNotExistsException e){
						e.getMessage();
					}
					break;

				case 4:
					System.out.println("Ingrese el Id del producto a eliminar.");
					int idProducto = scanner.nextInt();
					scanner.nextLine();
					try {
						ProductRepository.getInstancia().eliminarProductoPorId(idProducto);
					} catch(ProductNotExistsException e) {
						System.out.println("Error: "+e.getMessage());
					}
					break;
				case 5:
					try {
						OrderRepository.getInstancia().nuevoPedido();
					} catch (ProductNotExistsException productoNoexistente) {
						System.out.println(productoNoexistente.getMessage());

					} catch (NoStockException noHayStock){
						System.out.println(noHayStock.getMessage());
					}
					break;
				case 6:
					OrderRepository.getInstancia().mostrarTodosLosPedidos();
					break;
				case 7:
					salir = true;
					System.out.println("Saliendo del programa...");
					break;
				default:
					System.out.println("Opción no válida. Intente nuevamente.");
			}
		}

	}

}

    *
    * */
}
