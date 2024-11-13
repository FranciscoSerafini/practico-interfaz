package repositorio.dao.vendedor;

import java.util.List;
import modelo.vendedor.Vendedor;

/**
 *
 * @author 54346
 */

public interface IDaoVendedor {
    public void insertarNuevoVendedor(Vendedor vendedor);

    public void eliminarVendedor(String codigo);

    public void modificarVendedor(String codigo, Vendedor vendedor);

    public Vendedor obtenerVendedor(String codigo);

    public List<Vendedor> getVendedores(String codigo, String nombre, String apellido, int dni);

    public String getProximoCodigoVendedor();
}
