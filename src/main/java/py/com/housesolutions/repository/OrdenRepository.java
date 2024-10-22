package py.com.housesolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.housesolutions.domain.Orden;
import py.com.housesolutions.domain.Usuario;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    //Buscar todas las órdenes por usuario.
    List<Orden> findByUsuario(Usuario usuario);
}
