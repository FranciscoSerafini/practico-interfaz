package negocio.abm.producto;

import java.util.List;
import modelo.producto.Producto;
import negocio.abm.producto.exception.ProductoException;
import repositorio.dao.ConexionDb;
import repositorio.dao.producto.IDaoProducto;
import repositorio.dao.producto.ProductoDaoImpl;

/**
 *
 * @author 54346
 */

public class ABMProducto implements IABMProducto {

    private ConexionDb conexionDb = new ConexionDb();
    private IDaoProducto iDaoProducto = new ProductoDaoImpl(conexionDb);

    private void validarDatosProducto(Producto producto) throws ProductoException {
        if (producto == null) {
            throw new ProductoException("El producto no puede ser nulo.");
        }
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new ProductoException("El nombre del producto no puede ser vacío.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
            throw new ProductoException("La descripción del producto no puede ser vacía.");
        }
        if (producto.getPrecio() <= 0) {
            throw new ProductoException("El precio del producto debe ser mayor que cero.");
        }
        if (producto.getCantidad() < 0) {
            throw new ProductoException("La cantidad del producto no puede ser negativa.");
        }
    }

    @Override
    public void altaProducto(Producto producto) throws ProductoException {
        validarDatosProducto(producto);
        iDaoProducto.altaProducto(producto);
        System.out.println("Producto " + producto.getNombre() + " dado de alta con éxito.");
    }

    @Override
    public void bajaProducto(int idProducto) throws ProductoException {
        Producto producto = iDaoProducto.obtenerProductoPorId(idProducto);
        if (producto != null) {
            iDaoProducto.bajaProducto(idProducto);
            System.out.println("Producto con ID " + idProducto + " eliminado exitosamente.");
        } else {
            throw new ProductoException("El producto con ID " + idProducto + " no existe.");
        }
    }

    @Override
    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException {
        validarDatosProducto(productoModificado);
        Producto productoExistente = iDaoProducto.obtenerProductoPorId(idProducto);
        if (productoExistente != null) {
            iDaoProducto.modificarProducto(idProducto, productoModificado);
            System.out.println("Producto con ID " + idProducto + " modificado exitosamente.");
        } else {
            throw new ProductoException("El producto con ID " + idProducto + " no existe.");
        }
    }

    @Override
    public List<Producto> listarProductos(int idProducto, String nombreProducto) throws ProductoException {
        List<Producto> productos = iDaoProducto.listarProductos(idProducto, nombreProducto);
        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
        }
        return productos;
    }
}