package py.com.housesolutions.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import py.com.housesolutions.service.ProductoService;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdministradorController {
    @Autowired
    private ProductoService productoService;

    /*@GetMapping("")
    public String home() {
        //return "administrador/home";
        return "administrator/home";
    }*/
    /*Esta es la home del Administrador*/
    @GetMapping("")
    public String home(Model model) {
        log.info("AdministradorController-home::obteniendo el listado de Productos");
        try {
            model.addAttribute("productos", productoService.findAll());
            //return "administrator/home";
            return "admin/home";
        } catch (Exception e) {
            log.error("Error en el AdministradorController al obtener la lista de Productos", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            //return "administrator/home"; // Vuelve al formulario aun si hay un error.
            return "admin/home"; // Vuelve al formulario aun si hay un error.
        }

    }

}
