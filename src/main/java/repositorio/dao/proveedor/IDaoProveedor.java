package repositorio.dao.proveedor;

import java.util.List;
import modelo.proveedor.Proveedor;

/**
 *
 * @author 54346
 */

public interface IDaoProveedor{
    public void insertarNuevoProveedor(Proveedor proveedor);

    public void modificarProveedor(String codigo, Proveedor proveedor);

    public void eliminarProveedor(String codigo);

    public Proveedor obtenerProveedor(String codigo);

    public List<Proveedor> getProveedor(String codigo, String nombre, String apellido, int dni);

    public String getProximoCodigoProveedor();
}
