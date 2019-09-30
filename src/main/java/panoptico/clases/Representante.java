
package panoptico.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Proceso> lista_procesos_representativos; // Es una lista filtrada de los procesos que son representativos para el representante
    
    // Constructor

    public Representante(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
        lista_dias_trabajo = new ArrayList<>();
        lista_procesos = new ArrayList<>();
        lista_procesos_representativos = new ArrayList<>();
        lista_procesos.add(new Proceso("drop"));
    }
    
    // Metodos
    
    // Agrega un caso a un proceso especifico del representante a partir de su nombre
    
    public void agregar_caso_proceso(int duracion, Date fecha, int id, String nombre_proceso){
        if(!existe_proceso(nombre_proceso)){
            lista_procesos.add(new Proceso(nombre_proceso));
        }
        if(!existe_fecha(fecha)){
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
    
    // Verifica si existe o no la fecha
    
    public boolean existe_fecha(Date fecha){
        for(int i = 0; i < lista_dias_trabajo.size(); i++){
            if(lista_dias_trabajo.get(i).compareTo(fecha) == 0){
                return true;
            }
        }
        return false;
    }
    
    // Organiza los proceso de mayor a menor de acuerdo a su representatividad
    
    public void organizar_procesos(double representatividad_procesos, int tiempo_minimo_admitido){
        List<Proceso> copia_lista_procesos = new ArrayList<>();
        for (int i = 0; i < lista_procesos.size(); i++) {
            copia_lista_procesos.add(lista_procesos.get(i));
        }
        int numero_procesos = lista_procesos.size();
        lista_procesos.clear();
        for (int i = 0; i < numero_procesos; i++) {
            lista_procesos.add(copia_lista_procesos.get(indice_mayor_proceso(copia_lista_procesos)));
            copia_lista_procesos.remove(indice_mayor_proceso(copia_lista_procesos));
        }
        seleccionar_procesos_representativos(representatividad_procesos, tiempo_minimo_admitido);
        organizar_casos_procesos();
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
    
    // Selecciona los procesos que son representativos teniendo en cuenta dos parametros, el primero un porcentaje de representatividad y el segundo el tiempo minimo que puede tener un caso
    
    public void seleccionar_procesos_representativos(double representatividad_procesos, int tiempo_minimo_admitido){
        List<Proceso> copia_lista_procesos = new ArrayList<>();
        List<Proceso> copia_lista_procesos_representativos = new ArrayList<>();
        for (int i = 0; i < lista_procesos.size(); i++) {
            Proceso proceso = new Proceso(lista_procesos.get(i).getNombre_proceso());
            for(int j = 0; j < lista_procesos.get(i).getCasos().size(); j++){
                if(lista_procesos.get(i).getCasos().get(j).getDuracion() > tiempo_minimo_admitido){
                    proceso.getCasos().add(lista_procesos.get(i).getCasos().get(j));
                }
            }
            copia_lista_procesos.add(proceso);
            copia_lista_procesos_representativos.add(proceso);
        }
        double suma_representatividad = 0;
        for (int i = 0; suma_representatividad < representatividad_procesos; i++) {
            lista_procesos_representativos.add(copia_lista_procesos_representativos.get(indice_mayor_proceso(copia_lista_procesos_representativos)));
            copia_lista_procesos_representativos.remove(indice_mayor_proceso(copia_lista_procesos_representativos));
            suma_representatividad += representatividad_proceso(copia_lista_procesos, lista_procesos_representativos.get(i).getCasos().size());
        }       
    }
    
    // Calcula la representatividad de un proceso
    
    public double representatividad_proceso(List<Proceso> lista_procesos_equipo, int numero_casos_proceso){
        double suma = 0;
        for (int i = 0; i < lista_procesos_equipo.size(); i++) {
            suma += lista_procesos_equipo.get(i).getCasos().size();
        }
        return (numero_casos_proceso / suma)*100;
    }
    
    // Organiza los casos dentro de todos los procesos de menor a mayor
    
    public void organizar_casos_procesos(){
        for (int i = 0; i < lista_procesos.size(); i++) {
            lista_procesos.get(i).organizar_casos();
        }
        for (int i = 0; i < lista_procesos_representativos.size(); i++) {
            lista_procesos_representativos.get(i).organizar_casos();
        }
    }
    
    // Calcula un percentil tomando los tiempos promedio de respuesta diarios del representante
    
    public double calcular_percentil(double percentil){
        List<Double> copia_lista_promedios = new ArrayList<>();
        for (int i = 0; i < lista_dias_trabajo.size(); i++) {
            copia_lista_promedios.add(tiempo_promedio_dia_trabajo(lista_dias_trabajo.get(i)));
        }
        List<Double> lista_promedios = new ArrayList<>();
        for (int i = 0; i < lista_dias_trabajo.size(); i++) {
            lista_promedios.add(copia_lista_promedios.get(indice_menor_promedio(copia_lista_promedios)));
            copia_lista_promedios.remove(indice_menor_promedio(copia_lista_promedios));
        }
        double posicion_percentil = lista_promedios.size() * (percentil / 100);
        if((posicion_percentil % 1) != 0){
            posicion_percentil -= posicion_percentil % 1;
            return lista_promedios.get((int) posicion_percentil);
        }else{
            return (lista_promedios.get((int) posicion_percentil) + lista_promedios.get((int) posicion_percentil-1)) / 2;
        }
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
    
    // Calcula e tiempo promedio de un dia de trabajo
    
    public double tiempo_promedio_dia_trabajo(Date fecha){
        if(existe_fecha(fecha)){
            double suma = 0;
            int numero_casos = 0;
            for(int i = 0; i < lista_procesos.size(); i++){
                if(lista_procesos.get(i).suma_o_numero_casos_fecha(fecha, false) > 0){
                    suma += lista_procesos.get(i).suma_o_numero_casos_fecha(fecha, true);
                    numero_casos += lista_procesos.get(i).suma_o_numero_casos_fecha(fecha, false);
                }
            }
            return suma / numero_casos;
        }else{
            return 0;
        }
    }
    
    // Calcula el tiempo promedio de respuesta de un representante teniendo en cuenta solo los datos que se encuentran definidos entre dos percentiles
    
    public double tiempo_promedio_general_representante_rango(double percentil_inferior, double percentil_superior){
        double suma = 0;
        for (int i = 0; i < lista_procesos_representativos.size(); i++) {
            suma += lista_procesos_representativos.get(i).tiempo_promedio_proceso_rango(percentil_inferior, percentil_superior);
        }
        return suma / lista_procesos_representativos.size();
    }
    
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

    public List<Proceso> getLista_procesos_representativos() {
        return lista_procesos_representativos;
    }

    public void setLista_procesos_representativos(List<Proceso> lista_procesos_representativos) {
        this.lista_procesos_representativos = lista_procesos_representativos;
    }

}
