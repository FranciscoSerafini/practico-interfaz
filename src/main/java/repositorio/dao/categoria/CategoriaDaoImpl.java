package repositorio.dao.categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.Categoria;
import modelo.producto.TipoCategoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author 54346
 */

public class CategoriaDaoImpl implements IdaoCategoria {

    private ConexionDb conexionDb;

    private static final String SQL_INSERT_CATEGORIA = "INSERT INTO categorias (id, categoria, tipo) VALUES (?, ?, ?)";
    private static final String SQL_GET_CATEGORIA_BY_ID = "SELECT * FROM categorias WHERE id = ?";
    private static final String SQL_DELETE_CATEGORIA_BY_ID = "DELETE FROM categorias WHERE id = ?";
    private static final String SQL_GET_CATEGORIA_BY_NOMBRE = "SELECT * FROM categorias WHERE categoria = ?";
    private static final String SQL_DELETE_CATEGORIA_BY_NOMBRE = "DELETE FROM categorias WHERE categoria = ?";
    private static final String SQL_UPDATE_CATEGORIA = "UPDATE categorias SET categoria = ?, tipo = ? WHERE id = ?";

    public CategoriaDaoImpl(ConexionDb conexionDb) {
        this.conexionDb = conexionDb;
    }

    @Override
    public void altaCategoria(Categoria categoria) throws CategoriaException {
        String nombreCategoria = categoria.getCategoria();
        TipoCategoria tipoCategoria = categoria.getTipo();

        try (PreparedStatement stmtCategoria = conexionDb.dbConection().prepareStatement(SQL_INSERT_CATEGORIA)) {
            stmtCategoria.setString(1, nombreCategoria); 
            stmtCategoria.setString(2, nombreCategoria); 
            stmtCategoria.setString(3, tipoCategoria != null ? tipoCategoria.toString() : ""); 

            int affectedRows = stmtCategoria.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Categoría " + nombreCategoria + " insertada con éxito.");
            } else {
                System.out.println("Error al insertar la categoría.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void bajaCategoria(int idCategoria, String nombreCategoria) throws CategoriaException {
        try {
            if (idCategoria != 0) {
                
                if (categoriaExistsById(idCategoria)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_CATEGORIA_BY_ID)) {
                        stmtDelete.setInt(1, idCategoria);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Categoría con id " + idCategoria + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la categoría con id " + idCategoria);
                        }
                    }
                } else {
                    System.out.println("La categoría con id " + idCategoria + " no existe.");
                }
            } else if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                
                if (categoriaExistsByName(nombreCategoria)) {
                    try (PreparedStatement stmtDelete = conexionDb.dbConection().prepareStatement(SQL_DELETE_CATEGORIA_BY_NOMBRE)) {
                        stmtDelete.setString(1, nombreCategoria);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Categoría con nombre " + nombreCategoria + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la categoría con nombre " + nombreCategoria);
                        }
                    }
                } else {
                    System.out.println("La categoría con nombre " + nombreCategoria + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) throws CategoriaException {
        try {
            if (categoriaExistsById(idCategoria)) {
                try (PreparedStatement stmtUpdate = conexionDb.dbConection().prepareStatement(SQL_UPDATE_CATEGORIA)) {
                    stmtUpdate.setString(1, categoriaModificada.getCategoria());
                    stmtUpdate.setString(2, categoriaModificada.getTipo() != null ? categoriaModificada.getTipo().toString() : ""); 
                    stmtUpdate.setInt(3, idCategoria);

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Categoría con id " + idCategoria + " actualizada exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar la categoría con id " + idCategoria);
                    }
                }
            } else {
                System.out.println("La categoría con id " + idCategoria + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la categoría: " + e.getMessage());
        }
    }

    @Override
    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria) throws CategoriaException {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {

            if (idCategoria != 0) {
                sqlQuery.append(" AND id = ?");
                param.put(index++, idCategoria);
            }

            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria LIKE ?");
                param.put(index++, "%" + nombreCategoria + "%");
            }

            ResultSet rs = conexionDb.executeSqlQueryWithParameters(sqlQuery.toString(), param);

            while (rs.next()) {
                String nombre = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo); 

                Categoria categoria = new Categoria(nombre, tipoCategoria);
                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de categorías: " + e.getMessage());
        }

        return categorias;
    }

    @Override
    public Categoria obtenerCategoriaPorId(int idCategoria) throws CategoriaException {
        Categoria categoria = null;
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_CATEGORIA_BY_ID)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreCategoria = rs.getString("categoria");
                String tipo = rs.getString("tipo");
                TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo); 

                categoria = new Categoria(nombreCategoria, tipoCategoria);
            } else {
                throw new CategoriaException("No se encontró una categoría con el ID " + idCategoria); 
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la categoría por ID: " + e.getMessage());
            throw new CategoriaException("Error al obtener la categoría: " + e.getMessage()); 
        }
        return categoria;
    }
    
    private boolean categoriaExistsById(int idCategoria) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_CATEGORIA_BY_ID)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean categoriaExistsByName(String nombreCategoria) throws SQLException {
        try (PreparedStatement stmt = conexionDb.dbConection().prepareStatement(SQL_GET_CATEGORIA_BY_NOMBRE)) {
            stmt.setString(1, nombreCategoria);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}