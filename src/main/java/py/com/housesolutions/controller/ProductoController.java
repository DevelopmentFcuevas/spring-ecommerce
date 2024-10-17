package py.com.housesolutions.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import py.com.housesolutions.model.ProductDTO;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.service.ProductoService;
import py.com.housesolutions.service.UploadFileService;
import py.com.housesolutions.service.UsuarioService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
@Slf4j
public class ProductoController {
    /*
    * Controladores: Se usan DTO's en los Controladores para interactuar con los servicios,
    * validando los datos recibidos y enviando los datos apropiados a las vistas.
    * */

    @Autowired
    private ProductoService service;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping("")
    public String show(Model model) {
        try {
            log.info("ProductoController-show::obteniendo el listado de Productos");
            model.addAttribute("productos", service.findAll());
        } catch (Exception e) {
            log.error("ProductoController-show::Error al obtener la lista de Productos", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
        }
        return "admin/products/show";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("ProductoController-create::formulario para crear nuevo Producto");
        model.addAttribute("producto", new ProductDTO());
        //return "products/create";
        return "admin/products/create";
    }
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("producto") ProductDTO productDTO,
                       BindingResult result,
                       @RequestParam("file") MultipartFile file,
                       HttpSession session,
                       Model model) throws IOException {
        log.info("ProductoController-save::Guardar un nuevo Producto");

        //La validación de inputs es fundamental para garantizar la calidad de los datos que
        // se envían a un servidor o se procesan localmente. Permite prevenir errores, mejorar
        // la experiencia del usuario y asegurar la integridad de los datos.
        if (result.hasErrors()) {
            log.info("El formulario tiene errores" + result.getFieldErrors());
            //return "products/create";
            return "admin/products/create";
        }

        try {
            Long userId = Long.parseLong(session.getAttribute("idUsuario").toString());
            UserDTO userDTO = usuarioService.findById(userId);
            productDTO.setUserDTO(userDTO); //LLEVAR ESTA LOGICA EN EL SERVICE

            service.save(productDTO, file);
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Registro creado exitosamente!");
            model.addAttribute("productos", service.findAll());
            //return "products/show"; // Vuelve al listado de productos con el nuevo registro creado.
            return "admin/products/show"; // Vuelve al listado de productos con el nuevo registro creado.
        } catch (Exception e) {
            log.error("ProductoController-save::Error al guardar Producto", e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            //return "products/create"; // Vuelve al formulario de creación con el mensaje de error
            return "admin/products/create";
        }
    }

    @GetMapping("/{id}")
    public String obtenerProducto(@PathVariable Long id, Model model) {
        try {
            Optional<ProductDTO> productDTO = service.get(id);
            if (productDTO.isPresent()) {
                model.addAttribute("producto", productDTO);
                return "productoDetalle";
            } else {
                model.addAttribute("error", "Producto no encontrado");
                return "errorPage"; // Página de error si no se encuentra
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Página de error general
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) throws Exception {
        log.info("ProductoController-edit::formulario para editar un Producto existente con Id: {}", id);
        try {
            Optional<ProductDTO> productDTO = service.get(id);
            model.addAttribute("producto", productDTO.get());
            //return "products/edit";
            return "admin/products/edit";
        } catch (Exception e) {
            log.error("ProductoController-edit::formulario para editar Producto, Error al obtener el Producto con ID: "+id, e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("productos", service.findAll());
            //return "products/show"; // Vuelve al listado de productos con el mensaje de error.
            return "admin/products/show"; // Vuelve al listado de productos con el mensaje de error.
        }
    }
    @PostMapping("/update")
    public String actualizarProducto(@Valid @ModelAttribute("producto") ProductDTO productDTO,
                                     BindingResult result,
                                     Model model,
                                     @RequestParam("file") MultipartFile file) {
        log.info("ProductoController-actualizarProducto::Actualizar Producto existente con id: {}", productDTO.getId());
        if (result.hasErrors()) {
            return "admin/products/edit"; // Retorna al formulario si hay errores de validación.
        }

        try {
            //service.update(productDTO.getId(), productDTO);
            service.update(productDTO.getId(), productDTO, file);
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Registro actualizado exitosamente!");
            model.addAttribute("productos", service.findAll());
            return "admin/products/show"; // Vuelve al listado de productos con el registro actualizado.
        } catch (Exception e) {
            log.error("ProductoController-actualizarProducto::Error al obtener Producto con ID: "+productDTO.getId(), e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "admin/products/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String eliminarProducto(@PathVariable Long id, Model model) throws Exception {
        log.info("ProductoController-eliminarProducto::Eliminar Producto existente con id: {}", id);
        try {
            service.delete(id);
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Registro eliminado exitosamente!");
            model.addAttribute("productos", service.findAll());
            return "admin/products/show"; // Vuelve al listado de productos sin el registro eliminado.
        } catch (Exception e) {
            log.error("ProductoController-eliminarProducto::Error al eliminar Producto con ID: "+id, e);
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("productos", service.findAll());
            return "admin/products/show";
        }
    }

}
