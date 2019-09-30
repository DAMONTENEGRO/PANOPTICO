
package panoptico.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Proceso {
    
    // Atributos

    private String nombre_proceso; // Nombre proceso
    private List<Caso> casos; // Lista dinamica de todoso los casos de ese proceso
    
    // Constructor

    public Proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
        casos = new ArrayList<>();
    }
    
    // Metodos
    
    // Agrega un caso al proceso
    
    public void agregar_caso(int duracion, Date fecha, int id){
        casos.add(new Caso(duracion, fecha, id));
    }
    
    // Calcula el tiempo promedio de respuesta del proceso
    
    public double tiempo_promedio_proceso(){
        double suma = 0;
        for (int i = 0; i < casos.size(); i++) {
            suma += casos.get(i).getDuracion();
        }
        return suma / casos.size();
    }
    
    // Calcula el tiempro promedio de respuesta en el rango definido por dos percentiles
    
    public double tiempo_promedio_proceso_rango(double percentil_inferior, double percentil_superior){
        double suma = 0;
        int numero_casos = 0;
        for (int i = 0; i < casos.size(); i++) {
            if((casos.get(i).getDuracion() >= calcular_percentil(percentil_inferior)) && (casos.get(i).getDuracion() <= calcular_percentil(percentil_superior))){
                suma += casos.get(i).getDuracion();
                numero_casos ++;
            }
        }
        return suma / numero_casos;
    }
    
    // Devuelve la suma de todos los tiempos es una fecha especifica o el numero de casos que se atendieron
    
    public double suma_o_numero_casos_fecha(Date fecha, boolean tsuma_fcasos){
        if(existe_fecha(fecha)){
            double suma = 0;
            int numero_casos = 0;
            for (int i = 0; i < casos.size(); i++) {
                if(casos.get(i).getFecha().compareTo(fecha) == 0){
                    suma += casos.get(i).getDuracion();
                    numero_casos ++;
                }
            }
            if(tsuma_fcasos){
                return suma;
            }else{
                return numero_casos;
            }
        }else{
            return 0;
        }
    }
    
    // Organiza los casos del proceso de menor a mayor
    
    public void organizar_casos(){
        List<Caso> copia_lista_casos = new ArrayList<>();
        for (int i = 0; i < casos.size(); i++) {
            copia_lista_casos.add(casos.get(i));
        }
        int numero_procesos = casos.size();
        casos.clear();
        for (int i = 0; i < numero_procesos; i++) {
            casos.add(copia_lista_casos.get(indice_menor_caso(copia_lista_casos)));
            copia_lista_casos.remove(indice_menor_caso(copia_lista_casos));
        }
    }
    
    // Devuelve el indice del caso con menor duracion de una lista de casos
     
    public int indice_menor_caso(List<Caso> lista_casos_reducida){
        int indice_menor = 0;
        for (int i = 1; i < lista_casos_reducida.size(); i++) {
            if(lista_casos_reducida.get(indice_menor).getDuracion() > lista_casos_reducida.get(i).getDuracion()){
                indice_menor = i;
            }
        }
        return indice_menor;
    }
    
    // Calcula un percentil 
    
    public double calcular_percentil(double percentil){
        double posicion_percentil = casos.size() * (percentil / 100);
        if((posicion_percentil % 1) != 0){
            posicion_percentil -= posicion_percentil % 1;
            return casos.get((int) posicion_percentil).getDuracion();
        }else{
            return ((double) casos.get((int) posicion_percentil).getDuracion() + (double) casos.get((int) posicion_percentil-1).getDuracion()) / 2;
        }
    }
    
    // Verifica si existen casos en el proceso correspondientes a una fecha
    
    public boolean existe_fecha(Date fecha) {
        for (int i = 0; i < casos.size(); i++) {
            if (casos.get(i).getFecha().compareTo(fecha) == 0) {
                return true;
            }
        }
        return false;
    }
    
    // Codigo de referencia para pruebas del proyecto "HORUS"
    
    /*
    // Calcula la representatividad dia
    
    public void calcular_representatividad_dia(){
        representatividad_dia = tiempo_promedio_conversacion_casos_sin_drop()/19200;
    }
    
    // Calcula la representatividad general
    
    public void calcular_representatividad_general(double cantidad_total_casos){
        representatividad_general =  cantidad_casos_sin_drop()/ cantidad_total_casos;
    }
    
    // Agrega un caso
    
    public void agregar_caso(int duracion, boolean drop, Date fecha){
        casos.add(new Caso(duracion, drop, fecha));
    }
    
    // Calcula el tiempo promedio de conversacion del proceso
    
    public double tiempo_promedio_conversacion_casos_sin_drop(){
        try{
            return suma_tiempos_sin_drop()/cantidad_casos_sin_drop();
        }catch(Exception e){
            return 0;
        }
    }
    
    // Calcula el tiempo promedio de conversacion de los casos con drop
    
    public double tiempo_promedio_conversacion_casos_con_drop(){
        try{
            return suma_tiempos_con_drop()/cantidad_casos_con_drop();
        }catch(Exception e){
            return 0;
        }
    }
    
    // Suma todos los tiempos del proceso sin drop

    public int suma_tiempos_sin_drop(){
        int suma = 0;
        for (int i = 0; i < casos.size(); i++) {
            if(!casos.get(i).getDrop()){
                suma += casos.get(i).getDuracion();
            }
        }
        return suma;
    }
    
    // Suma todos los tiempos del proceso sin drop

    public int suma_tiempos_con_drop(){
        int suma = 0;
        for (int i = 0; i < casos.size(); i++) {
            if(casos.get(i).getDrop()){
                suma += casos.get(i).getDuracion();
            }
        }
        return suma;
    }
    
    // Retorna el numero de casos sin drop
    
    public int cantidad_casos_sin_drop(){
        int suma = 0;
        for (int i = 0; i < casos.size(); i++) {
            if(!casos.get(i).getDrop()){
                suma ++;
            }
        }
        return suma;
    }
    
    // Retorna el numero de casos con drop
    
    public int cantidad_casos_con_drop(){
        int suma = 0;
        for (int i = 0; i < casos.size(); i++) {
            if(casos.get(i).getDrop()){
                suma ++;
            }
        }
        return suma;
    }
    
    // Calcula los tiempos maximos y minimos

    public int duracion_minima_casos(){
        int minimo = 0;
        if (!casos.isEmpty()) {
            minimo = casos.get(primera_posicion_sin_drop()).getDuracion();
            for (int i = primera_posicion_sin_drop()+1; i < casos.size(); i++) {
                if (!casos.get(i).getDrop()) {
                    if (minimo > casos.get(i).getDuracion()) {
                        minimo = casos.get(i).getDuracion();
                    }
                }
            }
        }
        return minimo;
    }
    
    public int duracion_maxima_casos(){
        int maximo = 0;
        if (!casos.isEmpty()) {
            maximo = casos.get(primera_posicion_sin_drop()).getDuracion();
            for (int i = primera_posicion_sin_drop()+1; i < casos.size(); i++) {
                if (!casos.get(i).getDrop()) {
                    if (maximo < casos.get(i).getDuracion()) {
                        maximo = casos.get(i).getDuracion();
                    }
                }
            }
        }
        return maximo;
    }
    
    // Retorna el indice de la posicion del primer caso sin drop
    
    public int primera_posicion_sin_drop(){
        int posicion = 0;
        for (int i = 0; i < casos.size(); i++) {
            if(!casos.get(i).getDrop()){
                return i;
            }
        }
        return posicion;
    }
    */
    
    // Getters and Setters

    public String getNombre_proceso() {
        return nombre_proceso;
    }

    public void setNombre_proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
    }

    public List<Caso> getCasos() {
        return casos;
    }

    public void setCasos(List<Caso> casos) {
        this.casos = casos;
    }
    
}
