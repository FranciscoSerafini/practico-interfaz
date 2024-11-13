package repositorio.dao.vendedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.vendedor.Vendedor;

/**
 *
 * @author 54346
 */

public class VendedorDaoImpl implements IDaoVendedor{

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_PERSONA = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_VENDEDOR = "INSERT INTO vendedores (id_persona, codigo, sucursal, cuit) VALUES (?, ?, ?, ?)";

    @Override
    public void insertarNuevoVendedor(Vendedor vendedor) {
        String codigoVendedor = getProximoCodigoVendedor();

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.dbConection().prepareStatement(SQL_INSERT_PERSONA, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, vendedor.getNombre());
            stmtPersona.setString(2, vendedor.getApellido());
            stmtPersona.setInt(3, vendedor.getDni());
            stmtPersona.setString(4, vendedor.getEmail());
            stmtPersona.setString(5, vendedor.getTelefono());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1);

                    PreparedStatement stmtVendedor = conexionDb.dbConection().prepareStatement(SQL_INSERT_VENDEDOR);
                    stmtVendedor.setInt(1, personId);
                    stmtVendedor.setString(2, codigoVendedor);
                    stmtVendedor.setString(3, vendedor.getSucursal());
                    stmtVendedor.setString(4, vendedor.getCuit());

                    int affectedRowsVendedor = stmtVendedor.executeUpdate();

                    if (affectedRowsVendedor > 0) {
                        System.out.println("Nuevo vendedor insertado con éxito con código: " + codigoVendedor);
                    } else {
                        System.out.println("Error al insertar el vendedor.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el vendedor: " + e.getMessage());
        }
    }

    @Override
    public void eliminarVendedor(String codigo) {
        String sqlVendedorId = "SELECT id_persona FROM vendedores WHERE codigo = ?";
        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteVendedor = "DELETE FROM vendedores WHERE codigo = ?";
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(1, codigo); 

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlVendedorId, param);
            if (rs.next()) {
                int personId = rs.getInt("id_persona");

                param.clear();
                param.put(1, codigo);  
                conexionDb.executeSqlUpdateWithParameters(sqlDeleteVendedor, param);

                param.clear();
                param.put(1, personId);  
                conexionDb.executeSqlUpdateWithParameters(sqlDeletePerson, param);

                System.out.println("El vendedor se eliminó exitosamente.");
            } else {
                System.out.println("Vendedor no encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el vendedor: " + e.getMessage());
        }
    }
    
    @Override
    public void modificarVendedor(String codigo, Vendedor vendedor) {
        String sqlUpdateVendedor = "UPDATE vendedores SET cuit = ?, sucursal = ? WHERE codigo = ?";
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(1, vendedor.getCuit());  
            param.put(2, vendedor.getSucursal()); 
            param.put(3, codigo);

            int rowsUpdated = conexionDb.executeSqlUpdateWithParameters(sqlUpdateVendedor, param);
            if (rowsUpdated > 0) {
                String sqlUpdatePersona = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? WHERE id = (SELECT id_persona FROM vendedores WHERE codigo = ?)";
                param.clear();
                param.put(1, vendedor.getNombre());
                param.put(2, vendedor.getApellido());
                param.put(3, vendedor.getDni());
                param.put(4, vendedor.getEmail());
                param.put(5, vendedor.getTelefono());
                param.put(6, codigo); 

                int rowsPersonaUpdated = conexionDb.executeSqlUpdateWithParameters(sqlUpdatePersona, param);

                if (rowsPersonaUpdated > 0) {
                    System.out.println("El vendedor se actualizó con éxito.");
                } else {
                    System.out.println("No se encontró la persona asociada.");
                }
            } else {
                System.out.println("No se encontró el vendedor.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el vendedor: " + e.getMessage());
        }
    }
    
    @Override
    public Vendedor obtenerVendedor(String codigo) {
        String sqlVendedor = "SELECT * FROM vendedores v "
                + "INNER JOIN personas p ON p.id = v.id_persona "
                + "WHERE v.codigo = ?";  

        Vendedor vendedor = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(1, codigo);  

            System.out.println("Parameter 'codigo' added correctly to the HashMap: " + param.get(1));

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlVendedor, param);

            if (rs != null && rs.next()) { 
                vendedor = new Vendedor(
                        rs.getString("cuit"),
                        rs.getString("sucursal"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving the vendedor: " + e.getMessage());
        }

        return vendedor;
    }
    
    @Override
    public List<Vendedor> getVendedores(String codigo, String nombre, String apellido, int dni) {
        List<Vendedor> vendedores = new ArrayList<>();
        String sqlVendedores = "SELECT * FROM vendedores v INNER JOIN personas p ON p.id = v.id_persona WHERE 1 = 1";

        if (codigo != null && !codigo.isEmpty()) {
            sqlVendedores += " AND v.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlVendedores += " AND p.nombre = ?";
        }
        if (dni > 0) {
            sqlVendedores += " AND p.dni = ?";
        }

        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            int index = 1;
            
            if (codigo != null && !codigo.isEmpty()) {
                param.put(index++, codigo);
            }
            if (nombre != null && !nombre.isEmpty()) {
                param.put(index++, nombre);
            }
            if (dni > 0) {
                param.put(index++, dni);
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlVendedores, param);

            while (rs.next()) {  
                Vendedor vendedor = new Vendedor(
                        rs.getString("cuit"),
                        rs.getString("sucursal"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                vendedores.add(vendedor); 
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de vendedores: " + e.getMessage());
        }

        return vendedores;
    }
    
    @Override
    public String getProximoCodigoVendedor() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM vendedores";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.executeSqlQuery(sqlNextCode);
            if (rs.next()) {
                return "V-" + (rs.getInt("total") + 1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "V-1";
    }
}