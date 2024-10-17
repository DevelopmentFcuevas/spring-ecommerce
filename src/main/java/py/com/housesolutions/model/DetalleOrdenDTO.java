package py.com.housesolutions.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetalleOrdenDTO {
    private Long id;
    private String nombre;
    private double cantidad;
    private double precio;
    private double total;
    private OrdenDTO ordenDTO;
    private ProductDTO productDTO;
}
