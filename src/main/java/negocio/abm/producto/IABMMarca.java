package negocio.abm.producto;

import java.util.List;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author 54346
 */

public interface IABMMarca {
    public void altaMarca(Marca marca) throws MarcaException;

    public void bajaMarca(int codigoMarca) throws MarcaException;

    public void modificarMarca(int codigoMarca, Marca marcaModificada) throws MarcaException;

    public List<Marca> listarMarcas(int idMarca, String nombreMarca) throws MarcaException;
}
