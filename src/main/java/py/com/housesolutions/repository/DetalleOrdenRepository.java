package py.com.housesolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.housesolutions.domain.DetalleOrden;
@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

}
