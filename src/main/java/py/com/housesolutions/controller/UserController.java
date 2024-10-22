package py.com.housesolutions.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.service.DetalleOrdenService;
import py.com.housesolutions.service.OrdenService;
import py.com.housesolutions.service.ProductoService;
import py.com.housesolutions.service.UsuarioService;
import py.com.housesolutions.util.ViewNames;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private DetalleOrdenService detalleOrdenService;

    @GetMapping("/registro")
    public String create() {
        return "user/registro";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("usuario") UserDTO userDTO,
                        BindingResult result,
                        Model model) {
        log.info("UserController-save::Guardar un nuevo Registro de Usuario");

        //La validación de inputs es fundamental para garantizar la calidad de los datos que
        // se envían a un servidor o se procesan localmente. Permite prevenir errores, mejorar
        // la experiencia del usuario y asegurar la integridad de los datos.
        if (result.hasErrors()) {
            log.info("El formulario tiene errores " + result.getFieldErrors());
            // Crear una lista para almacenar los mensajes de error amigables
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            model.addAttribute("errorFormulario", true);
            model.addAttribute("message", errores); // Pasar la lista de errores a la vista
            return "user/registro";
        }

        try {
            userDTO.setTipo("USER"); //PARA QUE NO ME DE ERROR, SETEO EL TIPO DE USUARIO EN DURO
            usuarioService.save(userDTO);
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Registro creado exitosamente!");
            return "user/registro";
        } catch (Exception e) {
            log.error("UserController-save::Error al guardar nuevo Registro de Usuario", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "user/registro"; // Vuelve al formulario de creación con el mensaje de error
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        log.info("UserController-login::formulario para hacer login");
        model.addAttribute("userDTO", new UserDTO());
        return "user/login";
    }

    @PostMapping("/acceder")
    public String acceder(UserDTO userDTO, HttpSession session, Model model) {
        log.info("UserController-acceder::Inicia método posterior a hacer login en el sistema");

        try {
            // Buscar el usuario en la base de datos
            Optional<UserDTO> userDTO1 = usuarioService.findByEmail(userDTO);
            if (userDTO1.isPresent()) {
                // Guardar el ID del usuario en la sesión
                session.setAttribute("idUsuario", userDTO1.get().getId());
                model.addAttribute("usuario", userDTO1.get());

                // Añadir la lista de productos al modelo
                model.addAttribute("productos", productoService.findAll());

                // Redirigir según el tipo de usuario
                if (userDTO1.get().getTipo().equals("ADMIN")) {
                    return ViewNames.ADMIN_VIEW; // Vista para administrador
                } else {
                    return ViewNames.HOME_VIEW; // Vista para usuario normal
                }
            } else {
                // Si el usuario no existe, mostrar un mensaje de error
                model.addAttribute("error", true);
                model.addAttribute("message", "Credenciales incorrectas. Por favor, intente de nuevo.");
                //return "user/login";
                return ViewNames.USER_LOGIN_VIEW;
            }

        } catch (Exception e) {
            log.error("UserController-save::Error al acceder a la aplicación desde el login", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return ViewNames.USER_LOGIN_VIEW;
        }
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        try {
            log.info("UserController-obtenerCompras::Inicia método para obtener compras de usuario");

            Long idUsuario = (Long) session.getAttribute("idUsuario");
            if (idUsuario != null) {
                // Buscar el usuario en la base de datos
                UserDTO userDTO = usuarioService.findById(idUsuario);

                List<OrdenDTO> ordenDTOS = ordenService.findByUsuario(userDTO);
                model.addAttribute("ordenes", ordenDTOS); //Le pasa al model una lista de Órdenes del usuario conectado.
            } else {
                model.addAttribute("usuario", null);
            }
            return "user/compras";
        } catch (Exception e) {
            log.error("UserController-obtenerCompras::Error al obtener compras", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "user/compras";
        }
    }


    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Long id, HttpSession session, Model model) {
        try {
            log.info("UserController-detalleCompra::Inicia método para obtener detalle de compras de usuario");
            List<DetalleOrdenDTO> detalleOrdenDTOS = detalleOrdenService.findByOrdenId(id);
            model.addAttribute("detalles", detalleOrdenDTOS);
            return "user/detallecompra";
        } catch (Exception e) {
            log.error("UserController-detalleCompra::Error al obtener detalle de compras", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "user/detallecompra";
        }
    }

    @GetMapping("/cerrar")
    public String cerrarSession(Model model, HttpSession session) {
        try {
            log.info("UserController-cerrarSession::Obteniendo el listado de Productos para la vista de los usuarios");

            session.removeAttribute("idUsuario");
            model.addAttribute("productos", productoService.findAll());
        } catch (Exception e) {
            log.error("HomeController-cerrarSession::Error en el Controller al obtener el listado de Productos ", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
        }

        return ViewNames.HOME_VIEW;
    }
    
}
