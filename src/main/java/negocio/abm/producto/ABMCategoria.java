package negocio.abm.producto;

import java.util.List;
import modelo.producto.Categoria;
import negocio.abm.producto.exception.CategoriaException;
import repositorio.dao.ConexionDb;
import repositorio.dao.categoria.CategoriaDaoImpl;
import repositorio.dao.categoria.IdaoCategoria;

/**
 *
 * @author 54346
 */

public class ABMCategoria implements IABMCategoria {

    private ConexionDb conexionDb = new ConexionDb();
    private IdaoCategoria iDaoCategoria = new CategoriaDaoImpl(conexionDb);

    private void validarDatosCategoria(Categoria categoria) throws CategoriaException {
        if (categoria == null) {
            throw new CategoriaException("La categoría no puede ser nula.");
        }
        if (categoria.getCategoria() == null || categoria.getCategoria().isEmpty()) {
            throw new CategoriaException("El nombre de la categoría no puede ser vacío.");
        }
        if (categoria.getTipo() == null) {
            throw new CategoriaException("El tipo de la categoría no puede ser nulo.");
        }
    }

    @Override
    public void altaCategoria(Categoria categoria) throws CategoriaException {
        validarDatosCategoria(categoria);
        iDaoCategoria.altaCategoria(categoria);
        System.out.println("Categoría " + categoria.getCategoria() + " dada de alta con éxito.");
    }

    @Override
    public void bajaCategoria(int idCategoria, String nombreCategoria) throws CategoriaException {
        if (idCategoria != 0) {
            Categoria categoria = iDaoCategoria.obtenerCategoriaPorId(idCategoria);
            if (categoria != null) {
                iDaoCategoria.bajaCategoria(idCategoria, categoria.getCategoria());
                System.out.println("Categoría con ID " + idCategoria + " eliminada exitosamente.");
            } else {
                throw new CategoriaException("La categoría con ID " + idCategoria + " no existe.");
            }
        } else if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
            iDaoCategoria.bajaCategoria(0, nombreCategoria);
            System.out.println("Categoría con nombre " + nombreCategoria + " eliminada exitosamente.");
        } else {
            throw new CategoriaException("Debe proporcionar un ID o un nombre para eliminar la categoría.");
        }
    }

    @Override
    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) throws CategoriaException {
        validarDatosCategoria(categoriaModificada);
        Categoria categoriaExistente = iDaoCategoria.obtenerCategoriaPorId(idCategoria);
        if (categoriaExistente != null) {
            iDaoCategoria.modificarCategoria(idCategoria, categoriaModificada);
            System.out.println("Categoría con ID " + idCategoria + " modificada exitosamente.");
        } else {
            throw new CategoriaException("La categoría con ID " + idCategoria + " no existe.");
        }
    }

    @Override
    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria) throws CategoriaException {
        List<Categoria> categorias = iDaoCategoria.listarCategorias(idCategoria, nombreCategoria);
        if (categorias.isEmpty()) {
            System.out.println("No se encontraron categorías.");
        }
        return categorias;
    }
}