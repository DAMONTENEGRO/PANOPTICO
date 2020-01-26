
package proyecto.panoptico.clases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author montenegro
 */
public class Caso implements Comparable<Caso> {
    
    // Atributos
    
    private String skill; // Segmentacion del usuario atendido
    private String nombre_usuario; // Nombre de usuario del representante
    private String proceso; // Tema sobre el cual trato la consulta
    private Date fecha; // Fecha en la que se atendio la consulta
    private int id; // Numero identificador del caso
    private double duracion; // Tiempo en segundos que duro el caso 
    private String tl; // Lider asignado a ese representante
    private String turno; // Turno al que pertenece el representante

    // Constructor
    
    public Caso(String[] fila, int tiempo_minimo) throws ParseException {
        skill = "none";
        nombre_usuario = "none";
        proceso = "none";
        fecha = null;
        id = 0;
        duracion = 0;
        tl = "none";
        turno = "none";
        for (int i = 0; i < fila.length; i++) {
            switch (i) {
                case 0:
                    skill = fila[0].toLowerCase();
                    break;
                case 1:
                    nombre_usuario = fila[1].toLowerCase();
                    break;
                case 2:
                    proceso = fila[2].toLowerCase(); 
                    break;
                case 3:
                    if (fila[3].length() > 0) proceso = "drop";
                    break;
                case 4:
                    fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fila[4]);  
                    break;
                case 5:
                    id = (int) Integer.parseInt(fila[5]);
                    break;
                case 6:
                    duracion = Double.parseDouble(fila[6]);;
                    break;
                case 7:
                    tl = fila[7].toLowerCase();
                    break;
                case 8:
                    turno = fila[8].toLowerCase();
                    break;
            }
        }
        if((duracion < tiempo_minimo) || (proceso.equals("none"))) proceso = "drop";
    }

    // Metodos
    
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
