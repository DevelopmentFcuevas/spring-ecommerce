package py.com.housesolutions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import py.com.housesolutions.domain.Producto;
import py.com.housesolutions.model.ProductDTO;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.repository.ProductoRepository;
import py.com.housesolutions.util.ProductAlreadyExistsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService {
    /*
     * Servicios en Spring Boot: Un servicio en Spring Boot es
     * una clase que implementa una o más interfaces. Es donde se
     * escribe la lógica de negocio real. Los servicios suelen
     * acceder a la base de datos, realizar cálculos, llamar
     * a otros servicios, etc.
     *
     * dto: Se crean "dto" para manejar la transferencia de datos entre
     * la capa de presentación y la lógica de negocio.
     * Los @Service usan "DTO's" para recibir y devolver datos entre los
     * controladores y la capa de persistencia.
     *
     * Mapeo: Se utiliza un servicio de mapeo[toDto(),toEntity()] para conversion entre @Entity y DTO.
     *
     * Utilización de @Transactional:
     * - Puntos clave del uso de @Transactional
     * Atomicidad: Si un método anotado con @Transactional falla en cualquier punto, toda la transacción
     * se revertirá (rollback).
     * Read-Only: Puedes especificar @Transactional(readOnly = true) en métodos que solo realizan
     * operaciones de lectura. Esto optimiza el rendimiento al evitar bloqueos innecesarios en la base de datos.
     *
     */

    @Autowired
    ProductoRepository repository;
    @Autowired
    private UsuarioService usuarioService;
    private String folder = "images//";

    /*
     * Mapeo entre DTO y Entidades: Utiliza un servicio o una librería como "MapStruct" para mapear
     * entre DTO y Entidades.
     */
    @Override
    public ProductDTO toDto(Producto producto) {
        // Mapear los campos...
        ProductDTO dto = new ProductDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setImagen(producto.getImagen());
        dto.setPrecio(producto.getPrecio());
        dto.setCantidad(producto.getCantidad());
        //Setear Usuario
        UserDTO usuario = usuarioService.findById(producto.getUsuario().getId());
        if (usuario != null) {
           dto.setUserDTO(usuario);
        }
        return dto;
    }

    @Override
    public Producto toEntity(ProductDTO dto) {
        // Mapear los campos...
        Producto entity = new Producto();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setImagen(dto.getImagen());
        entity.setPrecio(dto.getPrecio());
        entity.setCantidad(dto.getCantidad());

        //Setear Usuario
        UserDTO usuario = usuarioService.findById(dto.getUserDTO().getId());
        if (usuario != null) {
            entity.setUsuario( usuarioService.toEntity(usuario)  );
        }

        return entity;
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte [] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename(); //retorna la imagen existente.
        }

        return "default.jpg"; //retorna el nombre de la imagen por defecto.
    }

    public void deleteImage(String nombre) {
        String ruta = "images//";
        File file = new File(ruta + nombre);
        file.delete();
    }

    //@Override
    //public List<Producto> findAll() {
    //return repository.findAll();
    //}
    /*@Override
    public List<ProductDTO> findAll() {
        List<Producto> productos = repository.findAll();
        //List<ProductDTO> productDTOs = new ArrayList<>();
        //for (Producto producto : productos) {
            //ProductDTO dto = toDto(producto);
            //productDTOs.add(dto);
        //}
        //return productDTOs;
        return productos
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }*/
    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAll() throws Exception {
        try {
            log.info("ProductoService-findAll::Iniciando Servicio para obtener listado de productos");
            List<Producto> productos = repository.findAll();
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Producto producto : productos) {
                ProductDTO dto = toDto(producto);
                productDTOS.add(dto);
            }
            log.info("ProductoService-findAll::Acción completada sin errores.");
            return productDTOS;
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("ProductoService-findAll::Error en el Service al obtener el listado de Productos ", e);
            throw new Exception("Error al obtener el Listado de Productos. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Transactional
    @Override
    public ProductDTO save(ProductDTO productDTO, MultipartFile file) throws Exception {
        try {
            log.info("ProductoService-save::Persistir en la Base de datos el producto");
            // Guardar la imagen y obtener el nombre
            String imageName = saveImage(file);
            productDTO.setImagen(imageName);

            Producto producto = toEntity(productDTO);
            Producto savedProducto = repository.save(producto);
            log.info("ProductoService-save::Acción completada sin errores");
            return toDto(savedProducto);
        } catch (DataIntegrityViolationException e) {
            log.error("Error en el ProductoService al guardar Producto: violación de integridad de datos", e);
            throw new ProductAlreadyExistsException("El Nombre: '"+productDTO.getNombre()+"' ya está en uso.");
        } catch (Exception e) {
            // Lanzar una excepción personalizada o regresar un valor opcional
            log.error("Error en el ProductoService al guardar Producto", e);
            throw new Exception("Error al guardar el producto", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductDTO> get(Long id) throws Exception {
        try {
            log.info("ProductoService-get::Iniciando Servicio para obtener Producto buscado por ID");
            Optional<Producto> producto = repository.findById(id);
            if (producto.isEmpty()) {
                log.error("ProductoService-get::Error en el Service, Producto con el ID: {}, No encontrado ", id);
                throw new Exception("Error al realizar la búsqueda del Producto. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
            }

            log.info("ProductoService-get::Acción completada sin errores");
            return producto.map(this::toDto);
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("ProductoService-get::Error en el Service al buscar Producto ", e);
            throw new Exception("Error al realizar la búsqueda del Producto. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Transactional
    @Override
    public ProductDTO update(Long id, ProductDTO productDTO, MultipartFile file) throws Exception {
        log.info("ProductoService-get::Iniciando la operación para actualizar producto con ID: {}", id);
        try {
           Producto productoExistente = repository.findById(id)
                   .orElseThrow(() -> new Exception("Producto no encontrado con ID: "+id));

           productoExistente.setNombre(productDTO.getNombre());
           productoExistente.setDescripcion(productDTO.getDescripcion());
           //productoExistente.setImagen(productDTO.getImagen());
           productoExistente.setPrecio(productDTO.getPrecio());
           productoExistente.setCantidad(productDTO.getCantidad());

            if (file.isEmpty()) { //editamos el producto, pero no cambiamos la imagen
                productoExistente.setImagen(productoExistente.getImagen());
            } else {
                //eliminar cuando no sea la imagen por defecto.
                if (!productoExistente.getImagen().equals("default.jpg")) {
                    deleteImage(productoExistente.getImagen());
                }

                String nombreImagen = saveImage(file);
                productoExistente.setImagen(nombreImagen);
            }

            Producto updatedProducto = repository.save(productoExistente);
            log.info("ProductoService-update::Acción completada sin errores");
            return toDto(updatedProducto);
        } catch (Exception e) {
            log.error("ProductoService-update::Error al al intentar actualizar el producto con ID: " + id, e);
            throw new Exception("Error en el Servicio al actualizar Producto con el Identificador Proporcionado: " + id);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) throws Exception {
        try {
            log.info("ProductoService-delete::Iniciando la operación para eliminar producto con ID: {}", id);
            Optional<Producto> producto = repository.findById(id);
            if (producto.isEmpty()) {
                log.info("Producto con ID: {}, no encontrado.", id);
                throw new Exception("Producto no encontrado con el ID: " + id);
            }

            //eliminar cuando no sea la imagen por defecto.
            if (!producto.get().getImagen().equals("default.jpg")) {
                deleteImage(producto.get().getImagen());
            }

            repository.deleteById(id);
            log.info("ProductoService-delete::Acción completada sin errores");
        } catch (Exception e) {
            log.error("ProductoService-delete::Error al eliminar el producto con ID: " + id, e);
            throw new Exception("Error en el Servicio al eliminar Producto con el Identificador Proporcionado: " + id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductDTO> getByNombre(String nombre) throws Exception {
        try {
            log.info("ProductoService-getByNombre::Iniciando Servicio para obtener Producto buscado por Nombre");
            Optional<Producto> producto = repository.findByNombre(nombre);
            if (producto.isEmpty()) {
                log.error("ProductoService-getByNombre::Error en el Service, Producto con el Nombre: {}, No encontrado ", nombre);
                throw new Exception("Error al realizar la búsqueda del Producto. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
            }

            log.info("ProductoService-getByNombre::Acción completada sin errores");
            return producto.map(this::toDto);
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("ProductoService-getByNombre::Error en el Service al buscar Producto ", e);
            throw new Exception("Error al realizar la búsqueda del Producto. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

}
