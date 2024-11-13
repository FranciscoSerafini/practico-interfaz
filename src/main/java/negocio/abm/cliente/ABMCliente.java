package negocio.abm.cliente;

import java.util.List;
import modelo.cliente.Cliente;
import negocio.abm.cliente.Exception.ClienteException;
import repositorio.dao.cliente.ClienteDaoImpl;
import repositorio.dao.cliente.IDaoCliente;

/**
 *
 * @author 54346
*/

public class ABMCliente implements IABMCliente{

    private IDaoCliente iDaoCliente = new ClienteDaoImpl();

    private void validarDatosCliente(Cliente cliente) throws ClienteException {
        if (cliente == null) {
            throw new ClienteException("El cliente no puede ser nulo");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ClienteException("El nombre del cliente no puede ser vacío");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
            throw new ClienteException("El apellido del cliente no puede ser vacío");
        }
        if (cliente.getDni() <= 0 || cliente.getDni() > Integer.MAX_VALUE) {
            throw new ClienteException("El valor del DNI no es correcto");
        }
    }

    @Override
    public String asignarCodigoCliente() {
        return "C-" + iDaoCliente.getProximoCodigoCliente();
    }

    @Override
    public void altaCliente(Cliente cliente) throws ClienteException{
        validarDatosCliente(cliente);

        String codigo = asignarCodigoCliente();
        cliente.setCodigo(codigo);  

        if (iDaoCliente.obtenerCliente(cliente.getCodigo()) == null) {
            iDaoCliente.insertarNuevoCliente(cliente);
            System.out.println("El cliente se agregó de forma correcta");
        } else {
            System.out.println("El cliente ya existe");
        }
    }

    @Override
    public void bajaCliente(String codigo) throws ClienteException {
        
        if (codigo != null && !codigo.isEmpty()) {
            iDaoCliente.eliminarCliente(codigo);
            System.out.println("El Cliente con código: " + codigo + " fue eliminado con éxito");
        } else {
            System.out.println("El código del cliente a eliminar no puede ser nulo o vacío");
        }
    }

    @Override
    public void modificarDatosCliente(String codigo, Cliente clienteModificado) throws ClienteException {
        validarDatosCliente(clienteModificado);

        Cliente clienteExistente = iDaoCliente.obtenerCliente(codigo);
        if (clienteExistente != null) {
            iDaoCliente.modificarCliente(codigo, clienteModificado);
            System.out.println("Los datos del cliente han sido modificados correctamente");
        } else {
            System.out.println("El cliente no existe");
        }
    }

    @Override
    public void listarClientes(String codigo, String nombre, String apellido, int dni) {
        List<Cliente> clientes = iDaoCliente.getClientes(codigo, nombre, apellido, dni);

        for (Cliente cli : clientes) {
            System.out.println(cli.toString());
        }
    }
    
    @Override
    public List<Cliente> filtrarClientes(String nombre,String apellido, int dni){
        return iDaoCliente.getClientes(null, nombre, apellido, dni);
    }

    
    public Cliente buscarClientePorCodigo(String codigo) {
        return iDaoCliente.obtenerCliente(codigo); 
    }
}