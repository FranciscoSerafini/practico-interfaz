package repositorio.dao.marca;

import java.util.List;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author 54346
 */

public interface IDaoMarca{
    public void altaMarca(Marca marca) throws MarcaException;

    public void bajaMarca(int idMarca, String nombreMarca) throws MarcaException;

    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException;

    public List<Marca> listarMarcas(int idMarca, String nombreMarca) throws MarcaException;
    
    public Marca obtenerMarcaPorId(int idMarca);
}
