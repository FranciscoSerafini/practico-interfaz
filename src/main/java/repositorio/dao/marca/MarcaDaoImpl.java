package repositorio.dao.marca;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author 54346
 */

public class MarcaDaoImpl implements IDaoMarca {

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_MARCA = "INSERT INTO marcas (idMarca, nombre, descripcion) VALUES (?, ?, ?)";
    private static final String SQL_GET_MARCA_BY_ID = "SELECT * FROM marcas WHERE idMarca = ?";
    private static final String SQL_DELETE_MARCA_BY_ID = "DELETE FROM marcas WHERE idMarca = ?";
    private static final String SQL_GET_MARCA_BY_NOMBRE = "SELECT * FROM marcas WHERE nombre = ?";
    private static final String SQL_DELETE_MARCA_BY_NOMBRE = "DELETE FROM marcas WHERE nombre = ?";
    private static final String SQL_UPDATE_MARCA = "UPDATE marcas SET nombre = ?, descripcion = ? WHERE idMarca = ?";

    public MarcaDaoImpl(ConexionDb conexionDb) {
        this.conexionDb = conexionDb;
    }

    @Override
    public void altaMarca(Marca marca) throws MarcaException {
        String nombreMarca = marca.getMarca();
        String descripcion = marca.getModelos() != null ? marca.getModelos().toString() : "";

        try (PreparedStatement stmtMarca = conexionDb.dbConection().prepareStatement(SQL_INSERT_MARCA)) {
            stmtMarca.setString(1, nombreMarca);
            stmtMarca.setString(2, nombreMarca);
            stmtMarca.setString(3, descripcion); 

            int affectedRows = stmtMarca.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Marca " + nombreMarca + " insertada con éxito.");
            } else {
                System.out.println("Error al insertar la marca.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la marca: " + e.getMessage());
        }
    }

    @Override
    public void bajaMarca(int idMarca, String nombreMarca) throws MarcaException {
        try {
            if (idMarca != 0) {
                
                if (marcaExistsById(idMarca)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_MARCA_BY_ID)) {
                        stmtDelete.setInt(1, idMarca);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Marca con idMarca " + idMarca + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la marca con idMarca " + idMarca);
                        }
                    }
                } else {
                    System.out.println("La marca con idMarca " + idMarca + " no existe.");
                }
            } else if (nombreMarca != null && !nombreMarca.isEmpty()) {
                
                if (marcaExistsByName(nombreMarca)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_MARCA_BY_NOMBRE)) {
                        stmtDelete.setString(1, nombreMarca);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Marca con nombre " + nombreMarca + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la marca con nombre " + nombreMarca);
                        }
                    }
                } else {
                    System.out.println("La marca con nombre " + nombreMarca + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la marca: " + e.getMessage());
        }
    }

    @Override
    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException {
        try {
            if (marcaExistsById(idMarca)) {
                try (PreparedStatement stmtUpdate = conexionDb.dbConection().prepareStatement(SQL_UPDATE_MARCA)) {
                    stmtUpdate.setString(1, marcaModificada.getMarca());
                    stmtUpdate.setString(2, marcaModificada.getModelos() != null ? marcaModificada.getModelos().toString() : "");
                    stmtUpdate.setInt(3, idMarca);

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Marca con idMarca " + idMarca + " actualizada exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar la marca con idMarca " + idMarca);
                    }
                }
            } else {
                System.out.println("La marca con idMarca " + idMarca + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la marca: " + e.getMessage());
        }
    }

    @Override
    public List<Marca> listarMarcas(int idMarca, String nombreMarca) throws MarcaException {
        List<Marca> marcas = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM marcas WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            if (idMarca != 0) {
                sqlQuery.append(" AND idMarca = ?");
                param.put(index++, idMarca);
            }

            if (nombreMarca != null && !nombreMarca.isEmpty()) {
                sqlQuery.append(" AND nombre LIKE ?");
                param.put(index++, "%" + nombreMarca + "%");
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery.toString(), param);

            while (rs.next()) {
                String nombre = rs.getString("nombre"); 
                
                Marca marca = new Marca(nombre);
                marca.setModelos(new ArrayList<>()); 

                marcas.add(marca);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de marcas: " + e.getMessage());
        }

        return marcas;
    }

    @Override
    public Marca obtenerMarcaPorId(int idMarca) {
        Marca marca = null;

        String sql = "SELECT * FROM marcas WHERE idMarca = ?";

        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(sql)) {
            stmt.setInt(1, idMarca); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                marca = new Marca(nombre);
                marca.setModelos(new ArrayList<>());  
            } else {
                System.out.println("La marca con idMarca " + idMarca + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la marca por idMarca: " + e.getMessage());
        }

        return marca;
    }
    
    private boolean marcaExistsById(int idMarca) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_MARCA_BY_ID)) {
            stmt.setInt(1, idMarca); 
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean marcaExistsByName(String nombreMarca) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_MARCA_BY_NOMBRE)) {
            stmt.setString(1, nombreMarca);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}