
package panoptico.clases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Representante implements Comparable<Representante> {
    
    // Atributos

    private String nombre_usuario; // Es el nombre de usuario que tiene el representante en el sistema
    private String skill; // Es la segmentacion de la atencion del representante, es decir el tipo de usuario al que brinda una atencion
    private int tiempo_preparacion; // Es el tiempo que necesita el representante para terminar los chats que tiene activos y salir a su descanso
    private ArrayList<Date> dias_trabajo; // Arreglo dinamico con todas las fechas en las que ha trabajado el representante
    private ArrayList<Proceso> procesos; // Arreglo dinamico con todos los procesos que ha atendido el representante
    private boolean representativo; // Define si el representante es representativo o no
    private double[] simulacion_dia; // Son las probabilidades que existen de que se cierre un caso en un rango de tiempo definido por el usuario
    
    // Constructor

    public Representante(String nombre_usuario, String skill) {
        this.nombre_usuario = nombre_usuario;
        this.skill = skill;
        tiempo_preparacion = 0;
        dias_trabajo = new ArrayList<>();
        procesos = new ArrayList<>();
        procesos.add(new Proceso("drop"));
        representativo = false;
    }
    
    // Metodos
    
    // Agrega un caso a un proceso especifico del representante a partir de su nombre
    
    public void agregar_caso_proceso(int duracion, Date fecha, int id, String nombre_proceso){
        if(!existe_proceso(nombre_proceso)){
            procesos.add(new Proceso(nombre_proceso));
        }
        if(!dias_trabajo.contains(fecha)){
            dias_trabajo.add(fecha);
        }
        for (int i = 0; i < procesos.size(); i++) {
            if(procesos.get(i).getNombre_proceso().equals(nombre_proceso)){
                procesos.get(i).agregar_caso(duracion, fecha, id);
            }
        }
    }
    
    // Verifica si existe o no el proceso
    
    public boolean existe_proceso(String nombre_proceso){
        for(int i = 0; i < procesos.size(); i++){
            if(procesos.get(i).getNombre_proceso().equals(nombre_proceso)){
                return true;
            }
        }
        return false;
    }
    
    // Elimina los datos del representante
    
    public void eliminar_datos_representante(){
        tiempo_preparacion = 0;
        dias_trabajo.clear();
        procesos.clear();
        representativo = false;
        simulacion_dia = new double[0];
    }
    
    // Organiza los procesos de mayor a menor de acuerdo a su representatividad 
    
    public void organizar_representante(int representatividad_procesos, int percentil_inferior_promedios, int percentil_superior_promedios, int percentil_tiempo_preparacion){
        procesos.removeIf(proceso -> proceso.getCasos().isEmpty());
        for (int i = 0; i < procesos.size(); i++) {
            procesos.get(i).organizar_proceso(percentil_inferior_promedios, percentil_superior_promedios, suma_todos_casos_representante());
        }
        Collections.sort(procesos);
        double suma_representatividad_procesos = 0;
        int suma_casos_representativos = 0;
        for (int i = 0; suma_representatividad_procesos <= ((double) representatividad_procesos)/100; i++) {
            procesos.get(i).setRepresentativo(true);
            suma_representatividad_procesos += procesos.get(i).getRepresentatividad_general();
            suma_casos_representativos += procesos.get(i).getCasos().size();
        }
        for (int i = 0; i < procesos.size(); i++) {
            if(procesos.get(i).isRepresentativo()){
                procesos.get(i).actualizar_proceso(suma_casos_representativos);
            }else{
                break;
            }
        }
        calcular_tiempo_preparacion_percentil(percentil_tiempo_preparacion);
    }
    
    // Devuelve la suma de todos los casos que atendio el representante
    
    public int suma_todos_casos_representante(){
        int suma = 0;
        for (int i = 0; i < procesos.size(); i++) {
            suma += procesos.get(i).getCasos().size();
        }
        return suma;
    }
    
    // Calcula un percentil tomando los tiempos promedio de respuesta diarios del representante
    
    public void calcular_tiempo_preparacion_percentil(double percentil_tiempo_preparacion){
        int[] lista_promedios = new int[dias_trabajo.size()];
        for (int i = 0; i < dias_trabajo.size(); i++) {
            lista_promedios[i] = tiempo_promedio_dia_trabajo(dias_trabajo.get(i));
        }
        Arrays.sort(lista_promedios);
        double posicion_percentil = lista_promedios.length * (percentil_tiempo_preparacion / 100);
        if((posicion_percentil % 1) != 0){
            tiempo_preparacion = lista_promedios[(int) posicion_percentil];
        }else{
            tiempo_preparacion = (lista_promedios[(int) posicion_percentil] + lista_promedios[(int) posicion_percentil-1]) / 2;
        }
    }
    
    // Calcula el tiempo promedio de un dia de trabajo
    
    public int tiempo_promedio_dia_trabajo(Date fecha){
        int suma = 0;
        int numero_casos = 0;
        for(int i = 0; i < procesos.size(); i++){
            suma += procesos.get(i).suma_tiempos_o_numero_casos_fecha(fecha, true);
            numero_casos += procesos.get(i).suma_tiempos_o_numero_casos_fecha(fecha, false);
        }
        return suma / numero_casos;
    }
    
    // Define un procentaje de probabilidad de que se cierre un caso en rangos definidos dentro de la jornada
    
    public void simular_dia_representante(int rango_minutos){
        simulacion_dia = new double[320/rango_minutos];
        int[] simulacion_casos_dia = new int[cantidad_casos_simulacion_casos_dia()];
        int suma_tiempos_simulacion = 0;
        double representatividad_dia = 0;
        int cantidad_casos = 0;
        for (int i = 0; i < procesos.size(); i++) {
            if(procesos.get(i).isCotidiano()){
                while(representatividad_dia <= procesos.get(i).getRepresentatividad_general()){
                    simulacion_casos_dia[cantidad_casos] = procesos.get(i).getTiempo_promedio_respuesta_entre_percentiles();
                    suma_tiempos_simulacion += procesos.get(i).getTiempo_promedio_respuesta_entre_percentiles();
                    representatividad_dia += procesos.get(i).getRepresentatividad_dia();
                    cantidad_casos ++;
                }
                representatividad_dia = 0;
            }else if(!procesos.get(i).isRepresentativo()){
                break;
            }
        }
        if(suma_tiempos_simulacion < 19200){
            simulacion_casos_dia[cantidad_casos] = 19200 - suma_tiempos_simulacion;
            cantidad_casos ++;
        }
        Combinatoria matriz_suma_simulacion = new Combinatoria(simulacion_casos_dia.length, simulacion_casos_dia);
        double probabilidad = 0;
        for (int[] simulacion : matriz_suma_simulacion.getMatriz_suma_indices()) {
            for (int suma_combinacion_sin_repeticion : simulacion) {
                probabilidad = 1.0 / simulacion.length;
                calcular_probabilidad_salida_rango(suma_combinacion_sin_repeticion, probabilidad);
            }
        }
    }
    
    // Devuelve el numero de casos que que se simulan en un dia
    
    public int cantidad_casos_simulacion_casos_dia(){
        int suma_tiempos_simulacion = 0;
        double representatividad_dia = 0;
        int cantidad_casos = 0;
        for (int i = 0; i < procesos.size(); i++) {
            if(procesos.get(i).isCotidiano()){
                while(representatividad_dia <= procesos.get(i).getRepresentatividad_general()){
                    suma_tiempos_simulacion += procesos.get(i).getTiempo_promedio_respuesta_entre_percentiles();
                    representatividad_dia += procesos.get(i).getRepresentatividad_dia();
                    cantidad_casos ++;
                }
                representatividad_dia = 0;
            }else if(!procesos.get(i).isRepresentativo()){
                break;
            }
        }
        if(suma_tiempos_simulacion < 19200) cantidad_casos ++;
        return cantidad_casos;
    }
    
    // Asigna la probabilidad al rango de la jornada al que pertence el cierre de un caso
    
    public void calcular_probabilidad_salida_rango(int suma_combinacion_sin_repeticion, double probabilidad){
        int rango = (320 / simulacion_dia.length)*60;
        for (int i = 0; i < simulacion_dia.length; i++) {
            if((suma_combinacion_sin_repeticion > rango*i) && (suma_combinacion_sin_repeticion <= rango*(i+1))){
                simulacion_dia[i] += probabilidad;
                break;
            }
        }
    }
    
    // Getters and Setters

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(int tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public ArrayList<Date> getDias_trabajo() {
        return dias_trabajo;
    }

    public void setDias_trabajo(ArrayList<Date> dias_trabajo) {
        this.dias_trabajo = dias_trabajo;
    }

    public ArrayList<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(ArrayList<Proceso> procesos) {
        this.procesos = procesos;
    }

    public boolean isRepresentativo() {
        return representativo;
    }

    public void setRepresentativo(boolean representativo) {
        this.representativo = representativo;
    }

    public double[] getSimulacion_dia() {
        return simulacion_dia;
    }

    public void setSimulacion_dia(double[] simulacion_dia) {
        this.simulacion_dia = simulacion_dia;
    }
    

    @Override
    public int compareTo(Representante representante) {
        return nombre_usuario.compareTo(representante.getNombre_usuario());
    }

}
