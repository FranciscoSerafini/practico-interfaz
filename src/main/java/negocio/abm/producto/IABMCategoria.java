package negocio.abm.producto;

import java.util.List;
import modelo.producto.Categoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author 54346
 */

public interface IABMCategoria {
    public void altaCategoria(Categoria categoria) throws CategoriaException;

    public void bajaCategoria(int idCategoria, String nombreCategoria) throws CategoriaException;

    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) throws CategoriaException;

    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria) throws CategoriaException;
}