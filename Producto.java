package proyectodb;

import java.math.BigDecimal;

class Producto {
	
	private String nombre;
    private String estado;
    private String origen;
    private BigDecimal precio;

    public Producto(String nombre, String estado, String origen, BigDecimal precio) {
        this.nombre = nombre;
        this.estado = estado;
        this.origen = origen;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public String getOrigen() {
        return origen;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
}


