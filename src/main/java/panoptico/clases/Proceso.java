
package panoptico.clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Proceso implements Comparable<Proceso>{
    
    // Atributos

    private String nombre_proceso; // Nombre proceso
    private ArrayList<Caso> casos; // Arreglo dinamico con todos los casos de ese proceso
    private int tiempo_promedio_respuesta_entre_percentiles; // El el tiempo promedio de respuesta del proceso calculado entre dos percentiles
    private double representatividad_dia; // Es la representatividad que tiene el tiempo promedio de respuesta en un dia de 320 minutos
    private double representatividad_general; //Es la representatividad del proceso con respecto a todos los procesos
    private boolean representativo; // Define si el proceso es representativo o no
    private boolean cotidiano; // Define si el proceso es cotidiano o no
    
    // Constructor

    public Proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
        casos = new ArrayList<>();
        tiempo_promedio_respuesta_entre_percentiles = 0;
        representatividad_dia = 0;
        representatividad_general = 0;
        representativo = false;
        cotidiano = false;
    }
    
    // Metodos
    
    // Agrega un caso al proceso
    
    public void agregar_caso(int duracion, Date fecha, int id){
        casos.add(new Caso(duracion, fecha, id));
    }
    
    // Organiza los datos del proceso y calcula datos importantes sobre el mismo
    
    public void organizar_proceso(int percentil_inferior, int percentil_superior, int suma_total_todos_casos){
        Collections.sort(casos);
        calcular_tiempo_promedio_proceso_rango(percentil_inferior, percentil_superior);
        calcular_representatividad_dia();
        calcular_representatividad_general(suma_total_todos_casos);
    }
    
    // Actualiza los valores importantes del proceso
    
    public void actualizar_proceso(int suma_total_todos_casos){
        calcular_representatividad_general(suma_total_todos_casos);
        if(representatividad_dia <= representatividad_general) cotidiano = true;
    }
    
    // Calcula el tiempo promedio de respuesta en el rango definido por dos percentiles
    
    public void calcular_tiempo_promedio_proceso_rango(int percentil_inferior, int percentil_superior){
        int suma = 0;
        int numero_casos = 0;
        for (int i = 0; i < casos.size(); i++) {
            if((casos.get(i).getDuracion() >= calcular_percentil(percentil_inferior)) && (casos.get(i).getDuracion() <= calcular_percentil(percentil_superior))){
                suma += casos.get(i).getDuracion();
                numero_casos ++;
            }
        }
        tiempo_promedio_respuesta_entre_percentiles = suma / numero_casos;
    }
    
    // Calcula un percentil 
    
    public double calcular_percentil(double percentil){
        double posicion_percentil = casos.size() * (percentil / 100);
        if((posicion_percentil % 1) != 0){
            posicion_percentil -= posicion_percentil % 1;
            return casos.get((int) posicion_percentil).getDuracion();
        }else{
            return ((double) (casos.get((int) posicion_percentil).getDuracion() + casos.get((int) posicion_percentil-1).getDuracion())) / 2;
        }
    }
    
    // Calcula la representatividad que tendria un caso en la jornada de trabajo
    
    public void calcular_representatividad_dia(){
        representatividad_dia = ((double) tiempo_promedio_respuesta_entre_percentiles)/19200;
    }
    
    // Calcula la representatividad del proceso con respecto a los otros
    
    public void calcular_representatividad_general(int suma_total_todos_casos){
        representatividad_general = ((double) casos.size())/ suma_total_todos_casos;
    }
    
    // Devuelve la suma de todos los tiempos en una fecha especifica o el numero de casos que se atendieron
    
    public int suma_tiempos_o_numero_casos_fecha(Date fecha, boolean truesuma_falsecasos){
        if(existe_caso_fecha(fecha)){
            int suma = 0;
            int numero_casos = 0;
            for (int i = 0; i < casos.size(); i++) {
                if(casos.get(i).getFecha().compareTo(fecha) == 0){
                    suma += casos.get(i).getDuracion();
                    numero_casos ++;
                }
            }
            if(truesuma_falsecasos){
                return suma;
            }else{
                return numero_casos;
            }
        }else{
            return 0;
        }
    }
    
    // Verifica si existen casos en el proceso correspondientes a una fecha
    
    public boolean existe_caso_fecha(Date fecha) {
        for (int i = 0; i < casos.size(); i++) {
            if (casos.get(i).getFecha().compareTo(fecha) == 0) return true;
        }
        return false;
    }
    
    // Getters and Setters

    public String getNombre_proceso() {
        return nombre_proceso;
    }

    public void setNombre_proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
    }

    public ArrayList<Caso> getCasos() {
        return casos;
    }

    public void setCasos(ArrayList<Caso> casos) {
        this.casos = casos;
    }

    public int getTiempo_promedio_respuesta_entre_percentiles() {
        return tiempo_promedio_respuesta_entre_percentiles;
    }

    public void setTiempo_promedio_respuesta_entre_percentiles(int tiempo_promedio_respuesta_entre_percentiles) {
        this.tiempo_promedio_respuesta_entre_percentiles = tiempo_promedio_respuesta_entre_percentiles;
    }

    public double getRepresentatividad_dia() {
        return representatividad_dia;
    }

    public void setRepresentatividad_dia(double representatividad_dia) {
        this.representatividad_dia = representatividad_dia;
    }

    public double getRepresentatividad_general() {
        return representatividad_general;
    }

    public void setRepresentatividad_general(double representatividad_general) {
        this.representatividad_general = representatividad_general;
    }

    public boolean isRepresentativo() {
        return representativo;
    }

    public void setRepresentativo(boolean representativo) {
        this.representativo = representativo;
    }

    public boolean isCotidiano() {
        return cotidiano;
    }

    public void setCotidiano(boolean cotidiano) {
        this.cotidiano = cotidiano;
    }

    @Override
    public int compareTo(Proceso proceso) {
        if(representatividad_general > proceso.getRepresentatividad_general()){
            return -1;
        }else if(representatividad_general < proceso.getRepresentatividad_general()){
            return 1;
        }else{
            return 0;
        }
    }

}
