package modelo.cliente;

import modelo.persona.Persona;

/**
 *
 * @author 54346
 */

public class Cliente extends Persona {

    private String codigo;
    private String cuil;

    public Cliente() {
        super();
    }

    public Cliente(String cuil) {
        this.cuil = cuil;
    }

    public Cliente(String cuil, String nombre, String apellido, int dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email);
        this.cuil = cuil;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    @Override
    public String toString() {
        return super.toString() + " CODIGO: " + this.codigo + " CUIL: " + this.cuil;
    }
}