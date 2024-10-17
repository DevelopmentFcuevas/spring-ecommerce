package py.com.housesolutions.service;

import py.com.housesolutions.domain.DetalleOrden;
import py.com.housesolutions.model.DetalleOrdenDTO;

public interface DetalleOrdenService {
    DetalleOrdenDTO toDto(DetalleOrden detalleOrden) throws Exception;
    DetalleOrden toEntity(DetalleOrdenDTO dto) throws Exception;
    DetalleOrdenDTO save(DetalleOrdenDTO detalleOrdenDTO) throws Exception;
}
