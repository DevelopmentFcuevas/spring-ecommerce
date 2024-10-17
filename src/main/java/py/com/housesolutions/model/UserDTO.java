package py.com.housesolutions.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    @NotEmpty(message = "El campo Nombre no puede estar vacío.")
    @NotBlank(message = "El campo Nombre no puede estar en blanco.")
    @Size(min = 2, message = "El campo Nombre debe tener entre 2 y 255 caracteres.")
    private String nombre;
    @NotEmpty(message = "El campo Nombre de Usuario no puede estar vacío.")
    @NotBlank(message = "El campo Nombre de Usuario no puede estar en blanco.")
    @Size(min = 2, message = "El campo Nombre de Usuario debe tener entre 2 y 255 caracteres.")
    private String username;
    @NotEmpty(message = "El campo Email no puede estar vacío.")
    @NotBlank(message = "El campo Email no puede estar en blanco.")
    @Size(min = 2, message = "El campo Email debe tener entre 2 y 255 caracteres.")
    private String email;
    private String direccion;
    private String telefono;
    private String tipo;
    private String password;
}
