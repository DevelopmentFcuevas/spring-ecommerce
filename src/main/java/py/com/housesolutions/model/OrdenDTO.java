package py.com.housesolutions.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
public class OrdenDTO {
    private Long id;
    private String numero;
    private Date fechaRecibida;
    private Date fechaCreacion;
    private double total;
    private UserDTO userDTO;
}
