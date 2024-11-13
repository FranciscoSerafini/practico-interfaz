package negocio.abm.cliente;

import java.util.List;
import negocio.abm.cliente.Exception.ClienteException;
import modelo.cliente.Cliente;

/**
 *
 * @author 54346
 */

public interface IABMCliente {
    public void altaCliente(Cliente cliente) throws ClienteException;  
    
    public void bajaCliente(String codigo) throws ClienteException ;   

    public void modificarDatosCliente(String codigo, Cliente clienteModificado) throws ClienteException ;  

    public void listarClientes(String codigo, String nombre, String apellido, int dni);  

    public String asignarCodigoCliente();
    
    public List<Cliente> filtrarClientes(String nombre,String apellido, int dni);
}