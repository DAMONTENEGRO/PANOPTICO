
package panoptico.clases;

import java.util.Date;

/**
 * @author montenegro
 */
public class Caso {
    
    // Atributos
    
    private int duracion; // Duracion en segundos del caso
    private Date fecha; // Fecha en la que se dio respuesta a ese caso
    private int id; // ID del caso
    
    // Constructor

    public Caso(int duracion, Date fecha, int id) {
        this.duracion = duracion;
        this.fecha = fecha;
        this.id = id;
    }
    
    // Getters and Setters

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
