package py.com.housesolutions.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import py.com.housesolutions.service.ProductoService;
import py.com.housesolutions.service.UsuarioService;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdministradorController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;

    /*
     *
     * Esta es la home del administrador.
     */
    @GetMapping("")
    public String home(Model model) {
        log.info("AdministradorController-home::obteniendo el listado de Productos");
        try {
            model.addAttribute("productos", productoService.findAll());
            return "admin/home";
        } catch (Exception e) {
            log.error("Error en el AdministradorController al obtener la lista de Productos", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "admin/home"; // Vuelve al formulario aun si hay un error.
        }
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) throws Exception {
        try {
            log.info("AdministradorController-usuarios::obteniendo el listado de Usuarios");
            model.addAttribute("usuarios", usuarioService.findAll());
            return "admin/users/usuarios";
        } catch (Exception e) {
            log.error("AdministradorController-usuarios::Error en el Controller al obtener el listado de Usuarios ", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("productos", productoService.findAll());
            return "admin/home";
        }
    }

}
