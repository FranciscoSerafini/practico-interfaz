package negocio.abm.pedido;

import java.util.List;
import java.util.Date;
import modelo.pedido.PedidoDetalle;
import modelo.pedido.Pedido;
import negocio.abm.pedido.exception.PedidoException;
import repositorio.dao.pedido.IDaoPedido;
import repositorio.dao.pedido.PedidoDaoImpl;

/**
 *
 * @author 54346
 * 
 */

public class ABMPedido implements IABMPedido {

    private IDaoPedido iDaoPedido = new PedidoDaoImpl();

    public List<Pedido> listarPedidos() {
        return iDaoPedido.listarPedidos();
    }

    public List<Pedido> filtrarPedidos(String nombreCliente, String apellidoCliente, String nombreVendedor, String apellidoVendedor, String nombrePedido, Date fechaInicio, Date fechaFin) {
        return iDaoPedido.filtrarPedidos(nombreCliente, apellidoCliente, nombreVendedor, apellidoVendedor, nombrePedido, fechaInicio, fechaFin);
    }

    public List<PedidoDetalle> obtenerDetallesPedido(int idPedido) throws PedidoException {
        if (idPedido <= 0) {
            throw new PedidoException("El ID del pedido no es válido");
        }
        return iDaoPedido.obtenerDetallesPedido(idPedido);
    }

    public String obtenerProductoMasVendido(int idPedido) throws PedidoException {
        if (idPedido <= 0) {
            throw new PedidoException("El ID del pedido no es válido");
        }
        return iDaoPedido.obtenerProductoMasVendido(idPedido);
    }

    public double obtenerTotalPedido(int idPedido) throws PedidoException {
        if (idPedido <= 0) {
            throw new PedidoException("El ID del pedido no es válido");
        }
        return iDaoPedido.obtenerTotalPedido(idPedido);
    }
}