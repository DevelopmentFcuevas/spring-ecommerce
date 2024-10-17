package py.com.housesolutions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.com.housesolutions.domain.DetalleOrden;
import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;
import py.com.housesolutions.model.ProductDTO;
import py.com.housesolutions.repository.DetalleOrdenRepository;

import java.util.Optional;

@Slf4j
@Service
public class DetalleOrdenServiceImpl implements DetalleOrdenService {
    @Autowired
    private DetalleOrdenRepository repository;
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private ProductoService productoService;

    /*
     * Mapeo entre DTO y Entidades: Utiliza un servicio o una librería como "MapStruct" para mapear
     * entre DTO y Entidades.
     */
    @Override
    public DetalleOrdenDTO toDto(DetalleOrden detalleOrden) throws Exception {
        // Mapear los campos...
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setId(detalleOrden.getId());
        dto.setNombre(detalleOrden.getNombre());
        dto.setCantidad(detalleOrden.getCantidad());
        dto.setPrecio(detalleOrden.getPrecio());
        dto.setTotal(detalleOrden.getTotal());

        //OrdenDTO orden = ordenService.findById(detalleOrden.getOrden().getId()).get();
        OrdenDTO orden = ordenService.findById(detalleOrden.getOrden().getId());
        if (orden != null) {
            dto.setOrdenDTO(orden);
        }

        Optional<ProductDTO> producto = productoService.get(detalleOrden.getProducto().getId());
        if (producto.isPresent()) {
            dto.setProductDTO(producto.get());
        }

        return dto;
    }

    @Override
    public DetalleOrden toEntity(DetalleOrdenDTO dto) throws Exception {
        // Mapear los campos...
        DetalleOrden entity = new DetalleOrden();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setCantidad(dto.getCantidad());
        entity.setPrecio(dto.getPrecio());
        entity.setTotal(dto.getTotal());

        //Setear Orden
        OrdenDTO orden = ordenService.findById(dto.getOrdenDTO().getId());
        if (orden != null) {
            entity.setOrden( ordenService.toEntity(orden) );
        }

        //Setear Producto
        Optional<ProductDTO> producto = productoService.get(dto.getProductDTO().getId());
        if (producto.isPresent()) {
            entity.setProducto( productoService.toEntity(producto.get()) );
        }

        return entity;
    }

    @Override
    public DetalleOrdenDTO save(DetalleOrdenDTO detalleOrdenDTO) throws Exception {
        try {
            log.info("DetalleOrdenService-save::Persistir en la Base de datos el detalle de la orden de compra");
            return toDto( repository.save( toEntity(detalleOrdenDTO) ) );
        } catch (Exception e) {
            log.error("DetalleOrdenService-save::Error al guardar el detalle de la orden de compras ", e);
            throw new Exception("Error en el Servicio al guardar el detalle de la orden de compras");
        }
    }

}
