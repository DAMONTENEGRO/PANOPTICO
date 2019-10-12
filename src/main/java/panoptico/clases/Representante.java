
package panoptico.clases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Representante {
    
    // Atributos

    private String nombre_usuario; // Es el nombre de usuario que tiene el representante en el sistema
    private String skill; // Es la segmentacion de la atencion del representante, es decir el tipo de usuario al que brinda una atencion
    private double tiempo_preparacion; // Es el tiempo que necesita el representante para terminar los chats que tiene activos y salir a su descanso
    private List<Date> lista_dias_trabajo; // Es una lista dinamica con todas las fechas en las que ha trabajado el representante
    private List<Proceso> lista_procesos; // Es una lista dinamica con todos los procesos que ha atendido el representante
    private boolean representativo; // Define si el representante es representativo o no
    private List<Double[]> matriz_simulacion; // Es una lista dinamica que contiene las sumas progresivas de los datos simulados
    private double[] probabilidad_salida_rango; // Son las probabilidades que existen de que se cierre un caso en un rango definido por el usuario
    
    // Constructor

    public Representante(String nombre_usuario, String skill, int rango_minutos) {
        this.nombre_usuario = nombre_usuario;
        this.skill = skill;
        lista_dias_trabajo = new ArrayList<>();
        lista_procesos = new ArrayList<>();
        lista_procesos.add(new Proceso("drop"));
        representativo = false;
        matriz_simulacion = new ArrayList<>();
        probabilidad_salida_rango = new double[320/rango_minutos];
    }
    
    // Metodos
    
    // Agrega un caso a un proceso especifico del representante a partir de su nombre
    
    public void agregar_caso_proceso(int duracion, Date fecha, int id, String nombre_proceso){
        if(!existe_proceso(nombre_proceso)){
            lista_procesos.add(new Proceso(nombre_proceso));
        }
        if(!Equipo.existe_fecha(fecha, getLista_dias_trabajo())){
            lista_dias_trabajo.add(fecha);
        }
        for (int i = 0; i < lista_procesos.size(); i++) {
            if(lista_procesos.get(i).getNombre_proceso().equals(nombre_proceso)){
                lista_procesos.get(i).agregar_caso(duracion, fecha, id);
            }
        }
    }
    
    // Verifica si existe o no el proceso
    
    public boolean existe_proceso(String nombre_proceso){
        for(int i = 0; i < lista_procesos.size(); i++){
            if(lista_procesos.get(i).getNombre_proceso().equals(nombre_proceso)){
                return true;
            }
        }
        return false;
    }
    
    // Elimina los datos del representante
    
    public void eliminar_datos_representante(){
        lista_dias_trabajo.clear();
        lista_procesos.clear();
    }
    
    // Organiza los procesos de mayor a menor de acuerdo a su representatividad 
    
    public void organizar_representante(double representatividad_procesos, double percentil_inferior_promedios, double percentil_superior_promedios, double percentil_tiempo_preparacion){
        List<Proceso> copia_lista_procesos = new ArrayList<>();
        int numero_procesos = lista_procesos.size();
        int suma_total_todos_casos = 0;
        for (int i = 0; i < lista_procesos.size(); i++) {
            copia_lista_procesos.add(lista_procesos.get(i));
            suma_total_todos_casos += lista_procesos.get(i).getCasos().size();
        }
        lista_procesos.clear();
        for (int i = 0; i < numero_procesos; i++) {
            lista_procesos.add(copia_lista_procesos.get(indice_mayor_proceso(copia_lista_procesos)));
            copia_lista_procesos.remove(indice_mayor_proceso(copia_lista_procesos));
        }
        organizar_procesos_representante(representatividad_procesos, percentil_inferior_promedios, percentil_superior_promedios, suma_total_todos_casos);
        calcular_tiempo_preparacion_percentil(percentil_tiempo_preparacion);
    }
    
    // Encuentra el indice del proceso con mayor representatividad en la lista
    
    public int indice_mayor_proceso(List<Proceso> lista_procesos_reducida){
        int indice_mayor = 0;
        for (int i = 1; i < lista_procesos_reducida.size(); i++) {
            if(lista_procesos_reducida.get(indice_mayor).getCasos().size() < lista_procesos_reducida.get(i).getCasos().size()){
                indice_mayor = i;
            }
        }
        return indice_mayor;
    }
    
    // Organiza todos los procesos y selecciona los procesos que son representativos 
    
    public void organizar_procesos_representante(double representatividad_procesos, double percentil_inferior_promedios, double percentil_superior_promedios, int suma_total_todos_casos){
        double suma_representatividad = 0;
        int suma_total_todos_casos_representativos = 0;
        for (int i = 0; i < lista_procesos.size(); i++) {
            lista_procesos.get(i).organizar_proceso(percentil_inferior_promedios, percentil_superior_promedios, suma_total_todos_casos);
            suma_representatividad += lista_procesos.get(i).getRepresentatividad_general();
            if(suma_representatividad <= representatividad_procesos/100){
                lista_procesos.get(i).setRepresentativo(true);
                suma_total_todos_casos_representativos += lista_procesos.get(i).getCasos().size();
            }
        }
        for (int i = 0; i < lista_procesos.size(); i++) {
            if(lista_procesos.get(i).isRepresentativo())lista_procesos.get(i).actualizar_proceso(suma_total_todos_casos_representativos);
        }
    }
    
    // Calcula un percentil tomando los tiempos promedio de respuesta diarios del representante
    
    public void calcular_tiempo_preparacion_percentil(double percentil_tiempo_preparacion){
        List<Double> copia_lista_promedios = new ArrayList<>();
        for (int i = 0; i < lista_dias_trabajo.size(); i++) {
            copia_lista_promedios.add(tiempo_promedio_dia_trabajo(lista_dias_trabajo.get(i)));
        }
        List<Double> lista_promedios = new ArrayList<>();
        for (int i = 0; i < lista_dias_trabajo.size(); i++) {
            lista_promedios.add(copia_lista_promedios.get(indice_menor_promedio(copia_lista_promedios)));
            copia_lista_promedios.remove(indice_menor_promedio(copia_lista_promedios));
        }
        double posicion_percentil = lista_promedios.size() * (percentil_tiempo_preparacion / 100);
        if((posicion_percentil % 1) != 0){
            tiempo_preparacion = lista_promedios.get((int) posicion_percentil);
        }else{
            tiempo_preparacion = (lista_promedios.get((int) posicion_percentil) + lista_promedios.get((int) posicion_percentil-1)) / 2;
        }
    }
    
    // Calcula el tiempo promedio de un dia de trabajo
    
    public double tiempo_promedio_dia_trabajo(Date fecha){
        double suma = 0;
        int numero_casos = 0;
        for(int i = 0; i < lista_procesos.size(); i++){
            suma += lista_procesos.get(i).suma_o_numero_casos_fecha(fecha, true);
            numero_casos += lista_procesos.get(i).suma_o_numero_casos_fecha(fecha, false);
        }
        if(numero_casos > 0) return suma / numero_casos;
        return 0;
    }
    
    // Encuentra el indice del menor promedio en una lista

    public int indice_menor_promedio(List<Double> lista_promedios_reducida){
        int indice_menor = 0;
        for (int i = 1; i < lista_promedios_reducida.size(); i++) {
            if(lista_promedios_reducida.get(indice_menor) > lista_promedios_reducida.get(i)){
                indice_menor = i;
            }
        }
        return indice_menor;
    }
    
    public void definir_horas_break(){
        List<Double> simulacion_tiempos_dia = new ArrayList<>();
        double representatividad_dia = 0;
        double suma_tiempos_simulacion = 0;
        for (int i = 0; i < lista_procesos.size(); i++) {
            if(lista_procesos.get(i).isCotidiano()){
                while(representatividad_dia <= lista_procesos.get(i).getRepresentatividad_general()){
                    simulacion_tiempos_dia.add(lista_procesos.get(i).getTiempo_promedio_respuesta_entre_percentiles());
                    suma_tiempos_simulacion += lista_procesos.get(i).getTiempo_promedio_respuesta_entre_percentiles();
                    representatividad_dia += lista_procesos.get(i).getRepresentatividad_dia();
                }
                representatividad_dia = 0;
            }
        }
        if(suma_tiempos_simulacion < 19200){
            simulacion_tiempos_dia.add(19200 - suma_tiempos_simulacion);
        }
        List<Combinatoria> indices_simulacion = new ArrayList<>();
        for (int i = 1; i < simulacion_tiempos_dia.size(); i++) {
            indices_simulacion.add(new Combinatoria(simulacion_tiempos_dia.size(), i));
            Double[] suma_indices_fila = ArrayUtils.toObject(sumar_indices_simulacion(simulacion_tiempos_dia, indices_simulacion.get(i-1).getCombinaciones_sin_repeticion()));
            matriz_simulacion.add(suma_indices_fila);
        }
        double probabilidad = 0;
        for (int i = 0; i < matriz_simulacion.size(); i++) {
            for (Double suma_combinacion_sin_repeticion : matriz_simulacion.get(i)) {
                probabilidad = 1.0 / matriz_simulacion.get(i).length;
                calcular_probabilidad_salida_rango(suma_combinacion_sin_repeticion, probabilidad);
            }
        }
    }
    
    public double[] sumar_indices_simulacion(List<Double> simulacion_tiempos_dia, int[][] combinaciones_sin_repeticion){
        double[] suma_indices_fila = new double[combinaciones_sin_repeticion.length];
        for (int i = 0; i < combinaciones_sin_repeticion.length; i++) {
            for (int j = 0; j < combinaciones_sin_repeticion[i].length; j++) {
                suma_indices_fila[i] += simulacion_tiempos_dia.get(combinaciones_sin_repeticion[i][j]-1);
            }
        }
        Arrays.sort(suma_indices_fila);
        return suma_indices_fila;
    }
    
    public void calcular_probabilidad_salida_rango(double suma_combinacion_sin_repeticion, double probabilidad){
        int rango = (320 / probabilidad_salida_rango.length)*60;
        for (int i = 0; i < probabilidad_salida_rango.length; i++) {
            if((suma_combinacion_sin_repeticion >= rango*i) && (suma_combinacion_sin_repeticion < rango*(i+1))){
                probabilidad_salida_rango[i] += probabilidad;
                break;
            }
        }
    }
    
    /* EN REVISION */
    
    // Codigo de referencia para pruebas del proyecto "HORUS"
    
    /*
    
    // Verifica si un rango de hora coincide con el final de un caso del representante
    
    public boolean es_hora_de_corte(int hora_corte, List<Double> lista_tiempos){
        if (lista_tiempos.isEmpty()) {
            return false;
        }else{
            double suma_tiempos = lista_tiempos.get(0);
            for (int i = 1; i < lista_tiempos.size(); i++) {
                if(suma_tiempos > hora_corte + 10){
                    return false;
                }else if((suma_tiempos > hora_corte) && (suma_tiempos < hora_corte+10)){
                    return true;
                }
                suma_tiempos += lista_tiempos.get(i);
            }
        }
        return false;
    }
    
    // Verifica cual es el proceso con mayor catidad de casos y calcula el promedio de tiempo de respuesta
    
    public void calcular_tiempo_preparacion(){
        tiempo_preparacion = procesos.get(0).tiempo_promedio_conversacion_casos_sin_drop();
        int casos_totales = procesos.get(0).cantidad_casos_sin_drop();
        for(int i = 0; i < procesos.size()-1; i++) {
            if(casos_totales < procesos.get(i++).cantidad_casos_sin_drop()){
                tiempo_preparacion = procesos.get(i).tiempo_promedio_conversacion_casos_sin_drop();
                casos_totales = procesos.get(i).cantidad_casos_sin_drop();
            }
        }
    }
    
    // Calcula el tiempo promedio de conversacion del representante
    
    public double tiempo_promedio_conversacion_sin_drop(){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < procesos.size(); i++) {
            tiempo_promedio_conversacion += procesos.get(i).tiempo_promedio_conversacion_casos_sin_drop();
        }
        if(numero_casos_no_vacios() > 0){
            return tiempo_promedio_conversacion / numero_casos_no_vacios();
        }else{
            return 0;
        } 
    }
    
    // Calcula el tiempo promedio de conversacion de los casos con drop del representante
    
    public double tiempo_promedio_conversacion_con_drop(){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < procesos.size(); i++) {
            tiempo_promedio_conversacion += procesos.get(i).tiempo_promedio_conversacion_casos_con_drop();
        }
        if(numero_casos_no_vacios() > 0){
            return tiempo_promedio_conversacion / numero_casos_no_vacios();
        }else{
            return 0;
        } 
    }
    
    // Retorna el numero de procesos que no estan vacios
    
    public int numero_casos_no_vacios(){
        int numero_casos_no_vacios = 0;
        for (int i = 0; i < procesos.size(); i++) {
            if(!procesos.get(i).getCasos().isEmpty()){
                numero_casos_no_vacios ++;
            }
        }
        return numero_casos_no_vacios;
    }
    
    // Agrega un caso a un proceso especifico tomando como parametro su nombre
    
    public void agregar_caso_proceso(int duracion, boolean drop, Date fecha, String nombre_proceso){
        for (int i = 0; i < procesos.size(); i++) {
            if(procesos.get(i).getNombre().equals(nombre_proceso)){
                procesos.get(i).agregar_caso(duracion, drop, fecha);
            }
        }
    }
    
    // Calcula el porcentaje de drop
    
    public double porcentaje_drop(){
        if(suma_todos_casos() > 0){
            return (double) suma_todos_casos_con_drop() / suma_todos_casos();
        }else{
            return 0;
        } 
    }
    
    // Calcula la cantidad total de casos para las listas de procesos
    
    public int suma_todos_casos(){
        int suma = 0;
        for (int i = 0; i < procesos.size(); i++) {
            suma += procesos.get(i).getCasos().size();
        }
        return suma;
    }
    
    public int suma_todos_casos_sin_drop(){
        int suma = 0;
        for (int i = 0; i < procesos.size(); i++) {
            suma += procesos.get(i).cantidad_casos_sin_drop();
        }
        return suma;
    }
    
    public int suma_todos_casos_con_drop(){
        int suma = 0;
        for (int i = 0; i < procesos.size(); i++) {
            suma += procesos.get(i).cantidad_casos_con_drop();
        }
        return suma;
    }
    

    
    // Aproxima un numero decimal
    
    public int aproximar(double numero_entero){
        int numero = (int) numero_entero;
        if(numero_entero%numero >= 0.5){
            return numero+1;
        }else{
            return numero;
        }
    }

*/
    
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

    public double getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(double tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public List<Date> getLista_dias_trabajo() {
        return lista_dias_trabajo;
    }

    public void setLista_dias_trabajo(List<Date> lista_dias_trabajo) {
        this.lista_dias_trabajo = lista_dias_trabajo;
    }

    public List<Proceso> getLista_procesos() {
        return lista_procesos;
    }

    public void setLista_procesos(List<Proceso> lista_procesos) {
        this.lista_procesos = lista_procesos;
    }

    public boolean isRepresentativo() {
        return representativo;
    }

    public void setRepresentativo(boolean representativo) {
        this.representativo = representativo;
    }

    public List<Double[]> getMatriz_simulacion() {
        return matriz_simulacion;
    }

    public void setMatriz_simulacion(List<Double[]> matriz_simulacion) {
        this.matriz_simulacion = matriz_simulacion;
    }

    public double[] getProbabilidad_salida_rango() {
        return probabilidad_salida_rango;
    }

    public void setProbabilidad_salida_rango(double[] probabilidad_salida_rango) {
        this.probabilidad_salida_rango = probabilidad_salida_rango;
    }

}
