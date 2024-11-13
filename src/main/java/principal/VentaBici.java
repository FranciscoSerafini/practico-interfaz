package principal;

import java.util.Scanner;
import modelo.cliente.Cliente;
import negocio.abm.cliente.ABMCliente;
import negocio.abm.cliente.Exception.ClienteException;
import modelo.vendedor.Vendedor;
import negocio.abm.vendedor.ABMVendedor;
import negocio.abm.vendedor.exception.VendedorException;
import modelo.proveedor.Proveedor;
import negocio.abm.proveedor.ABMProveedor;
import negocio.abm.proveedor.exception.ProveedorException;
import presentacion.JFormMenu;
import presentacion.pedido.JFormConsultaPedido;

/**
 *
 * @author 54346
 */

public class VentaBici {
    
    private static ABMCliente aBMCliente= new ABMCliente();
    private static ABMVendedor aBMVendedor= new ABMVendedor();
    private static ABMProveedor aBMProveedor= new ABMProveedor();
    
    public static void main(String[] args) {  
        menu();
    }
    
    private static void menu(){
        System.out.println("====================== MENU===========================");
        System.out.println("=================== SELECIONE UNA OPCION ==============");
        System.out.println("1 ABM CLIENTE");
        System.out.println("2 ABM VENDEDOR");
        System.out.println("3 ABM PROVEEDOR");
        System.out.println("4 SALIR");
        
        Scanner scan= new Scanner(System.in);
        
        int opt= scan.nextInt();
        
        while(opt!= 4){
            
            switch(opt){
                case 1:
                    subMenuAbmCliente();
                    break;
                    
                case 2:
                    subMenuAbmVendedor();
                    break;
                    
                case 3:
                    subMenuAbmProveedor();
                    break;
                default:
                    System.out.println("Ingrese una opcion comprendida entre 1 y 4");
                    break;
            }
        
            opt=scan.nextInt();
        }
    }
    
    // SubMenu e Items del SubMenu Clientes
    private static void subMenuAbmCliente(){
        System.out.println("=================== SUB MENU ABM CLIENTE ==============");
        System.out.println("1 ALTA CLIENTE");
        System.out.println("2 BAJA CLIENTE");
        System.out.println("3 MODIFICAR CLIENTE");
        System.out.println("4 LISTAR TODOS LOS CLIENTES");
        System.out.println("5 INTERFAZ");
        System.out.println("6 VOLVER AL MENU PRINCIPAL");


        Scanner scan = new Scanner(System.in);
        int opt = scan.nextInt();

        while(opt != 6){
            switch(opt){
                case 1:
                    try{
                        altaCliente();
                    }catch(ClienteException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try{
                        bajaCliente();
                    }catch(ClienteException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try{
                        modificarCliente();
                    }catch(ClienteException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    aBMCliente.listarClientes(null, null, null, 0);
                    break;
                case 5:
                    levantarVistaMenu();
                    break;

                default:
                    System.out.println("Ingrese una opción comprendida entre 1 y 6");
                    break;
            }

            subMenuAbmCliente();
            opt = scan.nextInt();
        }

        menu();
    }
    
    private static void levantarVistaMenu(){
        
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new JFormMenu().setVisible(true);
                new JFormConsultaPedido().setVisible(true);
            }
        });
    }
    
    private static void altaCliente() throws ClienteException {
        
        Scanner scan= new Scanner(System.in);
        System.out.println("Ingrese el cuil del cliente");
        
        String cuil= scan.next();
        System.out.println("Ingrese nombre del clinete");
        
        String nombre= scan.next();
        System.out.println("Ingrese apellido del cliente");
        
        String apellido= scan.next();
        System.out.println("Ingrese el dni del cliente");
        
        int dni= scan.nextInt();
        System.out.println("Ingese telefono del cliente");
        
        String telefono = scan.next();
        System.out.println("Ingrese el email del cliente");
        
        String email= scan.next();
        
        Cliente cliente= new Cliente(cuil, nombre, apellido, dni, telefono, email);
        
        ABMCliente aBMCliente= new ABMCliente();
        
        cliente.setCodigo(aBMCliente.asignarCodigoCliente());
        
        aBMCliente.altaCliente(cliente);
    }
    
    private static void bajaCliente() throws ClienteException {
        Scanner scan = new Scanner(System.in);

        // Solicitar el código del cliente a eliminar
        System.out.println("Ingrese el código del cliente a eliminar:");
        String codigo = scan.nextLine();  // Usamos nextLine() para leer toda la línea del código

        // Buscar el cliente por el código ingresado
        Cliente cliente = aBMCliente.buscarClientePorCodigo(codigo);

        if (cliente != null) {
            // Si el cliente fue encontrado, mostrar su información
            System.out.println("Cliente encontrado: " + cliente);
            System.out.println("¿Está seguro de que desea eliminar este cliente? (S/N)");

            // Leer la confirmación del usuario
            String confirmacion = scan.nextLine();  // Usamos nextLine() para capturar la respuesta completa

            // Si el usuario confirma con "S", proceder con la eliminación
            if (confirmacion.equalsIgnoreCase("S")) {
                // Llamar al método de baja de cliente en la clase aBMCliente, pasándole el código
                aBMCliente.bajaCliente(codigo);  // Suponiendo que bajaCliente acepta un código de cliente

                System.out.println("El cliente ha sido eliminado exitosamente.");
            } else {
                // Si el usuario cancela, no se realiza ninguna acción
                System.out.println("Eliminación cancelada.");
            }
        } else {
            // Si no se encontró el cliente, mostrar un mensaje de error
            System.out.println("Cliente no encontrado.");
        }
    }

    private static void modificarCliente() throws ClienteException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese el código del cliente que desea modificar:");
        String codigo = scan.nextLine(); // Asegurarse de capturar la línea completa para el código

        // Buscar el cliente con el código proporcionado
        Cliente clienteExistente = aBMCliente.buscarClientePorCodigo(codigo);

        if (clienteExistente != null) {
            System.out.println("Cliente encontrado: " + clienteExistente);

            // Pedir el nuevo nombre, asegurándose de capturar la línea completa
            System.out.println("Ingrese el nuevo nombre del cliente (deje vacío si no desea modificarlo):");
            String nuevoNombre = scan.nextLine();
            if (!nuevoNombre.isEmpty()) {
                clienteExistente.setNombre(nuevoNombre);
            }

            // Pedir el nuevo apellido
            System.out.println("Ingrese el nuevo apellido del cliente (deje vacío si no desea modificarlo):");
            String nuevoApellido = scan.nextLine();
            if (!nuevoApellido.isEmpty()) {
                clienteExistente.setApellido(nuevoApellido);
            }

            // Pedir el nuevo DNI
            System.out.println("Ingrese el nuevo dni del cliente (deje vacío si no desea modificarlo):");
            String nuevoDni = scan.nextLine();
            if (!nuevoDni.isEmpty()) {
                clienteExistente.setDni(Integer.parseInt(nuevoDni));
            }

            // Pedir el nuevo teléfono
            System.out.println("Ingrese el nuevo teléfono del cliente (deje vacío si no desea modificarlo):");
            String nuevoTelefono = scan.nextLine();
            if (!nuevoTelefono.isEmpty()) {
                clienteExistente.setTelefono(nuevoTelefono);
            }

            // Pedir el nuevo email
            System.out.println("Ingrese el nuevo email del cliente (deje vacío si no desea modificarlo):");
            String nuevoEmail = scan.nextLine();
            if (!nuevoEmail.isEmpty()) {
                clienteExistente.setEmail(nuevoEmail);
            }

            // Pedir el nuevo CUIL (solo si se desea modificar)
            System.out.println("Ingrese el nuevo CUIL del cliente (deje vacío si no desea modificarlo):");
            String nuevoCuil = scan.nextLine();
            if (!nuevoCuil.isEmpty()) {
                clienteExistente.setCuil(nuevoCuil); // Aquí se actualiza el CUIL
            }

            // Realiza la actualización del cliente
            try {
                aBMCliente.modificarDatosCliente(codigo, clienteExistente);
                System.out.println("Cliente modificado con éxito.");
            } catch (ClienteException e) {
                System.out.println("Error al modificar cliente: " + e.getMessage());
            }
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }
    
    // SubMenu e Items del SubMenu Vendedores
    private static void subMenuAbmVendedor(){
        System.out.println("=================== SUB MENU ABM VENDEDOR ==============");
        System.out.println("1 ALTA VENDEDOR");
        System.out.println("2 BAJA VENDEDOR");
        System.out.println("3 MODIFICAR VENDEDOR");
        System.out.println("4 LISTAR TODOS LOS VENDEDORES");
        System.out.println("6 VOLVER AL MENU PRINCIPAL");

        Scanner scan = new Scanner(System.in);
        int opt = scan.nextInt();

        while(opt != 6){
            switch(opt){
                case 1:
                    try{
                        altaVendedor();
                    }catch(VendedorException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try{
                        bajaVendedor();
                    }catch(VendedorException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        modificarVendedor();
                    } catch (VendedorException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    aBMVendedor.listarVendedores(null, null, null, 0);
                    break;
                default:
                    System.out.println("Ingrese una opción comprendida entre 1 y 6");
                    break;
            }

            subMenuAbmVendedor();
            opt = scan.nextInt();
        }

        menu();
    }

    private static void altaVendedor() throws VendedorException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Ingrese el cuit del vendedor");  // Ahora solicitamos el cuit
        String cuit = scan.next();  // Solicitamos el cuit del vendedor

        System.out.println("Ingrese la sucursal del vendedor");
        String sucursal = scan.next();

        System.out.println("Ingrese nombre del vendedor");
        String nombre = scan.next();

        System.out.println("Ingrese apellido del vendedor");
        String apellido = scan.next();

        System.out.println("Ingrese el dni del vendedor");
        int dni = scan.nextInt();

        System.out.println("Ingrese el teléfono del vendedor");
        String telefono = scan.next();

        System.out.println("Ingrese el email del vendedor");
        String email = scan.next();

        // Creamos el vendedor con el cuit
        Vendedor vendedor = new Vendedor(cuit, sucursal, nombre, apellido, dni, telefono, email);

        // Aquí no es necesario asignar el código manualmente, ya que se genera al alta
        ABMVendedor aBMVendedor = new ABMVendedor();
        aBMVendedor.altaVendedor(vendedor);  // Damos de alta al vendedor
    }

    private static void bajaVendedor() throws VendedorException {
        Scanner scan = new Scanner(System.in);

        // Solicitar el código del vendedor a eliminar
        System.out.println("Ingrese el código del vendedor a eliminar:");
        String codigo = scan.nextLine();  // Usamos nextLine() para leer toda la línea del código

        // Buscar el vendedor por el código ingresado
        Vendedor vendedor = aBMVendedor.buscarVendedorPorCodigo(codigo);

        if (vendedor != null) {
            // Si el vendedor fue encontrado, mostrar su información
            System.out.println("Vendedor encontrado: " + vendedor);
            System.out.println("¿Está seguro de que desea eliminar este vendedor? (S/N)");

            // Leer la confirmación del usuario
            String confirmacion = scan.nextLine();  // Usamos nextLine() para capturar la respuesta completa

            // Si el usuario confirma con "S", proceder con la eliminación
            if (confirmacion.equalsIgnoreCase("S")) {
                // Llamar al método de baja de vendedor en la clase aBMVendedor, pasándole el código
                aBMVendedor.bajaVendedor(codigo);  // Suponiendo que bajaVendedor acepta un código de vendedor

                System.out.println("El vendedor ha sido eliminado exitosamente.");
            } else {
                // Si el usuario cancela, no se realiza ninguna acción
                System.out.println("Eliminación cancelada.");
            }
        } else {
            // Si no se encontró el vendedor, mostrar un mensaje de error
            System.out.println("Vendedor no encontrado.");
        }
    }

    private static void modificarVendedor() throws VendedorException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese el código del vendedor que desea modificar:");
        String codigo = scan.nextLine(); // Asegurarse de capturar la línea completa para el código

        // Buscar el vendedor con el código proporcionado
        Vendedor vendedorExistente = aBMVendedor.buscarVendedorPorCodigo(codigo);

        if (vendedorExistente != null) {
            System.out.println("Vendedor encontrado: " + vendedorExistente);

            // Pedir el nuevo nombre, asegurándose de capturar la línea completa
            System.out.println("Ingrese el nuevo nombre del vendedor (deje vacío si no desea modificarlo):");
            String nuevoNombre = scan.nextLine();
            if (!nuevoNombre.isEmpty()) {
                vendedorExistente.setNombre(nuevoNombre);
            }

            // Pedir el nuevo apellido
            System.out.println("Ingrese el nuevo apellido del vendedor (deje vacío si no desea modificarlo):");
            String nuevoApellido = scan.nextLine();
            if (!nuevoApellido.isEmpty()) {
                vendedorExistente.setApellido(nuevoApellido);
            }

            // Pedir el nuevo DNI
            System.out.println("Ingrese el nuevo dni del vendedor (deje vacío si no desea modificarlo):");
            String nuevoDni = scan.nextLine();
            if (!nuevoDni.isEmpty()) {
                vendedorExistente.setDni(Integer.parseInt(nuevoDni));
            }

            // Pedir el nuevo teléfono
            System.out.println("Ingrese el nuevo teléfono del vendedor (deje vacío si no desea modificarlo):");
            String nuevoTelefono = scan.nextLine();
            if (!nuevoTelefono.isEmpty()) {
                vendedorExistente.setTelefono(nuevoTelefono);
            }

            // Pedir el nuevo email
            System.out.println("Ingrese el nuevo email del vendedor (deje vacío si no desea modificarlo):");
            String nuevoEmail = scan.nextLine();
            if (!nuevoEmail.isEmpty()) {
                vendedorExistente.setEmail(nuevoEmail);
            }

            // Realiza la actualización del vendedor
            try {
                aBMVendedor.modificarDatosVendedor(codigo, vendedorExistente);
                System.out.println("Vendedor modificado con éxito.");
            } catch (VendedorException e) {
                System.out.println("Error al modificar vendedor: " + e.getMessage());
            }
        } else {
            System.out.println("Vendedor no encontrado.");
        }
    }   
    
    // SubMenu e Items del SubMenu Proveedores
    private static void subMenuAbmProveedor() {
        System.out.println("=================== SUB MENU ABM PROVEEDOR ==============");
        System.out.println("1 ALTA PROVEEDOR");
        System.out.println("2 BAJA PROVEEDOR");
        System.out.println("3 MODIFICAR PROVEEDOR");
        System.out.println("4 LISTAR TODOS LOS PROVEEDORES");
        System.out.println("5 LISTAR PROVEEDOR POR FILTRO");
        System.out.println("6 VOLVER AL MENU PRINCIPAL");

        Scanner scan = new Scanner(System.in);
        int opt = scan.nextInt();

        while (opt != 6) {
            switch (opt) {
                case 1:
                    try {
                        altaProveedor();
                    } catch (ProveedorException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        bajaProveedor();
                    } catch (ProveedorException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        modificarProveedor();
                    } catch (ProveedorException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    aBMProveedor.listarProveedor(null, null, null, 0);
                    break;

                case 5:
                    buscarProveedorPorFiltro();
                    break;

                default:
                    System.out.println("Ingrese una opción comprendida entre 1 y 6");
                    break;
            }

            subMenuAbmProveedor();
            opt = scan.nextInt();
        }

        menu();
    }

    private static void altaProveedor() throws ProveedorException {
        Scanner scan = new Scanner(System.in);

        // Leer el cuit del proveedor
        System.out.println("Ingrese el cuit del proveedor");
        String cuit = scan.next();

        // Leer el nombre del proveedor
        System.out.println("Ingrese el nombre del proveedor");
        String nombre = scan.next();

        // Leer el teléfono del proveedor
        System.out.println("Ingrese el teléfono del proveedor");
        String telefono = scan.next();

        // Leer el email del proveedor
        System.out.println("Ingrese el email del proveedor");
        String email = scan.next();

        // Leer el nombre de fantasía del proveedor
        System.out.println("Ingrese el nombre de fantasía del proveedor");
        String nombreFantasia = scan.next();

        // Leer el apellido y DNI del proveedor (campos para Persona)
        System.out.println("Ingrese el apellido del proveedor");
        String apellido = scan.next();

        System.out.println("Ingrese el DNI del proveedor");
        int dni = scan.nextInt();

        // Crear proveedor utilizando el constructor adecuado
        Proveedor proveedor = new Proveedor(cuit, nombreFantasia, nombre, apellido, dni, telefono, email);

        // Llamada al método ABMProveedor para registrar el proveedor
        ABMProveedor aBMProveedor = new ABMProveedor();
        aBMProveedor.altaProveedor(proveedor);
    }

    private static void bajaProveedor() throws ProveedorException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese el código del proveedor a eliminar:");
        String codigo = scan.next();

        Proveedor proveedor = aBMProveedor.buscarProveedorPorCodigo(codigo);

        if (proveedor != null) {
            System.out.println("Proveedor encontrado: " + proveedor);
            System.out.println("¿Está seguro de que desea eliminar este proveedor? (S/N)");
            String confirmacion = scan.next();

            if (confirmacion.equalsIgnoreCase("S")) {
                aBMProveedor.bajaProveedor(proveedor);
            } else {
                System.out.println("Eliminación cancelada.");
            }
        } else {
            System.out.println("Proveedor no encontrado.");
        }
    }

    private static void modificarProveedor() throws ProveedorException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese el código del proveedor que desea modificar:");
        String codigo = scan.next();

        Proveedor proveedorExistente = aBMProveedor.buscarProveedorPorCodigo(codigo);

        if (proveedorExistente != null) {
            System.out.println("Proveedor encontrado: " + proveedorExistente);

            System.out.println("Ingrese el nuevo nombre del proveedor (deje vacío si no desea modificarlo):");
            String nuevoNombre = scan.nextLine();
            if (!nuevoNombre.isEmpty()) {
                proveedorExistente.setNombre(nuevoNombre);
            }

            System.out.println("Ingrese el nuevo teléfono del proveedor (deje vacío si no desea modificarlo):");
            String nuevoTelefono = scan.nextLine();
            if (!nuevoTelefono.isEmpty()) {
                proveedorExistente.setTelefono(nuevoTelefono);
            }

            System.out.println("Ingrese el nuevo email del proveedor (deje vacío si no desea modificarlo):");
            String nuevoEmail = scan.nextLine();
            if (!nuevoEmail.isEmpty()) {
                proveedorExistente.setEmail(nuevoEmail);
            }

            try {
                aBMProveedor.modificarDatosProveedor(codigo, proveedorExistente);
            } catch (ProveedorException e) {
                System.out.println("Error al modificar proveedor: " + e.getMessage());
            }
        } else {
            System.out.println("Proveedor no encontrado.");
        }
    }

    private static void buscarProveedorPorFiltro() {
        Scanner scan = new Scanner(System.in);

        // Leer los filtros de búsqueda del proveedor
        System.out.println("Ingrese el código del proveedor (deje vacío para no filtrar):");
        String codigo = scan.nextLine();

        System.out.println("Ingrese el nombre del proveedor (deje vacío para no filtrar):");
        String nombre = scan.nextLine();

        System.out.println("Ingrese el cuit del proveedor (deje vacío para no filtrar):");
        String cuit = scan.nextLine();

        // Para los campos tipo int, usa Integer en lugar de int, ya que Integer puede ser null
        System.out.println("Ingrese el DNI del proveedor (deje vacío para no filtrar):");
        String dniInput = scan.nextLine();
        Integer dni = (dniInput.isEmpty()) ? null : Integer.parseInt(dniInput);

        // Pasar los filtros a la función listarProveedor
        aBMProveedor.listarProveedor(
                (codigo.isEmpty() ? null : codigo),
                (nombre.isEmpty() ? null : nombre),
                (cuit.isEmpty() ? null : cuit),
                dni // Pasar el DNI como Integer (puede ser null)
        );
    }
}