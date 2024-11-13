package negocio.abm.proveedor;

import modelo.proveedor.Proveedor;
import negocio.abm.proveedor.exception.ProveedorException;

/**
 *
 * @author 54346
 */

public interface IAMBProveedor{
    public void altaProveedor(Proveedor proveedor) throws ProveedorException;  

    public void bajaProveedor(Proveedor proveedor) throws ProveedorException;   
    
    public void modificarDatosProveedor(String codigo, Proveedor proveedorModificado) throws ProveedorException;  
    
    public void listarProveedor(String codigo, String nombre, String apellido, int dni);  
    
    public String asignarCodigoProveedor();
}
