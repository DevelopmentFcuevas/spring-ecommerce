package py.com.housesolutions.service;

import py.com.housesolutions.domain.Usuario;
import py.com.housesolutions.model.UserDTO;

import java.util.Optional;

public interface UsuarioService {
    UserDTO toDto(Usuario usuario);
    Usuario toEntity(UserDTO dto);
    Optional<Usuario> get(Long id); //DEBO CAMBIAR POR DTO
    UserDTO findById(Long id);
    UserDTO save(UserDTO userDTO) throws Exception;
    Optional<UserDTO> findByEmail(UserDTO userDTO) throws Exception;

}
