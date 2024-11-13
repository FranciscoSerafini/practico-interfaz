package repositorio.dao.modelo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.producto.marca.Marca;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
import negocio.abm.producto.exception.ModeloException;


/**
 *
 * @author 54346
 */

public class ModeloDaoImpl implements IDaoModelo {

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_MODELO = "INSERT INTO modelos (modelo, id_marca, descripcion, id_rodado) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_MODELO_BY_ID = "SELECT * FROM modelos WHERE id = ?";
    private static final String SQL_DELETE_MODELO_BY_ID = "DELETE FROM modelos WHERE id = ?";
    private static final String SQL_GET_MODELO_BY_NOMBRE = "SELECT * FROM modelos WHERE modelo = ?";
    private static final String SQL_DELETE_MODELO_BY_NOMBRE = "DELETE FROM modelos WHERE modelo = ?";
    private static final String SQL_UPDATE_MODELO = "UPDATE modelos SET modelo = ?, id_marca = ?, descripcion = ?, id_rodado = ? WHERE id = ?";

    public ModeloDaoImpl(ConexionDb conexionDb) {
        this.conexionDb = conexionDb;
    }

    @Override
    public void altaModelo(Modelo modelo) throws ModeloException {
        String nombreModelo = modelo.getModelo();
        Marca marca = modelo.getMarca();
        String descripcion = modelo.getDescripcion();
        Rodado rodado = modelo.getRodado();

        try (PreparedStatement stmtModelo = conexionDb.dbConection().prepareStatement(SQL_INSERT_MODELO)) {
            stmtModelo.setString(1, nombreModelo);
            stmtModelo.setString(2, marca.getMarca()); 
            stmtModelo.setString(3, descripcion); 
            stmtModelo.setString(4, rodado.toString());

            int affectedRows = stmtModelo.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Modelo " + nombreModelo + " insertado con éxito.");
            } else {
                System.out.println("Error al insertar el modelo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void bajaModelo(int idModelo, String nombreModelo) throws ModeloException {
        try {
            if (idModelo != 0) {

                if (modeloExistsById(idModelo)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_MODELO_BY_ID)) {
                        stmtDelete.setInt(1, idModelo);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Modelo con ID " + idModelo + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el modelo con ID " + idModelo);
                        }
                    }
                } else {
                    System.out.println("El modelo con ID " + idModelo + " no existe.");
                }
            } else if (nombreModelo != null && !nombreModelo.isEmpty()) {

                if (modeloExistsByName(nombreModelo)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_MODELO_BY_NOMBRE)) {
                        stmtDelete.setString(1, nombreModelo);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Modelo con nombre " + nombreModelo + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el modelo con nombre " + nombreModelo);
                        }
                    }
                } else {
                    System.out.println("El modelo con nombre " + nombreModelo + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void modificarModelo(int idModelo, Modelo modeloModificado) throws ModeloException {
        try {
            if (modeloExistsById(idModelo)) {
                try (PreparedStatement stmtUpdate = conexionDb.dbConection().prepareStatement(SQL_UPDATE_MODELO)) {
                    stmtUpdate.setString(1, modeloModificado.getModelo());
                    stmtUpdate.setString(2, modeloModificado.getMarca().getMarca());
                    stmtUpdate.setString(3, modeloModificado.getDescripcion());
                    stmtUpdate.setString(4, modeloModificado.getRodado().toString());
                    stmtUpdate.setInt(5, idModelo);

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Modelo con ID " + idModelo + " actualizado exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar el modelo con ID " + idModelo);
                    }
                }
            } else {
                System.out.println("El modelo con ID " + idModelo + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el modelo: " + e.getMessage());
        }
    }

    @Override
    public List<Modelo> listarModelos(int idModelo, String nombreModelo) throws ModeloException {
        List<Modelo> modelos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT m.id, m.modelo, m.id_marca, m.descripcion, m.id_rodado, mar.nombre AS nombre_marca ");
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.id_marca = mar.codigo ");
        sqlQuery.append("WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {

            if (idModelo != 0) {
                sqlQuery.append(" AND m.id = ?");
                param.put(index++, idModelo);
            }

            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                sqlQuery.append(" AND m.modelo LIKE ?");
                param.put(index++, "%" + nombreModelo + "%");
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery.toString(), param);

            while (rs.next()) {
                String nombre = rs.getString("modelo");
                Marca marca = new Marca(rs.getString("nombre_marca"));
                Rodado rodado = Rodado.valueOf(rs.getString("id_rodado"));
                String descripcion = rs.getString("descripcion");

                Modelo modelo = new Modelo(nombre, marca, descripcion, rodado);
                modelos.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos: " + e.getMessage());
        }

        return modelos;
    }

    @Override
    public Modelo obtenerModeloPorId(int idModelo) {
        Modelo modelo = null;
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_MODELO_BY_ID)) {
            stmt.setInt(1, idModelo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("modelo");
                String descripcion = rs.getString("descripcion");

                String marcaId = rs.getString("id_marca");
                Marca marca = new Marca(marcaId);

                Rodado rodado = Rodado.valueOf(rs.getString("id_rodado"));

                modelo = new Modelo(nombre, marca, descripcion, rodado);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el modelo por ID: " + e.getMessage());
        }
        return modelo;
    }

    private boolean modeloExistsById(int idModelo) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_MODELO_BY_ID)) {
            stmt.setInt(1, idModelo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean modeloExistsByName(String nombreModelo) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_MODELO_BY_NOMBRE)) {
            stmt.setString(1, nombreModelo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}