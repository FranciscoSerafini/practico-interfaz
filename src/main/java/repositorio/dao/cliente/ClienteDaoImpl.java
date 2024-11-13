package repositorio.dao.cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.cliente.Cliente;
import repositorio.dao.ConexionDb;

/**
 *
 * @author 54346
 */

public class ClienteDaoImpl implements IDaoCliente{

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_PERSONA = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CLIENTE = "INSERT INTO clientes (codigo, id_persona, cuil) VALUES (?, ?, ?)";

    @Override
    public void insertarNuevoCliente(Cliente cliente) {
        
        String codigoCliente = getProximoCodigoCliente();

        String sqlPersona = SQL_INSERT_PERSONA;
        HashMap<Integer, Object> paramPersona = new HashMap<>();
        paramPersona.put(1, cliente.getNombre());
        paramPersona.put(2, cliente.getApellido());
        paramPersona.put(3, cliente.getDni());
        paramPersona.put(4, cliente.getEmail());
        paramPersona.put(5, cliente.getTelefono());

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.dbConection().prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, cliente.getNombre());
            stmtPersona.setString(2, cliente.getApellido());
            stmtPersona.setInt(3, cliente.getDni());
            stmtPersona.setString(4, cliente.getEmail());
            stmtPersona.setString(5, cliente.getTelefono());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1); 

                    String sqlClient = SQL_INSERT_CLIENTE;
                    PreparedStatement stmtClient = conexionDb.dbConection().prepareStatement(sqlClient);
                    stmtClient.setString(1, codigoCliente); 
                    stmtClient.setInt(2, personId);
                    stmtClient.setString(3, cliente.getCuil()); 

                    int affectedRowsClient = stmtClient.executeUpdate();

                    if (affectedRowsClient > 0) {
                        System.out.println("Nuevo cliente insertado con éxito con código: " + codigoCliente);
                    } else {
                        System.out.println("Error al insertar el cliente.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: " + e.getMessage());
        }
    }

    @Override
    public void eliminarCliente(String codigo) {
        String sqlClientId = "SELECT id_persona FROM clientes WHERE codigo = ?";  

        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteClient = "DELETE FROM clientes WHERE codigo = ?";

        HashMap<Integer, Object> param = new HashMap<>();
        param.put(0, codigo);  

        Integer idPersona = 0;  

        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlClientId, param);
            if (rs.next()) {
                idPersona = rs.getInt("id_persona");  
            }

            if (idPersona != 0) {
                param.clear();
                param.put(0, codigo);
                conexionDb.executeSqlUpdateWithParameters(sqlDeleteClient, param);  

                param.clear();  
                param.put(0, idPersona);
                conexionDb.executeSqlUpdateWithParameters(sqlDeletePerson, param);  

                System.out.println("El cliente se eliminó exitosamente");
            } else {
                System.out.println("Cliente no encontrado, no se pudo eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    @Override
    public void modificarCliente(String codigo, Cliente cliente) {
        conexionDb = new ConexionDb();  

        String sqlUpdateClient = "UPDATE clientes SET cuil = ? WHERE codigo = ?";

        HashMap<Integer, Object> param = new HashMap<>();
        param.put(0, cliente.getCuil());   
        param.put(1, codigo);               

        try {
            int rowsUpdated = conexionDb.executeSqlUpdateWithParameters(sqlUpdateClient, param);

            if (rowsUpdated > 0) {

                String sqlUpdatePer = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? "
                        + "WHERE id = (SELECT id_persona FROM clientes WHERE codigo = ?)";

                param.clear();
                param.put(0, cliente.getNombre());  
                param.put(1, cliente.getApellido()); 
                param.put(2, cliente.getDni());     
                param.put(3, cliente.getEmail());  
                param.put(4, cliente.getTelefono()); 
                param.put(5, codigo);             

                int rowsPersonUpdated = conexionDb.executeSqlUpdateWithParameters(sqlUpdatePer, param);

                if (rowsPersonUpdated > 0) {
                    System.out.println("El cliente se actualizó con éxito");
                } else {
                    System.out.println("No se encontró la persona asociada al código proporcionado");
                }
            } else {
                System.out.println("No se encontró el cliente con el código proporcionado");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar el cliente: " + e.getMessage());
        } 
    }
    
    @Override
    public Cliente obtenerCliente(String codigo) {
        String sqlClient = "SELECT * FROM clientes c "
                + "INNER JOIN personas p ON p.id = c.id_persona "
                + "WHERE c.codigo = ?";  

        Cliente cliente = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, codigo);  

            System.out.println("Parámetro 'codigo' añadido correctamente al HashMap: " + param.get(0));
            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlClient, param);

            // Procesar el resultado
            if (rs != null && rs.next()) {
                cliente = new Cliente(
                        rs.getString("cuil"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el cliente: " + e.getMessage());
        }

        return cliente;
    }

    @Override
    public List<Cliente> getClientes(String codigo, String nombre, String apellido, int dni) {
        List<Cliente> clientes = new ArrayList<>();
        String sqlClients = "SELECT * FROM clientes c "
                + "INNER JOIN personas p ON p.id = c.id_persona "
                + "WHERE 1 = 1 ";

        if (codigo != null && !codigo.isEmpty()) {
            sqlClients += " AND c.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlClients += " AND p.nombre = ?";
        }
        if (dni > 0) {
            sqlClients += " AND p.dni = ?";
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

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlClients, param);

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cuil"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de clientes: " + e.getMessage());
        }

        return clientes;
    }

    @Override
    public String getProximoCodigoCliente(){
        String sqlNextCode = "SELECT MAX(id) AS total FROM clientes";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.executeSqlQuery(sqlNextCode);

            if (rs.next()) {
                return "C-" + (rs.getInt("total") + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "C-1"; 
    }
}