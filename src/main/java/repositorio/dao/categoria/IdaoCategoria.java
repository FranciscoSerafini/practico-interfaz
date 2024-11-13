package repositorio.dao.categoria;

import java.util.List;
import modelo.producto.Categoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author 54346
 */

public interface IdaoCategoria {
    public void altaCategoria(Categoria categoria) throws CategoriaException;

    public void bajaCategoria(int codigoCategoria, String nombreCategoria) throws CategoriaException;

    public void modificarCategoria(int codigoCategoria, Categoria categoriaModificada) throws CategoriaException;

    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria) throws CategoriaException;
    
    public Categoria obtenerCategoriaPorId(int idCategoria) throws CategoriaException;
}