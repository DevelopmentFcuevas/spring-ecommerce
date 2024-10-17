package py.com.housesolutions.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "productos")
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del registro.")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    @Comment("Nombre del producto")
    private String nombre;

    @Column(name = "descripcion")
    @Comment("Descripción del producto")
    private String descripcion;

    @Column(name = "imagen")
    @Comment("Imagen del producto")
    private String imagen;

    @Column(name = "precio")
    @Comment("Precio del producto")
    private double precio;

    @Column(name = "cantidad")
    @Comment("Cantidad del producto")
    private double cantidad;

    //Aquí lo que va a hacer internamente el framework(spring) es a crear un campo en la tabla "Producto"
    //para mandar el IdUsuario o un Usuario como Objeto y que se mapee directamente a la clase "Usuario".
    @ManyToOne
    private Usuario usuario;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Producto producto = (Producto) o;
        return getId() != null && Objects.equals(getId(), producto.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
