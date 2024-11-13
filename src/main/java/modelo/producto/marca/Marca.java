package modelo.producto.marca;
import java.util.List;

/**
 *
 * @author 54346
 */

public class Marca {

    private String marca;
    private List<Modelo> modelos;

    public Marca(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<Modelo> getModelos() {
        return modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }
}