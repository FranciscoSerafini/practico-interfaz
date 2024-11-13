package modelo.pedido;

import java.util.Date;
import java.util.List;
import modelo.cliente.Cliente;
import modelo.vendedor.Vendedor;

/**
 *
 * @author 54346
 */
public class Pedido {
    private int idPedido;
    private Date fecha;
    private String nombreVendedor;
    private String nombreCliente;

    // Constructor
    public Pedido(int idPedido, Date fecha, String nombreVendedor, String nombreCliente) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.nombreVendedor = nombreVendedor;
        this.nombreCliente = nombreCliente;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}