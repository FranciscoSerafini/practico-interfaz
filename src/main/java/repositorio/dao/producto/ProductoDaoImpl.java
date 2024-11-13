package repositorio.dao.producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.Producto;
import negocio.abm.producto.exception.ProductoException;

/**
 *
 * @author 54346
 */

public class ProductoDaoImpl implements IDaoProducto {

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_PRODUCTO = "INSERT INTO productos (nombre, precio, descripcion, cantidad) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_PRODUCTO_BY_ID = "SELECT * FROM productos WHERE id = ?";
    private static final String SQL_DELETE_PRODUCTO_BY_ID = "DELETE FROM productos WHERE id = ?";
    private static final String SQL_UPDATE_PRODUCTO = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, cantidad = ? WHERE id = ?";

    public ProductoDaoImpl(ConexionDb conexionDb) {
        this.conexionDb = conexionDb;
    }

    @Override
    public void altaProducto(Producto producto) throws ProductoException {
        String nombreProducto = producto.getNombre();
        double precio = producto.getPrecio();
        String descripcion = producto.getDescripcion();
        int cantidad = producto.getCantidad();

        try (PreparedStatement stmtProducto = conexionDb.dbConection().prepareStatement(SQL_INSERT_PRODUCTO)) {
            stmtProducto.setString(1, nombreProducto);
            stmtProducto.setDouble(2, precio);
            stmtProducto.setString(3, descripcion);
            stmtProducto.setInt(4, cantidad);

            int affectedRows = stmtProducto.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Producto " + nombreProducto + " insertado con éxito.");
            } else {
                System.out.println("Error al insertar el producto.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    @Override
    public void bajaProducto(int idProducto) throws ProductoException {
        try {
            if (productoExistsById(idProducto)) {
                try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_PRODUCTO_BY_ID)) {
                    stmtDelete.setInt(1, idProducto);
                    int affectedRows = stmtDelete.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Producto con ID " + idProducto + " eliminado exitosamente.");
                    } else {
                        System.out.println("No se pudo eliminar el producto con ID " + idProducto);
                    }
                }
            } else {
                System.out.println("El producto con ID " + idProducto + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @Override
    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException {
        try {
            if (productoExistsById(idProducto)) {
                try (PreparedStatement stmtUpdate = conexionDb.dbConection().prepareStatement(SQL_UPDATE_PRODUCTO)) {
                    stmtUpdate.setString(1, productoModificado.getNombre());
                    stmtUpdate.setDouble(2, productoModificado.getPrecio());
                    stmtUpdate.setString(3, productoModificado.getDescripcion());
                    stmtUpdate.setInt(4, productoModificado.getCantidad());
                    stmtUpdate.setInt(5, idProducto);

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Producto con ID " + idProducto + " actualizado exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar el producto con ID " + idProducto);
                    }
                }
            } else {
                System.out.println("El producto con ID " + idProducto + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el producto: " + e.getMessage());
        }
    }

    @Override
    public List<Producto> listarProductos(int idProducto, String nombreProducto) throws ProductoException {
        List<Producto> productos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM productos WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            
            if (idProducto != 0) {
                sqlQuery.append(" AND id = ?");
                param.put(index++, idProducto);
            }

            if (nombreProducto != null && !nombreProducto.isEmpty()) {
                sqlQuery.append(" AND nombre LIKE ?");
                param.put(index++, "%" + nombreProducto + "%");
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                String descripcion = rs.getString("descripcion");
                int cantidad = rs.getInt("cantidad");

                Producto producto = new Producto(nombre, precio, descripcion, cantidad);
                producto.setId(id);

                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de productos: " + e.getMessage());
        }

        return productos;
    }

    @Override
    public Producto obtenerProductoPorId(int idProducto) throws ProductoException {
        Producto producto = null;
        String query = "SELECT * FROM productos WHERE id = ?";

        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(query)) {
            stmt.setInt(1, idProducto);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                String descripcion = rs.getString("descripcion");
                int cantidad = rs.getInt("cantidad");

                producto = new Producto(nombre, precio, descripcion, cantidad);
                producto.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new ProductoException("Error al obtener el producto por ID: " + e.getMessage());
        }

        return producto;
    }
    
    private boolean productoExistsById(int idProducto) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_PRODUCTO_BY_ID)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}