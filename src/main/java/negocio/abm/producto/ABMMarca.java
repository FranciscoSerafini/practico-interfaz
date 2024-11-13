package negocio.abm.producto;

import java.util.List;
import negocio.abm.producto.exception.MarcaException;
import repositorio.dao.marca.MarcaDaoImpl;
import modelo.producto.marca.Marca;
import repositorio.dao.ConexionDb;
import repositorio.dao.marca.IDaoMarca;
import modelo.producto.marca.Modelo;


/**
 *
 * @author 54346
 */

public class ABMMarca implements IABMMarca {

    private ConexionDb conexionDb = new ConexionDb();
    private IDaoMarca iDaoMarca = new MarcaDaoImpl(conexionDb);

    private void validarDatosMarca(Marca marca) throws MarcaException {
        if (marca == null) {
            throw new MarcaException("La marca no puede ser nula.");
        }
        if (marca.getMarca() == null || marca.getMarca().isEmpty()) {
            throw new MarcaException("El nombre de la marca no puede ser vacío.");
        }
        if (marca.getModelos() == null || marca.getModelos().isEmpty()) {
            throw new MarcaException("La marca debe tener al menos un modelo asociado.");
        }
        for (Modelo modelo : marca.getModelos()) {
            if (modelo.getModelo() == null || modelo.getModelo().isEmpty()) {
                throw new MarcaException("El nombre del modelo no puede ser vacío.");
            }
        }
    }

    @Override
    public void altaMarca(Marca marca) throws MarcaException {
        validarDatosMarca(marca);
        iDaoMarca.altaMarca(marca);
        System.out.println("Marca " + marca.getMarca() + " dada de alta con éxito.");
    }

    @Override
    public void bajaMarca(int idMarca) throws MarcaException {
        Marca marca = iDaoMarca.obtenerMarcaPorId(idMarca);
        if (marca != null) {
            String nombreMarca = marca.getMarca();
            iDaoMarca.bajaMarca(idMarca, nombreMarca);
            System.out.println("Marca con ID " + idMarca + " eliminada exitosamente.");
        } else {
            throw new MarcaException("La marca con ID " + idMarca + " no existe.");
        }
    }

    @Override
    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException {
        validarDatosMarca(marcaModificada);
        Marca marcaExistente = iDaoMarca.obtenerMarcaPorId(idMarca);
        if (marcaExistente != null) {
            iDaoMarca.modificarMarca(idMarca, marcaModificada);
            System.out.println("Marca con ID " + idMarca + " modificada exitosamente.");
        } else {
            throw new MarcaException("La marca con ID " + idMarca + " no existe.");
        }
    }

    @Override
    public List<Marca> listarMarcas(int idMarca, String nombreMarca) throws MarcaException {
        List<Marca> marcas = iDaoMarca.listarMarcas(idMarca, nombreMarca);
        if (marcas.isEmpty()) {
            System.out.println("No se encontraron marcas.");
        }
        return marcas;
    }
}