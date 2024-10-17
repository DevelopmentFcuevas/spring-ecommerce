package py.com.housesolutions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.com.housesolutions.domain.Orden;
import py.com.housesolutions.domain.Usuario;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.repository.OrdenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdenServiceImpl implements OrdenService {
    @Autowired
    private OrdenRepository repository;
    @Autowired
    private UsuarioService usuarioService;

    /*
     * Mapeo entre DTO y Entidades: Utiliza un servicio o una librería como "MapStruct" para mapear
     * entre DTO y Entidades.
     */
    @Override
    public OrdenDTO toDto(Orden orden) {
        // Mapear los campos...
        OrdenDTO dto = new OrdenDTO();
        dto.setId(orden.getId());
        dto.setNumero(orden.getNumero());
        dto.setFechaRecibida(orden.getFechaRecibida());
        dto.setFechaCreacion(orden.getFechaCreacion());
        dto.setTotal(orden.getTotal());

        UserDTO usuario = usuarioService.findById(orden.getUsuario().getId());
        if (usuario != null) {
            dto.setUserDTO(usuario);
        }

        return dto;
    }

    @Override
    public Orden toEntity(OrdenDTO dto) {
        // Mapear los campos...
        Orden orden = new Orden();
        orden.setId(dto.getId());
        orden.setNumero(dto.getNumero());
        orden.setFechaRecibida(dto.getFechaRecibida());
        orden.setFechaCreacion(dto.getFechaCreacion());
        orden.setTotal(dto.getTotal());

        Usuario usuario = usuarioService.get(dto.getUserDTO().getId()).get();
        orden.setUsuario(usuario);

        return orden;
    }

    //Con este método obtenemos todas las órdenes, lo que nos va a servir para ir
    //mirando cuál es el último número secuencial de la orden.
    @Override
    public List<OrdenDTO> findAll() throws Exception {
        try {
            List<Orden> ordenes = repository.findAll();
            List<OrdenDTO> ordenDTOS = new ArrayList<>();
            for (Orden orden : ordenes) {
                OrdenDTO dto = toDto(orden);
                ordenDTOS.add(dto);
            }
            log.info("OrdenService-findAll::Acción completada sin errores.");
            return ordenDTOS;
        } catch (Exception e) {
            log.error("Error en OrdenService al obtener la lista de Ordenes", e);
            throw new Exception("Error en OrdenService al obtener la lista de Ordenes {}", e);
        }
    }

    @Override
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.info("OrdenService-save::Guardar orden en la base de datos");
        //DEBO AGREGAR TRY-CATCH Y VER SI HACER TRANSACCIONAL
        return toDto(repository.save(toEntity(ordenDTO)));
    }

    @Override
    public String generarNumeroOrden() throws Exception {
        log.info("OrdenService-generarNumeroOrden::Generar numero de orden");
        int numero = 0;
        String numeroConcatenado = "";

        List<OrdenDTO> ordenes = findAll();
        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.stream().forEach(orden -> numeros.add(Integer.parseInt( orden.getNumero() ) ));

        if (ordenes.isEmpty()) {
            numero = 1;
        } else {
            //Con esto lo que hace es obtenernos el mayor número de toda esa lista
            numero = numeros.stream().max(Integer::compare).get();  //La lista donde hemos añadido todos los números.
            numero++;
        }

        //Ejemplo para armar numero: 00000000010..
        if (numero < 10) {
            numeroConcatenado = "000000000" + String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        } else if (numero < 10000) {
            numeroConcatenado = "000000" + String.valueOf(numero);
        } else if (numero < 100000) {
            numeroConcatenado = "00000" + String.valueOf(numero);
        }

        log.info("OrdenService-generarNumeroOrden::Acción completada sin errores.");
        return numeroConcatenado;
    }

    @Override
    public List<OrdenDTO> findByUsuario(UserDTO userDTO) throws Exception {
        try {
            log.info("OrdenService-findByUsuario::Iniciando Servicio para buscar ordenes por usuario");
            // Buscar el usuario por ID usando el servicio
            UserDTO usuarioBuscado = usuarioService.findById(userDTO.getId());
            // Si no se encuentra el usuario, lanzar excepción
            if (usuarioBuscado == null) {
                throw new Exception("Usuario no encontrado");
            }

            // Buscar las órdenes del usuario
            List<Orden> ordens = repository.findByUsuario(usuarioService.toEntity(usuarioBuscado));
            // Convertir la lista de entidades Orden a DTO
            List<OrdenDTO> ordenDTOList = ordens.stream()
                    .map(orden -> {
                        OrdenDTO ordenDTO = new OrdenDTO();
                        ordenDTO.setId(orden.getId());
                        ordenDTO.setNumero(orden.getNumero());
                        ordenDTO.setTotal(orden.getTotal());
                        ordenDTO.setFechaCreacion(orden.getFechaCreacion());
                        ordenDTO.setUserDTO(userDTO);
                        // Agregar más campos si es necesario
                        return ordenDTO;
                    })
                    .collect(Collectors.toList());
            log.info("OrdenService-findByUsuario::Acción completada sin errores.");

            // Retornar la lista de OrdenDTO
            return ordenDTOList;
        } catch (Exception e) {
            log.error("OrdenService-findByUsuario::Error al obtener ordenes por usuario ", e);
            throw new Exception("Error en el Servicio para obtener orden por usuario");
        }
    }

    @Override
    public OrdenDTO findById(Long id) {
        return toDto( repository.findById(id).get() );
    }

}
