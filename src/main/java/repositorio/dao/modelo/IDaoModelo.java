package repositorio.dao.modelo;
import java.util.List;
import modelo.producto.marca.Modelo;
import negocio.abm.producto.exception.ModeloException;

/**
 *
 * @author 54346
 */

public interface IDaoModelo {
    public void altaModelo(Modelo modelo) throws ModeloException;

    public void bajaModelo(int codigoModelo, String nombreModelo) throws ModeloException;
    
    public void modificarModelo(int codigoModelo, Modelo modeloModificado) throws ModeloException;

    public List<Modelo> listarModelos(int idModelo, String nombreModelo) throws ModeloException;
    
    public Modelo obtenerModeloPorId(int codigoModelo) throws ModeloException;
}