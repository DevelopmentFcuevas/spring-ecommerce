package py.com.housesolutions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.ProductDTO;
import py.com.housesolutions.model.UserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private DetalleOrdenService detalleOrdenService;

    //Lista para almacenar los diferentes detalles de una orden.
    private List<DetalleOrdenDTO> detalleOrdenDTOS = new ArrayList<>(); //es una variable global
    //Para almacenar datos de la orden
    private OrdenDTO ordenDTO = new OrdenDTO();

    @Override
    public OrdenDTO addToCart(Long productId, Integer cantidad) throws Exception {
        log.info("CartService-addToCart::Iniciando Servicio para agregar productos al carrito de compras");

        DetalleOrdenDTO detalleOrdenDTO = new DetalleOrdenDTO();
        double sumaTotal = 0;

        try {
            // Buscar si existe el producto que se quiere agregar al carrito.
            Optional<ProductDTO> optionalProductDTO = productoService.get(productId);

            // Validar si el producto ya está en el carrito
            Optional<DetalleOrdenDTO> productoExistente = detalleOrdenDTOS.stream()
                    .filter(p -> p.getProductDTO().getId().equals(productId))
                    .findFirst();

            if (productoExistente.isPresent()) {
                // Si el producto ya está en el carrito, actualizamos la cantidad y el total
                DetalleOrdenDTO detalleExistente = productoExistente.get();
                detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad); // Sumamos la cantidad nueva
                detalleExistente.setTotal(detalleExistente.getPrecio() * detalleExistente.getCantidad());
                log.info("Producto actualizado en el carrito: {}, Cantidad: {}", optionalProductDTO.get().getNombre(), detalleExistente.getCantidad());
            } else {
                // Si el producto no está en el carrito, lo agregamos como un nuevo detalle
                detalleOrdenDTO.setCantidad(cantidad);
                detalleOrdenDTO.setNombre(optionalProductDTO.get().getNombre());
                detalleOrdenDTO.setPrecio(optionalProductDTO.get().getPrecio());
                detalleOrdenDTO.setTotal(optionalProductDTO.get().getPrecio() * cantidad);
                detalleOrdenDTO.setProductDTO(optionalProductDTO.get());
                detalleOrdenDTOS.add(detalleOrdenDTO);
                log.info("Producto agregado al carrito: {}, Cantidad: {}", optionalProductDTO.get().getNombre(), cantidad);
            }

            // Sumar el total de los productos en el carrito
            sumaTotal = detalleOrdenDTOS.stream().mapToDouble(dt -> dt.getTotal()).sum();

            // Recalcular el total de todos los productos en el carrito
            ordenDTO.setTotal(sumaTotal);
            log.info("Total actualizado del carrito: " + sumaTotal);

            log.info("CartService-addToCart::Acción completada sin errores.");
            return ordenDTO;
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("CartService-addToCart::Error en el Service al agregar Productos al carrito ", e);
            throw new Exception("Error al Intentar agregar Productos al carrito de compras. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Override
    // Retornar el carrito actual
    public List<DetalleOrdenDTO> getCart() throws Exception {
        try {
            log.info("CartService-getCart::Iniciando Servicio para obtener el detalle del carrito de compras");
            log.info("CartService-getCart::Acción completada sin errores.");
            return detalleOrdenDTOS;
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("CartService-getCart::Error en el Service al obtener el detalle del carrito de compras ", e);
            throw new Exception("Error al Intentar obtener los detalles del carrito de compras. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Override
    // Limpiar el carrito
    public void clearCart() {
        detalleOrdenDTOS.clear();
        ordenDTO.setTotal(0);
    }


    /*
    * deleteFromCart(Long productId):
    * - Recorre la lista de detalleOrdenDTOS (tu carrito) y crea una nueva lista excluyendo
    * el producto que tiene el productId que se quiere eliminar.
    * - Luego, actualiza la lista detalleOrdenDTOS con los productos restantes.
    * - Calcula el nuevo total de la orden y lo actualiza en ordenDTO.
    * - Finalmente, devuelve el objeto OrdenDTO actualizado con el nuevo total.
    * - Registro de Logs: Hemos agregado log.info para registrar cuando un producto
    * es eliminado y cuál es el nuevo total, para ayudar con la depuración.
    * */
    @Override
    public OrdenDTO deleteFromCart(Long productId) throws Exception {
        try {
            log.info("CartService-deleteFromCart::Iniciando Servicio para eliminar producto del carrito de compras");

            // Lista nueva de productos sin el producto a eliminar
            List<DetalleOrdenDTO> ordenesNuevas = new ArrayList<>();

            // Validamos que exista el producto en la lista
            boolean productFound = false;
            for (DetalleOrdenDTO detalleOrdenDTO : detalleOrdenDTOS) {
                if (!detalleOrdenDTO.getProductDTO().getId().equals(productId)) {
                    ordenesNuevas.add(detalleOrdenDTO);
                } else {
                    productFound = true;
                }
            }
            if (!productFound) {
                log.error("CartService-deleteFromCart::Error en el Service, Producto con el ID: {}, No encontrado ", productId);
                throw new Exception("Error al Intentar eliminar Producto del carrito de compras. No se encontró el producto buscado \nPor favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
            }

            // Actualizar la lista de productos en el carrito
            detalleOrdenDTOS = ordenesNuevas;

            // Calcular nuevamente el total de la orden
            double sumaTotal = detalleOrdenDTOS.stream().mapToDouble(dt -> dt.getTotal()).sum();
            ordenDTO.setTotal(sumaTotal);
            log.info("Producto con ID {} eliminado. Total actualizado: {}", productId, sumaTotal);

            log.info("CartService-deleteFromCart::Acción completada sin errores.");
            // Retornar la orden actualizada
            return ordenDTO;
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("CartService-deleteFromCart::Error en el Service al eliminar Producto del carrito ", e);
            throw new Exception("Error al Intentar eliminar Producto del carrito de compras. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    /*
    * getOrdenDTO()
    * - Recalcular el total: Cada vez que se llama a getOrdenDTO(), se recalcula el total sumando los
    * totales de cada DetalleOrdenDTO que se encuentran en el carrito (detalleOrdenDTOS).
    * - Retornar ordenDTO: Después de recalcular el total, este valor se asigna al campo total de
    * ordenDTO y se retorna el objeto actualizado.
    * - Control de errores: Se incluye un bloque try-catch para manejar cualquier excepción que
    * pueda ocurrir al calcular el total.
    * */
    @Override
    public OrdenDTO getOrden() throws Exception {
        try {
            log.info("CartService-getOrden::Iniciando Servicio para obtener una orden(cabecera) de compras del carrito");
            // Recalcular el total en caso de que haya cambios en el carrito
            double sumaTotal = detalleOrdenDTOS.stream()
                    .mapToDouble(DetalleOrdenDTO::getTotal)
                    .sum();
            ordenDTO.setTotal(sumaTotal);
            log.info("CartService-getOrden::Acción completada sin errores.");
            return ordenDTO;
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("CartService-getOrden::Error en el Service al obtener orden del carrito ", e);
            throw new Exception("Error al obtener la Orden de compra. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Override
    public void saveOrder(Long userId) throws Exception {
        try {
            log.info("CartService-saveOrder::Persistir en la Base de datos la orden de compra");

            // Crear una nueva Orden
            OrdenDTO orden = new OrdenDTO();

            // Asignar el número secuencial para la orden
            orden.setNumero(ordenService.generarNumeroOrden());
            // Asignar fecha de creacion o de compra.
            orden.setFechaCreacion(new Date());

            // Calcular el total de la orden sumando los totales de cada detalle
            double totalOrden = detalleOrdenDTOS.stream()
                    .mapToDouble(DetalleOrdenDTO::getTotal)
                    .sum();
            orden.setTotal(totalOrden); // Asignar el total calculado a la orden

            // Obtener el usuario
            UserDTO userDTO = usuarioService.findById(userId);
            orden.setUserDTO(userDTO);

            // Guardar la orden en la base de datos
            OrdenDTO ordenGuardada = ordenService.save(orden);

            // Guardar los detalles de la orden
            for (DetalleOrdenDTO detalleDTO : detalleOrdenDTOS) {
                DetalleOrdenDTO detalleOrden = new DetalleOrdenDTO();

                detalleOrden.setNombre(detalleDTO.getNombre());
                detalleOrden.setCantidad(detalleDTO.getCantidad());
                detalleOrden.setPrecio(detalleDTO.getPrecio());
                detalleOrden.setTotal(detalleDTO.getTotal());
                detalleOrden.setOrdenDTO(ordenGuardada);
                detalleOrden.setProductDTO(detalleDTO.getProductDTO());

                // Guardar cada detalle en la base de datos
                detalleOrdenService.save(detalleOrden);
            }

            // Limpiar la lista y resetear la orden
            detalleOrdenDTOS.clear();
            ordenDTO.setTotal(0);
            log.info("CartService-saveOrder::Acción completada sin errores.");
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("CartService-saveOrder::Error en el Service al guardar la orden del carrito ", e);
            throw new Exception("Error al guardar la orden(pedido) del carrito. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

}
