package repositorio.dao.producto;

import java.util.List;
import modelo.producto.Producto;
import negocio.abm.producto.exception.ProductoException;

/**
 *
 * @author 54346
 */

public interface IDaoProducto {
    public void altaProducto(Producto producto) throws ProductoException;

    public void bajaProducto(int idProducto) throws ProductoException;

    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException;

    public List<Producto> listarProductos(int idProducto, String nombreProducto) throws ProductoException;
    
    public Producto obtenerProductoPorId(int idProducto) throws ProductoException;
}