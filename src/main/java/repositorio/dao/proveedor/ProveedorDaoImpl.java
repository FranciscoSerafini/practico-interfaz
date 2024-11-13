package repositorio.dao.proveedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.proveedor.Proveedor;
import repositorio.dao.ConexionDb;

/**
 *
 * @author 54346
 */

public class ProveedorDaoImpl implements IDaoProveedor{

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_PERSONA = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_PROVEEDOR = "INSERT INTO proveedores (id_persona, codigo, nombre_fantasia, cuit) VALUES (?, ?, ?, ?)";
    private static final String SQL_LIST_PROVEEDORES = "SELECT * FROM proveedores pr INNER JOIN personas p ON p.id = pr.id_persona WHERE 1 = 1";

    @Override
    public void insertarNuevoProveedor(Proveedor proveedor) {
        String codigoProveedor = getProximoCodigoProveedor();

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.dbConection().prepareStatement(SQL_INSERT_PERSONA, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, proveedor.getNombre());
            stmtPersona.setString(2, proveedor.getApellido());
            stmtPersona.setInt(3, proveedor.getDni());
            stmtPersona.setString(4, proveedor.getTelefono());
            stmtPersona.setString(5, proveedor.getEmail());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1);

                    PreparedStatement stmtProveedor = conexionDb.dbConection().prepareStatement(SQL_INSERT_PROVEEDOR);
                    stmtProveedor.setInt(1, personId);                
                    stmtProveedor.setString(2, codigoProveedor);       
                    stmtProveedor.setString(3, proveedor.getNombreFantasia()); 
                    stmtProveedor.setString(4, proveedor.getCuit());   

                    int affectedRowsProveedor = stmtProveedor.executeUpdate();

                    if (affectedRowsProveedor > 0) {
                        System.out.println("Nuevo proveedor insertado con éxito con código: " + codigoProveedor);
                    } else {
                        System.out.println("Error al insertar el proveedor.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el proveedor: " + e.getMessage());
        }
    }

    @Override
    public void modificarProveedor(String codigo, Proveedor proveedor) {
        conexionDb = new ConexionDb();

        String sqlUpdateProveedor = "UPDATE proveedores SET codigo = ?, nombre_fantasia = ?, cuit = ? WHERE codigo = ?";
        HashMap<Integer, Object> paramProveedor = new HashMap<>();
        paramProveedor.put(1, proveedor.getCodigo());
        paramProveedor.put(2, proveedor.getNombreFantasia());
        paramProveedor.put(3, proveedor.getCuit());
        paramProveedor.put(4, codigo);

        try {
            conexionDb.executeSqlWithParameters(sqlUpdateProveedor, paramProveedor);

            String sqlUpdatePersona = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? "
                    + "WHERE id = (SELECT id_persona FROM proveedores WHERE codigo = ?)";

            HashMap<Integer, Object> paramPersona = new HashMap<>();
            paramPersona.put(1, proveedor.getNombre());
            paramPersona.put(2, proveedor.getApellido());
            paramPersona.put(3, proveedor.getDni());
            paramPersona.put(4, proveedor.getEmail());
            paramPersona.put(5, proveedor.getTelefono());
            paramPersona.put(6, codigo);

            conexionDb.executeSqlWithParameters(sqlUpdatePersona, paramPersona);

            System.out.println("El proveedor se actualizó con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al modificar el proveedor: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProveedor(String codigo) {
        String sqlProveedorId = "SELECT p.id FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona WHERE pr.codigo = ?";
        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteProveedor = "DELETE FROM proveedores WHERE codigo = ?";

        HashMap<Integer, Object> param = new HashMap<>();
        param.put(1, codigo);

        Integer idPersona = null;
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlProveedorId, param);
            if (rs.next()) {
                idPersona = rs.getInt("id");
            }

            if (idPersona != null) {
                param.clear();
                param.put(1, codigo);
                conexionDb.executeSqlWithParameters(sqlDeleteProveedor, param);

                param.clear();
                param.put(1, idPersona);
                conexionDb.executeSqlWithParameters(sqlDeletePerson, param);

                System.out.println("El proveedor se eliminó exitosamente.");
            } else {
                System.out.println("Proveedor no encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Proveedor obtenerProveedor(String codigo) {
        String sqlProveedor = "SELECT pr.codigo, pr.nombre_fantasia, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE pr.codigo = ?";

        Proveedor proveedor = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(1, codigo);

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlProveedor, param);

            if (rs.next()) {
                proveedor = new Proveedor(
                        rs.getString("cuit"),
                        rs.getString("nombre_fantasia"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el proveedor: " + e.getMessage());
        }

        return proveedor;
    }

    @Override
    public List<Proveedor> getProveedor(String codigo, String nombre, String apellido, int dni) {
        List<Proveedor> proveedores = new ArrayList<>();
        conexionDb = new ConexionDb();

        try {
            StringBuilder sqlQuery = new StringBuilder(SQL_LIST_PROVEEDORES);
            HashMap<Integer, Object> param = new HashMap<>();
            int index = 1;

            if (codigo != null && !codigo.isEmpty()) {
                sqlQuery.append(" AND pr.codigo = ?");
                param.put(index++, codigo);
            }
            if (nombre != null && !nombre.isEmpty()) {
                sqlQuery.append(" AND p.nombre = ?");
                param.put(index++, nombre);
            }
            if (apellido != null && !apellido.isEmpty()) {
                sqlQuery.append(" AND p.apellido = ?");
                param.put(index++, apellido);
            }
            if (dni > 0) {
                sqlQuery.append(" AND p.dni = ?");
                param.put(index++, dni);
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery.toString(), param);

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getString("codigo"),
                        rs.getString("nombre_fantasia"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de proveedores: " + e.getMessage());
        }

        return proveedores;
    }

    @Override
    public String getProximoCodigoProveedor() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM proveedores";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.executeSqlQuery(sqlNextCode);

            if (rs.next()) {
                int nextId = rs.getInt("total") + 1;
                return "P-" + Year.now().getValue() + "-" + nextId; 
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "P-" + Year.now().getValue() + "-1"; 
    }
}