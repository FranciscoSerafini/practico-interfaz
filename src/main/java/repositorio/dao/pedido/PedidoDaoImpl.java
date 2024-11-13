package repositorio.dao.pedido;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import modelo.pedido.Pedido;
import modelo.pedido.PedidoDetalle;
import repositorio.dao.ConexionDb;

/**
 *
 * @author 54346
 */
public class PedidoDaoImpl implements IDaoPedido {

    private ConexionDb conexionDb;

    private static final String SQL_LISTAR_PEDIDOS
            = "SELECT p.id AS id_pedido, p.fecha AS fecha_pedido, "
            + "CONCAT(pc.nombre, ' ', pc.apellido) AS nombre_cliente, "
            + "CONCAT(pv.nombre, ' ', pv.apellido) AS nombre_vendedor "
            + "FROM pedidos p "
            + "INNER JOIN clientes c ON p.id_cliente = c.id "
            + "INNER JOIN personas pc ON c.id_persona = pc.id "
            + "INNER JOIN vendedores v ON p.id_vendedor = v.id "
            + "INNER JOIN personas pv ON v.id_persona = pv.id";

    private static final String SQL_FILTRAR_PEDIDOS
            = "SELECT p.id AS id_pedido, p.fecha AS fecha_pedido, "
            + "CONCAT(pc.nombre, ' ', pc.apellido) AS nombre_cliente, "
            + "CONCAT(pv.nombre, ' ', pv.apellido) AS nombre_vendedor "
            + "FROM pedidos p "
            + "INNER JOIN clientes c ON p.id_cliente = c.id "
            + "INNER JOIN personas pc ON c.id_persona = pc.id "
            + "INNER JOIN vendedores v ON p.id_vendedor = v.id "
            + "INNER JOIN personas pv ON v.id_persona = pv.id "
            + "WHERE 1=1";

    private static final String SQL_DETALLE_PEDIDO = "SELECT nombre_producto, cantidad, precio_unitario "
            + "FROM detalle_pedido WHERE id_pedido = ?";

    private static final String SQL_PRODUCTO_MAS_VENDIDO = "SELECT nombre_producto, MAX(cantidad) AS cantidad "
            + "FROM detalle_pedido WHERE id_pedido = ? GROUP BY nombre_producto ORDER BY cantidad DESC LIMIT 1";

    private static final String SQL_TOTAL_PEDIDO = "SELECT SUM(cantidad * precio_unitario) AS total "
            + "FROM detalle_pedido WHERE id_pedido = ?";

    public PedidoDaoImpl() {
        this.conexionDb = new ConexionDb();
    }

    @Override
    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        try {
            ResultSet rs = conexionDb.executeSqlQuery(SQL_LISTAR_PEDIDOS);

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido"),
                        rs.getString("nombre_vendedor"),
                        rs.getString("nombre_cliente")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todos los pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    @Override
    public List<Pedido> filtrarPedidos(String nombreCliente, String apellidoCliente, String nombreVendedor, String apellidoVendedor, String nombrePedido, Date fechaInicio, Date fechaFin) {
        List<Pedido> pedidos = new ArrayList<>();

        String sqlQuery = SQL_FILTRAR_PEDIDOS;
        HashMap<Integer, Object> params = new HashMap<>();
        int index = 1;

        if (nombreCliente != null && !nombreCliente.isEmpty()) {
            sqlQuery += " AND pc.nombre = ?";
            params.put(index++, nombreCliente);
        }
        if (apellidoCliente != null && !apellidoCliente.isEmpty()) {
            sqlQuery += " AND pc.apellido = ?";
            params.put(index++, apellidoCliente);
        }
        if (nombreVendedor != null && !nombreVendedor.isEmpty()) {
            sqlQuery += " AND pv.nombre = ?";
            params.put(index++, nombreVendedor);
        }
        if (apellidoVendedor != null && !apellidoVendedor.isEmpty()) {
            sqlQuery += " AND pv.apellido = ?";
            params.put(index++, apellidoVendedor);
        }
        if (fechaInicio != null) {
            sqlQuery += " AND p.fecha >= ?";
            params.put(index++, fechaInicio);
        }
        if (fechaFin != null) {
            sqlQuery += " AND p.fecha <= ?";
            params.put(index++, fechaFin);
        }

        try {
            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery, params);

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido"),
                        rs.getString("nombre_vendedor"),
                        rs.getString("nombre_cliente")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    @Override
    public List<PedidoDetalle> obtenerDetallesPedido(int idPedido) {
        List<PedidoDetalle> detalles = new ArrayList<>();

        try {
            HashMap<Integer, Object> params = new HashMap<>();
            params.put(1, idPedido);

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(SQL_DETALLE_PEDIDO, params);

            while (rs.next()) {
                PedidoDetalle detalle = new PedidoDetalle(
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                );
                detalles.add(detalle);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el detalle del pedido: " + e.getMessage());
        }

        return detalles;
    }

    @Override
    public String obtenerProductoMasVendido(int idPedido) {
        String productoMasVendido = null;

        try {
            HashMap<Integer, Object> params = new HashMap<>();
            params.put(1, idPedido);

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(SQL_PRODUCTO_MAS_VENDIDO, params);

            if (rs.next()) {
                productoMasVendido = rs.getString("nombre_producto");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el producto más vendido: " + e.getMessage());
        }

        return productoMasVendido;
    }

    @Override
    public double obtenerTotalPedido(int idPedido) {
        double total = 0;

        try {
            HashMap<Integer, Object> params = new HashMap<>();
            params.put(1, idPedido);

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(SQL_TOTAL_PEDIDO, params);

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total del pedido: " + e.getMessage());
        }

        return total;
    }
}
