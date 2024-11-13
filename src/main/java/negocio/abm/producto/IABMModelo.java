package negocio.abm.producto;

import java.util.List;
import modelo.producto.marca.Modelo;
import negocio.abm.producto.exception.ModeloException;

/**
 *
 * @author 54346
 */

public interface IABMModelo {
    public void altaModelo(Modelo modelo) throws ModeloException;

    public void bajaModelo(int idModelo) throws ModeloException;

    public void modificarModelo(int idModelo, Modelo modeloModificado) throws ModeloException;

    public List<Modelo> listarModelos(int idModelo, String nombreModelo) throws ModeloException;
}