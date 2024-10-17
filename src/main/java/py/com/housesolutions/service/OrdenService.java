package py.com.housesolutions.service;

import py.com.housesolutions.domain.Orden;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.UserDTO;

import java.util.List;

public interface OrdenService {
    public OrdenDTO toDto(Orden orden);
    public Orden toEntity(OrdenDTO dto);
    List<OrdenDTO> findAll() throws Exception;
    OrdenDTO save(OrdenDTO ordenDTO);
    String generarNumeroOrden() throws Exception;
    List<OrdenDTO> findByUsuario(UserDTO userDTO) throws Exception;
    OrdenDTO findById(Long id);
}
