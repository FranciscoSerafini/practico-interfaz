package repositorio.dao.cliente;

import java.util.List;
import modelo.cliente.Cliente;

/**
 *
 * @author 54346
 */

public interface IDaoCliente{
    public void insertarNuevoCliente(Cliente cliente);
    
    public void eliminarCliente(String codigo);
    
    public void modificarCliente(String codigo, Cliente cliente);

    public Cliente obtenerCliente(String codigo);

    public List<Cliente> getClientes(String codigo, String nombre, String apellido, int dni);
    
    public String getProximoCodigoCliente();
}