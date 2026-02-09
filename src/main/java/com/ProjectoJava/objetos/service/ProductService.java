package com.ProjectoJava.objetos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ProjectoJava.objetos.DTO.request.ProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.ImageRepository;
import exceptions.NoStockException;
import exceptions.ProductExistsException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepositoryJPA;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository repositorio) {
        this.productRepositoryJPA = repositorio;
    }

    public ProductResponseDTO agregarProducto(ProductRequestDTO nuevoDTO) throws ProductExistsException {
        // 1. Validar existencia
        if (productRepositoryJPA.existsByTitleIgnoreCase(nuevoDTO.getTitle())){
            throw new ProductExistsException("Ya existe un producto con ese titulo");
        }

        // 2. Mapear DTO a Entidad
        Product productoNuevo = new Product();
        productoNuevo.setTitle(nuevoDTO.getTitle());
        productoNuevo.setPrice(nuevoDTO.getPrice());
        productoNuevo.setStock(nuevoDTO.getStock());
        productoNuevo.setDescription(nuevoDTO.getDescription()); // ¡No te olvides de la descripción!

        // 3. Cargar Categorías
        List<Category> categoriasEncontradas = categoryRepository.findAllById(nuevoDTO.getCategories());
        productoNuevo.setCategories(new HashSet<>(categoriasEncontradas));

        // 4. GUARDAR el producto primero (para generar el ID)
        Product productoGuardado = productRepositoryJPA.save(productoNuevo);

        // 5. GUARDAR las imágenes vinculadas al producto guardado
        if (nuevoDTO.getImageURL() != null && !nuevoDTO.getImageURL().isEmpty()) {
            for (String nombre : nuevoDTO.getImageURL()) {
                Image img = new Image();
                img.setUrl(nombre); 
                img.setProduct(productoGuardado); // Vinculamos la foto al ID del producto
                imageRepository.save(img); 
            }
        }

        // 6. Retornar la respuesta (fuera del if de las imágenes)
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

    public List<Category> obtenerCategoriasDeProducto(Long productId) {
        Product producto = productRepositoryJPA.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return new ArrayList<>(producto.getCategories());
    }

    public List<ProductResponseDTO> filtrarPorCategoria(Long categoryId) {
        List<Long> idsFiltrar = new ArrayList<>();
        idsFiltrar.add(categoryId);
        
        categoryRepository.findByParent_Id(categoryId)
                        .forEach(hija -> idsFiltrar.add(hija.getId()));

        List<Product> productos = productRepositoryJPA.findByCategories_IdIn(idsFiltrar);
        
        return productos.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }
 

    public Map<String, List<Category>> obtenerMapaCategoriasPorProducto(Long productId) {
        Product producto = productRepositoryJPA.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Map<String, List<Category>> mapaResultados = new HashMap<>();

        for (Category cat : producto.getCategories()) {
            mapaResultados.put(cat.getName(), categoryService.obtenerAncestros(cat.getId()));
        }

        return mapaResultados;
    }


    public ProductResponseDTO buscarProductoPorId(Long id) throws ProductNotExistsException {
        Product productoBuscado = productRepositoryJPA.findById(id)
                .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + id));
        return new ProductResponseDTO(productoBuscado);
    }

    public ProductResponseDTO actualizarProducto(long id, ProductRequestDTO PRDTO) throws ProductNotExistsException{
        Product productoExistente = productRepositoryJPA.findById(id)
            .orElseThrow(() -> new ProductNotExistsException("Producto no encontrado con ID: " + id));

        if (PRDTO.getTitle() != null && !PRDTO.getTitle().isEmpty()) productoExistente.setTitle(PRDTO.getTitle());
        if (PRDTO.getPrice() > 0) productoExistente.setPrice(PRDTO.getPrice());
        if (PRDTO.getStock() >= 0) productoExistente.setStock(PRDTO.getStock());
        if (PRDTO.getDescription() != null && !PRDTO.getDescription().isEmpty()) productoExistente.setDescription(PRDTO.getDescription());

        List<Category> categoriasEncontradas = categoryRepository.findAllById(PRDTO.getCategories());
        productoExistente.setCategories(new HashSet<>(categoriasEncontradas));

        if (PRDTO.getImageURL() != null && !PRDTO.getImageURL().isEmpty()) {
                // Solo actualizamos el nombre si el DTO trae una imagen nueva
            productoExistente.getImages().clear();
            for(String nombreArchivo : PRDTO.getImageURL()){
                Image nuevaImagen = new Image();
                nuevaImagen.setUrl(nombreArchivo);
                nuevaImagen.setProduct(productoExistente); // Vínculo bidireccional
                productoExistente.getImages().add(nuevaImagen);
            }
            
            }
            Product productoActualizado = productRepositoryJPA.save(productoExistente);
            return new ProductResponseDTO(productoActualizado);
    }

    public void eliminarProductoPorId(long id) throws ProductNotExistsException{
        if(!productRepositoryJPA.existsById(id)){
            throw new ProductNotExistsException("Producto no encontrado con ID: "+ id);
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

    @Transactional
    public void establecerImagenPrincipal(Long productoId, Long imagenId) {
        Product producto = productRepositoryJPA.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Image imagen = imageRepository.findById(imagenId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        producto.setMainImage(imagen);
        productRepositoryJPA.save(producto);
    }

}



