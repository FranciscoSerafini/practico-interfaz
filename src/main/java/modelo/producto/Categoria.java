package modelo.producto;

/**
 *
 * @author 54346
 */

public class Categoria {

    private String categoria;
    private TipoCategoria tipo;
    
    public Categoria(String categoria, TipoCategoria tipo) {
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }
}