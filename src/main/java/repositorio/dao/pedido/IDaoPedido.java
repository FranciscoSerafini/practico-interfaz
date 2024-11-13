package repositorio.dao.pedido;

import java.util.Date;
import java.util.List;
import modelo.pedido.Pedido;
import modelo.pedido.PedidoDetalle;

/**
 *
 * @author 54346
 */

public interface IDaoPedido {
    public List<Pedido> listarPedidos();
    
    public List<Pedido> filtrarPedidos(String nombreCliente, String apellidoCliente, String nombreVendedor, String apellidoVendedor, String nombrePedido, Date fechaInicio, Date fechaFin);
    
    public List<PedidoDetalle> obtenerDetallesPedido(int idPedido);
    
    public String obtenerProductoMasVendido(int idPedido);
    
    public double obtenerTotalPedido(int idPedido);
}
