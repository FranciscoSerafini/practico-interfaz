package modelo.vendedor;

import modelo.persona.Persona;

/**
 *
 * @author 54346
 */

public class Vendedor extends Persona {

    private String codigo;
    private String cuit;
    private String sucursal;
    
    public Vendedor() {
        super();
    }

    public Vendedor(String cuit) {
        this.cuit = cuit;
    }

    public Vendedor(String cuit, String sucursal, String nombre, String apellido, int dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email);  // Llama al constructor de la clase Persona
        this.cuit = cuit;
        this.sucursal = sucursal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public String toString() {
        return super.toString() + " CODIGO: " + this.codigo + " SUCURSAL: " + this.sucursal + " CUIT: " + this.cuit;
    }
}