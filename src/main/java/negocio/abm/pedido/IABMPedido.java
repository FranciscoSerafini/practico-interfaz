package negocio.abm.pedido;

import java.util.Date;
import java.util.List;
import modelo.cliente.Cliente;
import modelo.pedido.PedidoDetalle;
import modelo.pedido.Pedido;
import modelo.producto.Producto;
import modelo.vendedor.Vendedor;
import negocio.abm.pedido.exception.PedidoException;

/**
 *
 * @author 54346
 */
public interface IABMPedido {

    public List<Pedido> listarPedidos();

    public List<Pedido> filtrarPedidos(String nombreCliente, String apellidoCliente, String nombreVendedor, String apellidoVendedor, String nombrePedido, Date fechaInicio, Date fechaFin);

    public List<PedidoDetalle> obtenerDetallesPedido(int idPedido) throws PedidoException;

    public String obtenerProductoMasVendido(int idPedido) throws PedidoException;

    public double obtenerTotalPedido(int idPedido) throws PedidoException;

}
