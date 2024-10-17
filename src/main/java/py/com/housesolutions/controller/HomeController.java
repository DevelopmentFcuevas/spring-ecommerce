package py.com.housesolutions.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.ProductDTO;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.service.CartService;
import py.com.housesolutions.service.ProductoService;
import py.com.housesolutions.service.UsuarioService;
import py.com.housesolutions.util.ViewNames;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {
    /*
    * Home del Usuario
    * */
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        try {
            log.info("HomeController-home::Obteniendo el listado de Productos para la vista de los usuarios");
            //model.addAttribute("session", session.getAttribute("idUsuario"));
            // Obtener el ID del usuario de la sesión
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario != null) {
                // Buscar el usuario en la base de datos
                UserDTO userDTO = usuarioService.findById(idUsuario);
                model.addAttribute("usuario", userDTO);
            } else {
                model.addAttribute("usuario", null);
            }
            model.addAttribute("productos", productoService.findAll());
        } catch (Exception e) {
            log.error("HomeController-home::Error en el Controller al obtener el listado de Productos ", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
        }

        return ViewNames.HOME_VIEW;
    }

    @GetMapping("productohome/{id}")
    public String productHome(@PathVariable Long id, Model model) throws Exception {
        try {
            log.info("HomeController-productHome::Buscar Producto por ID y renderizar en la vista del usuario.");
            //log.info("Id enviado como parámetro: {}", id);

            Optional<ProductDTO> productDTO = productoService.get(id);
            model.addAttribute("producto", productDTO.get());
            return ViewNames.PRODUCT_HOME_VIEW;
        } catch (Exception e) {
            log.error("HomeController-productHome::Error en el Controller al buscar Producto por ID: " + id, e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW; //Mostrar el error en la vista del usuario.
        }
    }

    /*
     * Petición ejecutada al momento de hacer clic en el botón de "Añadir al carrito"
     * en la view "productohome".
     * cantidad: Nos permite saber cuantas unidades va a comprar un usuario.
     * id: Nos permite saber qué producto es el que tenemos que ir a buscar a la base de datos para
     * obtener tanto el precio y multiplicarlo por la cantidad.
     *
    */
    @PostMapping("/cart")
    public String addCart(@RequestParam Long id,
                          @RequestParam Integer cantidad,
                          Model model) throws Exception {
        try {
            log.info("HomeController-addCart::Agregar productos al carrito de compras");
            OrdenDTO ordenDTO = cartService.addToCart(id, cantidad);

            // Pasar los datos necesarios a la vista (Thymeleaf)
            model.addAttribute("cart", cartService.getCart());
            model.addAttribute("orden", ordenDTO);
            return ViewNames.CART_VIEW;
        } catch (Exception e) {
            log.error("HomeController-addCart::Error en el Controller al agregar productos al carrito", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }
    }

    /*
    * - deleteFromCart(id): Este método se encarga de eliminar el producto del carrito y
    * devuelve la orden actualizada.
    * - getCart(): Se llama para obtener la lista actualizada de productos
    * en el carrito.
    * - Luego, se actualizan los atributos del model (cart y orden) para que Thymeleaf
    * pueda renderizar la nueva información en la vista del carrito (usuario/carrito).
    * */
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Long id, Model model) throws Exception {
        try {
            log.info("HomeController-deleteProductoCart::Eliminar producto del carrito de compras");
            OrdenDTO ordenDTO = cartService.deleteFromCart(id);

            // Pasar los datos actualizados al modelo para la vista
            model.addAttribute("cart", cartService.getCart());
            model.addAttribute("orden", ordenDTO);
            return ViewNames.CART_VIEW;
        } catch (Exception e) {
            // Registrar el error
            log.error("HomeController-deleteProductoCart::Error en el Controller al eliminar producto del carrito", e);
            // Pasar un mensaje de error a la vista si ocurre alguna excepción
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }

        //return "user/carrito";
    }

    /*
     * Petición ejecutada al momento de hacer clic en el botón de "Ver carrito"
     * en la parte del menu de las vistas.
     */
    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session) throws Exception {
        try {
            log.info("HomeController-getCart::Ver el carrito de compras");
            List<DetalleOrdenDTO> detalleCarrito = cartService.getCart(); //Obtiene los detalles
            OrdenDTO ordenDTO = cartService.getOrden(); //Obtiene la Orden

            // Añadir al modelo para la vista
            model.addAttribute("cart", detalleCarrito);
            model.addAttribute("orden", ordenDTO);
            //model.addAttribute("session", session.getAttribute("idUsuario"));
            // Obtener el ID del usuario de la sesión
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario != null) {
                // Buscar el usuario en la base de datos
                UserDTO userDTO = usuarioService.findById(idUsuario);
                model.addAttribute("usuario", userDTO);
            } else {
                model.addAttribute("usuario", null);
            }

            return ViewNames.CART_VIEW;
        } catch (Exception e) {
            // Registrar el error
            log.error("HomeController-getCart::Error en el Controller al obtener el carrito de compras", e);
            // Pasar un mensaje de error a la vista si ocurre alguna excepción
            model.addAttribute("error", true);
            //model.addAttribute("message", "Ocurrió un error al obtener el carrito de compras");
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }
    }


    /*
     * Petición ejecutada al momento de hacer clic en el botón de "Ver orden"
     * desde la vista "carrito".
     */
    @GetMapping("/order")
    public String order(Model model, HttpSession session) throws Exception {
        try {
            log.info("HomeController-order::Ver la orden de compras");
            List<DetalleOrdenDTO> detalleCarrito = cartService.getCart();
            OrdenDTO ordenDTO = cartService.getOrden();
            //UserDTO userDTO = usuarioService.findById(Long.parseLong((String) session.getAttribute("idUsuario")));

            // Obtener el ID del usuario desde la sesión como Long
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario == null) {
                throw new Exception("El usuario no está autenticado.");
            }
            // Buscar el usuario en la base de datos
            UserDTO userDTO = usuarioService.findById(idUsuario);

            // Añadir al modelo para la vista
            model.addAttribute("cart", detalleCarrito);
            model.addAttribute("orden", ordenDTO);
            model.addAttribute("usuario", userDTO);
            return ViewNames.RESUME_ORDER_VIEW;
        } catch (Exception e) {
            // Registrar el error
            log.error("HomeController-order::Error al obtener la orden de compras", e);
            // Pasar un mensaje de error a la vista si ocurre alguna excepción
            model.addAttribute("error", true);
            //model.addAttribute("message", "Ocurrió un error al obtener la orden de compras");
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }
    }

    //Guardar la orden
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, Model model) throws Exception {
        try {
            log.info("HomeController-saveOrder::Guardar la orden en la base de datos");

            // Obtener el ID de usuario de la sesión
            Long userId = Long.parseLong(session.getAttribute("idUsuario").toString());
            //UserDTO userDTO = usuarioService.findById(1L);
            UserDTO userDTO = usuarioService.findById(userId);

            // Llamar al servicio para guardar la orden
            cartService.saveOrder(userDTO.getId());
            //return "redirect:/";

            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        } catch (Exception e) {
            // Registrar el error
            log.error("HomeController-saveOrder::Error al guardar la orden de compras", e);
            // Pasar un mensaje de error a la vista si ocurre alguna excepción
            model.addAttribute("error", true);
            //model.addAttribute("message", "Ocurrió un error al guardar la orden de compras");
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }
    }


    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model) throws Exception {
        //log.info("Nombre del producto: {}", nombre);
        ////esta escrito en lambda, pasar con chatgpt a funcion normal.
        //List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
        //model.addAttribute("productos", productos);
        //return "usuario/home";

        try {
            log.info("HomeController-search::Búsqueda de Producto desde el Home del usuario");

            //Obtener la lista de productos, pero ir filtrando por las coincidencias encontradas
            //dentro de la lista que coincidan con el nombre enviado como parámetro.
            List<ProductDTO> productDTOS = productoService
                    .findAll()
                    .stream()
                    .filter(p -> p.getNombre().contains(nombre))
                    .collect(Collectors.toList());
            model.addAttribute("productos", productDTOS);

            return ViewNames.HOME_VIEW;
        } catch (Exception e) {
            // Registrar el error
            log.error("HomeController-search::Error al buscar Producto", e);
            // Pasar un mensaje de error a la vista si ocurre alguna excepción
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());

            //Volver a la vista home y mostrar mensaje de error al usuario.
            model.addAttribute("productos", productoService.findAll());
            return ViewNames.HOME_VIEW;
        }
    }

}
