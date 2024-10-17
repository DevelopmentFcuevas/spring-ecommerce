package py.com.housesolutions.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import py.com.housesolutions.domain.Producto;
import py.com.housesolutions.model.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    /*
     * interface: Una interfaz en Java (y por extensión, en Spring Boot) define
     * un contrato: un conjunto de métodos que una clase debe implementar.
     * Es como un plano o un blueprint que especifica las operaciones
     * que una clase puede realizar.
     */

    ProductDTO toDto(Producto producto);
    Producto toEntity(ProductDTO dto);
    List<ProductDTO> findAll() throws Exception;
    ProductDTO save(ProductDTO productDTO, MultipartFile file) throws Exception;
    /* ¿Porque se utiliza Optional?
     * - Por que Optional nos da la opción de poder validar
     * si es que el objeto que obtenemos existe o no.
     */
    Optional<ProductDTO> get(Long id) throws Exception;
    @Transactional
    ProductDTO update(Long id, ProductDTO productDTO, MultipartFile file) throws Exception;
    void delete(Long id) throws Exception;
    Optional<ProductDTO> getByNombre(String nombre) throws Exception;
}
