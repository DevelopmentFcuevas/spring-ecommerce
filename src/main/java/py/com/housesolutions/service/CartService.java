package py.com.housesolutions.service;

import py.com.housesolutions.model.DetalleOrdenDTO;
import py.com.housesolutions.model.OrdenDTO;

import java.util.List;

public interface CartService {
    OrdenDTO addToCart(Long productId, Integer cantidad) throws Exception;
    List<DetalleOrdenDTO> getCart() throws Exception;
    void clearCart();
    OrdenDTO deleteFromCart(Long productId) throws Exception;

    // Método para obtener el objeto OrdenDTO
    OrdenDTO getOrden() throws Exception;

    // Método para guardar la orden
    void saveOrder(Long userId) throws Exception;
}
