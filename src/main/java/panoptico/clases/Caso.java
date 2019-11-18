
package panoptico.clases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author montenegro
 */
public class Caso implements Comparable<Caso> {

    // Atributos
    
    private String skill; // Segmentacion del usuario atendido
    private String nombre_usuario; // Nombre de usuario del representante
    private String proceso; // Tema sobre el cual trato la consulta
    private Date fecha; // Fecha en la que se atendio la consulta
    private double id; // Numero identificador del caso
    private double duracion; // Tiempo en segundos que duro el caso 
    private String tl; // Lider asignado a ese representante
    private String turno; // Turno al que pertenece el representante

    // Constructor
    
    public Caso(Row fila, int tiempo_minimo) throws FileNotFoundException, IOException {
        skill = "none";
        nombre_usuario = "none";
        proceso = "none";
        fecha = null;
        id = 0;
        duracion = 0;
        tl = "none";
        turno = "none";
        Iterator<Cell> iterador_celdas = fila.cellIterator();
        while (iterador_celdas.hasNext()) {
            Cell siguiente_celda = iterador_celdas.next();
            switch (siguiente_celda.getColumnIndex()) {
                case 0:
                    skill = (String) leer_celda(siguiente_celda);
                    break;
                case 1:
                    nombre_usuario = (String) leer_celda(siguiente_celda);
                    break;
                case 2:
                    proceso = (String) leer_celda(siguiente_celda);
                    break;
                case 3:
                    proceso = "drop";
                    break;
                case 4:
                    fecha = (Date) leer_celda(siguiente_celda);
                    break;
                case 5:
                    id = (double) leer_celda(siguiente_celda);
                    break;
                case 6:
                    duracion = (double) leer_celda(siguiente_celda);
                    break;
                case 7:
                    tl = (String) leer_celda(siguiente_celda);
                    break;
                case 8:
                    turno = (String) leer_celda(siguiente_celda);
                    break;
            }
        }
        if((duracion < tiempo_minimo) || (proceso.equals("none"))) proceso = "drop";
    }

    // Metodos

    // Lee una celda
    
    public Object leer_celda(Cell celda) {
        switch (celda.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return celda.getStringCellValue().toLowerCase();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) return celda.getDateCellValue();
                return celda.getNumericCellValue();
        }
        return null;
    }
    
    // Muestra el caso con todos sus atributos por consola
    
    void mostrar_caso_consola(){
        System.out.print("(Skill): " + skill + " - " + "(Nombre usuario): " + nombre_usuario + " - " + "(Proceso): " + proceso + " - " + "(Fecha): " + fecha);
        System.out.println(" - " + "(Id): " + id + " - " + "(Duracion): " + duracion + " - " + "(Lider): " + tl + " - " + "(Turno): " + turno);
    }
    
    // G&S

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
    
    @Override
    public int compareTo(Caso caso) {
        if(duracion < caso.getDuracion()){
            return -1;
        }else if(duracion > caso.getDuracion()){
            return 1;
        }else{
            return 0;
        }
    }
}
