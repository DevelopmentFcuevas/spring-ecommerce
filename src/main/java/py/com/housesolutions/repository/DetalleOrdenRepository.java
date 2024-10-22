package py.com.housesolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.housesolutions.domain.DetalleOrden;
import py.com.housesolutions.domain.Usuario;

import java.util.List;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    List<DetalleOrden> findByOrdenId(Long idOrden);
    //List<DetalleOrden> findByUsuario(Usuario usuario);

}
