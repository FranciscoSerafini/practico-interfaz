package negocio.abm.producto;

import java.util.List;
import negocio.abm.producto.exception.ModeloException;
import repositorio.dao.ConexionDb;
import repositorio.dao.modelo.IDaoModelo;
import repositorio.dao.modelo.ModeloDaoImpl;
import modelo.producto.marca.Modelo;

/**
 *
 * @author 54346
 */

public class ABMModelo implements IABMModelo {

    private ConexionDb conexionDb = new ConexionDb();
    private IDaoModelo iDaoModelo = new ModeloDaoImpl(conexionDb);

    private void validarDatosModelo(Modelo modelo) throws ModeloException {
        if (modelo == null) {
            throw new ModeloException("El modelo no puede ser nulo.");
        }
        if (modelo.getModelo() == null || modelo.getModelo().isEmpty()) {
            throw new ModeloException("El nombre del modelo no puede ser vacío.");
        }
        if (modelo.getDescripcion() == null || modelo.getDescripcion().isEmpty()) {
            throw new ModeloException("La descripción del modelo no puede ser vacía.");
        }
        if (modelo.getMarca() == null) {
            throw new ModeloException("La marca del modelo no puede ser nula.");
        }
        if (modelo.getRodado() == null) {
            throw new ModeloException("El rodado del modelo no puede ser nulo.");
        }
    }

    @Override
    public void altaModelo(Modelo modelo) throws ModeloException {
        validarDatosModelo(modelo);
        iDaoModelo.altaModelo(modelo);
        System.out.println("Modelo " + modelo.getModelo() + " dado de alta con éxito.");
    }

    @Override
    public void bajaModelo(int idModelo) throws ModeloException {
        Modelo modelo = iDaoModelo.obtenerModeloPorId(idModelo);
        if (modelo != null) {
            String nombreModelo = modelo.getModelo();
            iDaoModelo.bajaModelo(idModelo, nombreModelo); 
            System.out.println("Modelo con ID " + idModelo + " eliminado exitosamente.");
        } else {
            throw new ModeloException("El modelo con ID " + idModelo + " no existe.");
        }
    }

    @Override
    public void modificarModelo(int idModelo, Modelo modeloModificado) throws ModeloException {
        validarDatosModelo(modeloModificado);
        Modelo modeloExistente = iDaoModelo.obtenerModeloPorId(idModelo);
        if (modeloExistente != null) {
            iDaoModelo.modificarModelo(idModelo, modeloModificado);
            System.out.println("Modelo con ID " + idModelo + " modificado exitosamente.");
        } else {
            throw new ModeloException("El modelo con ID " + idModelo + " no existe.");
        }
    }

    @Override
    public List<Modelo> listarModelos(int idModelo, String nombreModelo) throws ModeloException {
        List<Modelo> modelos = iDaoModelo.listarModelos(idModelo, nombreModelo);
        if (modelos.isEmpty()) {
            System.out.println("No se encontraron modelos.");
        }
        return modelos;
    }
}