package py.com.housesolutions.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orden")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del registro.")
    private Long id;

    @Column(name = "numero", nullable = false)
    @Comment("Numero de orden")
    private String numero;

    @Column(name = "fecha_recibida")
    @Comment("Fecha que se recibio la orden")
    private Date fechaRecibida;

    @Column(name = "fecha_creacion")
    @Comment("Registro de la fecha y hora en que se creó el registro.")
    private Date fechaCreacion;

    @Column(name = "total")
    @Comment("Total de la orden")
    private double total;

    //Vamos a crear un atributo que nos permita identificar que usuarios están registrados a una orden.
    //Aquí lo que va a hacer internamente el framework es a crear un campo en la tabla "Producto"
    //para mandar el IdUsuario o un Usuario como Objeto y que se mapee directamente a la clase "Usuario".
    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "orden")
    @ToString.Exclude
    private List<DetalleOrden> detalleOrden;

    /*
    public Orden() {

    }

    public Orden(Long id, String numero, Date fechaCreacion, Date fechaRecibida, double total) {
        super();
        this.id = id;
        this.numero = numero;
        this.fechaCreacion = fechaCreacion;
        this.fechaRecibida = fechaRecibida;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaRecibida() {
        return fechaRecibida;
    }

    public void setFechaRecibida(Date fechaRecibida) {
        this.fechaRecibida = fechaRecibida;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public List<DetalleOrden> getDetalleOrden() {
        return detalleOrden;
    }

    public void setDetalleOrden(List<DetalleOrden> detalleOrden) {
        this.detalleOrden = detalleOrden;
    }

    @Override
    public String toString() {
        return "Orden [id=" + id + ", numero=" + numero + ", fechaCreacion=" + fechaCreacion + ", fechaRecibida="
                + fechaRecibida + ", total=" + total + "]";
    }
    */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Orden orden = (Orden) o;
        return getId() != null && Objects.equals(getId(), orden.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
