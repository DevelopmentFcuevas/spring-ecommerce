package py.com.housesolutions.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    private Long id;

    @NotEmpty(message = "El campo Nombre no puede estar vacío.")
    @NotBlank(message = "El campo Nombre no puede estar en blanco.")
    @Size(min = 2, message = "El campo Nombre debe tener entre 2 y 255 caracteres.")
    private String nombre;

    @NotEmpty(message = "El campo Descripción no puede estar vacío.")
    @NotBlank(message = "El campo Descripción no puede estar en blanco.")
    @Size(min = 2, message = "El campo Descripción debe tener entre 2 y 255 caracteres.")
    private String descripcion;

    private String imagen;

    @DecimalMin(value = "0.1", inclusive = true, message = "El campo Precio debe ser mayor a cero." )
    private double precio;

    private double cantidad;

    private UserDTO userDTO;

}
