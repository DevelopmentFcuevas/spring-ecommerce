package py.com.housesolutions.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "usuarios")
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del registro.")
    private Long id;

    @Column(name = "nombre")
    @Comment("Nombre del usuario")
    private String nombre;

    @Column(name = "username")
    @Comment("Username del usuario")
    private String username;

    @Column(name = "email")
    @Comment("Email del usuario")
    private String email;

    @Column(name = "direccion")
    @Comment("Dirección del usuario")
    private String direccion;

    @Column(name = "telefono")
    @Comment("Teléfono del usuario")
    private String telefono;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "password")
    @Comment("Password del usuario")
    private String password;

    //Para obtener una lista de productos para usuarios
    /*@OneToMany(mappedBy = "usuario")
    private List<Producto> productos;*/

    //Para obtener una lista de productos para ordenes
    /*@OneToMany(mappedBy = "usuario")
    private List<Orden> ordenes;*/

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getId() != null && Objects.equals(getId(), usuario.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}