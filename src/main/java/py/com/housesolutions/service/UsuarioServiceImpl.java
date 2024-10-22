package py.com.housesolutions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.com.housesolutions.domain.Usuario;
import py.com.housesolutions.model.UserDTO;
import py.com.housesolutions.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    UserRepository repository;

    /*
     * Mapeo entre DTOs y Entidades: Utiliza un servicio o una librería como MapStruct para mapear
     * entre DTOs y entidades.
     * */
    public UserDTO toDto(Usuario usuario) {
        // Mapear los campos...
        UserDTO dto = new UserDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        dto.setTipo(usuario.getTipo());
        dto.setPassword(usuario.getPassword());

        return dto;
    }

    public Usuario toEntity(UserDTO dto) {
        // Mapear los campos...
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        usuario.setTipo(dto.getTipo());
        usuario.setPassword(dto.getPassword());

        return usuario;
    }

    //DEBO CAMBIAR POR DTO
    @Override
    public Optional<Usuario> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserDTO findById(Long id) {
        return toDto(repository.getOne(id));
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws Exception {
        try {
            log.info("UsuarioService-save::Persistir en la Base de datos el usuario");
            Usuario usuario = toEntity(userDTO);
            Usuario savedUsuario = repository.save(usuario);
            log.info("UsuarioService-save::Acción completada sin errores");
            return toDto(savedUsuario);
        } catch (Exception e) {
            log.error("UsuarioService-save::Error en el Service al guardar el nuevo registro de usuario ", e);
            throw new Exception("Error al guardar el usuario. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

    @Override
    public Optional<UserDTO> findByEmail(UserDTO userDTO) throws Exception {
        try {
            Optional<Usuario> usuario = repository.findByEmail(userDTO.getEmail());
            if (usuario.isEmpty()) {
                log.error("UsuarioService-findByEmail::Error en el Service, Usuario con el Email: {}, No encontrado ", userDTO.getEmail());
                throw new Exception("Error al realizar la búsqueda de Usuario. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
            }

            log.info("UsuarioService-get::Acción completada sin errores");
            return usuario.map(this::toDto);
            //throw new RuntimeException("Error simulado para probar el manejo de excepciones.");
        } catch (Exception e) {
            log.error("UsuarioService-findByEmail::Error en el Service al buscar Producto ", e);
            throw new Exception("Error al realizar la búsqueda del Producto. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }


    @Override
    public List<UserDTO> findAll() throws Exception {
        try {
            log.info("UsuarioService-findAll::Obteniendo lista de usuarios");
            List<Usuario> usuarioList = repository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for (Usuario usuario : usuarioList) {
                UserDTO dto = toDto(usuario);
                userDTOS.add(dto);
            }
            log.info("UsuarioService-findAll::Acción completada sin errores.");
            return userDTOS;
        } catch (Exception e) {
            log.error("Error en UsuarioService al obtener la lista de Usuarios", e);
            throw new Exception("Error al obtener el listado de usuarios. Por favor, inténtelo de nuevo más tarde o consulte con el Administrador del Sistema.");
        }
    }

}
