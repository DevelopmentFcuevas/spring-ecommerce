package py.com.housesolutions.service;

import py.com.housesolutions.domain.DetalleOrden;
import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.UserDTO;

import java.util.List;

public interface DetalleOrdenService {
    DetalleOrdenDTO toDto(DetalleOrden detalleOrden) throws Exception;
    DetalleOrden toEntity(DetalleOrdenDTO dto) throws Exception;
    DetalleOrdenDTO save(DetalleOrdenDTO detalleOrdenDTO) throws Exception;
    //List<DetalleOrdenDTO> findByUsuario(UserDTO userDTO) throws Exception;
    List<DetalleOrdenDTO> findByOrdenId(Long idOrden) throws Exception;
}
