
package panoptico.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author montenegro
 */
public class Equipo {

    // Atributos
    
    private ArrayList<Caso> casos; // Es un arreglo dinamico que contiene todos los casos
    private HashMap mapa_busqueda; // Es el mapa de busqueda del programa
    private double tiempo_futuro_estado; // Es el tiempo necesario para desencolar las consultas
    private double[] simulacion_jornada; // Son las probabilidades que existen de que se cierre un caso en el rango de un minuto

    // Constructor
    
    public Equipo(int tiempo_minimo, double porcentaje_dias_trabajo, double percentil_tiempo_descanso) throws FileNotFoundException, IOException {
        FileInputStream archivo = new FileInputStream(new File("Base de datos.xlsx"));
        Workbook documento_excel = new XSSFWorkbook(archivo);
        Sheet hoja = documento_excel.getSheetAt(0);
        casos = new ArrayList<>(); 
        Iterator<Row> iterador_filas = hoja.iterator();
        while (iterador_filas.hasNext()) {
            Row siguiente_fila = iterador_filas.next();
            casos.add(new Caso(siguiente_fila, tiempo_minimo));
        }
        documento_excel.close();
        archivo.close();
        mapa_busqueda = new HashMap<>();
        //eliminar_no_representativos(porcentaje_dias_trabajo);
        //calcular_tiempo_futuro_estado(percentil_tiempo_descanso);
    }

    // Metodos
    
    // Aplica los filtros indicados al mapa de busqueda
    
    public void generar_mapa_busqueda(int[] filtros){
        
    }
    
    // Retorna un mapa de busqueda con varios filtros
    
    public HashMap filtrar_mapa_busqueda(ArrayList<Caso> arreglo, int[] filtros){
        HashMap mapa_filtrado = crear_mapa_busqueda(arreglo, filtros[0]);
        if(filtros.length == 1){
            for (Object llave : mapa_filtrado.keySet()) {
                ArrayList<Caso> arreglo_filtro = (ArrayList<Caso>) mapa_filtrado.get(llave);
                mostrar_arreglo_consola(arreglo_filtro);
            }
            return mapa_filtrado;
        }else{
            int[] nuevos_filtros = new int[filtros.length-1];
            System.arraycopy(filtros, 1, nuevos_filtros, 0, filtros.length - 1);   
            ArrayList<Caso> arreglo_filtro = new ArrayList<>();
            for (Object llave : mapa_filtrado.keySet()) {
                arreglo_filtro = (ArrayList<Caso>) mapa_filtrado.get(llave);
                //mostrar_arreglo_consola(arreglo_filtro);
                mapa_filtrado.put(llave, filtrar_mapa_busqueda(arreglo_filtro, nuevos_filtros));
            }
            return mapa_filtrado;
        }
    }
    
    // Crea un mapa de busqueda a partir de un arreglo dinamico
    
    public HashMap crear_mapa_busqueda(ArrayList<Caso> arreglo, int numero_columna_llave){
        HashMap<Object, ArrayList<Caso>> mapa_filtrado = new HashMap<>();
        Object llave = null;
        for (Caso caso : casos) {
            llave = objeto_filtro(numero_columna_llave, caso);
            if(mapa_filtrado.containsKey(llave)){
                mapa_filtrado.get(llave).add(caso);
            }else{
                ArrayList<Caso> lista_filtrada = new ArrayList<>();
                lista_filtrada.add(caso);
                mapa_filtrado.put(llave, lista_filtrada);
            }
        }
        return mapa_filtrado;
    }
    
    // Devuelve el tipo de objeto segun la columna elegida

    public Object objeto_filtro(int numero_columna_filtro, Caso caso) {
        switch (numero_columna_filtro) {
            case 1:
                return caso.getSkill();
            case 2:
                return caso.getNombre_usuario();
            case 3:
                return caso.getProceso();
            case 4:
                return caso.getFecha();
            case 7:
                return caso.getTl();
            case 8:
                return caso.getTurno();
        }
        return null;
    }
    
    /*
    // Elimina los representantes que no cumplen con un porcentaje minimo de dias trabajados
    
    public void eliminar_no_representativos(double porcentaje_dias_trabajo){
        int dias_trabajados = crear_filtros(2, 4, 4);
        mapa_busqueda.values().removeIf(representante -> representante.size() < dias_trabajados*(porcentaje_dias_trabajo/100));
        convertir_mapa_en_arreglo_original();
    }
*/
    /*
    // Convierte el mapa actual en arreglo de casos original
    
    public void convertir_mapa_en_arreglo_original(){
        casos = new ArrayList<>();
        for (Object llave : mapa_busqueda.keySet()) {
            Collection<ArrayList<Caso>> lista_listas_casos = mapa_busqueda.get(llave).values(); 
            Iterator<ArrayList<Caso>> iterador_lista_listas_casos = lista_listas_casos.iterator();
            while (iterador_lista_listas_casos.hasNext()) {
                casos.addAll(iterador_lista_listas_casos.next());
            }
        }
    }
*/
    
    
    
    /*
    // Aplica filtros al mapa de busqueda y devuelve la cantidad de elementos no repetidos de una columna especificada
    
    public int crear_filtros(int numero_columna_primer_filtro, int numero_columna_segundo_filtro, int numero_columna_elementos_no_repetidos) {
        mapa_busqueda.clear();
        Object primer_filtro = null;
        Object segundo_filtro = null;
        Object elemento_columna = null;
        ArrayList<Object> elementos_no_repetidos = new ArrayList<>();
        for (Caso caso : casos) {
            primer_filtro = objeto_filtro(numero_columna_primer_filtro, caso);
            segundo_filtro = objeto_filtro(numero_columna_segundo_filtro, caso);
            elemento_columna = objeto_filtro(numero_columna_elementos_no_repetidos, caso);
            if (mapa_busqueda.containsKey(primer_filtro)) {
                if (mapa_busqueda.get(primer_filtro).containsKey(segundo_filtro)) {
                    mapa_busqueda.get(primer_filtro).get(segundo_filtro).add(caso);
                } else {
                    ArrayList<Caso> lista_filtro = new ArrayList<>();
                    lista_filtro.add(caso);
                    mapa_busqueda.get(primer_filtro).put(segundo_filtro, lista_filtro);
                }
            } else {
                HashMap<Object, ArrayList<Caso>> mapa_busqueda_segundo_filtro = new HashMap<>();
                ArrayList<Caso> lista_filtro = new ArrayList<>();
                lista_filtro.add(caso);
                mapa_busqueda_segundo_filtro.put(segundo_filtro, lista_filtro);
                mapa_busqueda.put(primer_filtro, mapa_busqueda_segundo_filtro);
            }
            if(!elementos_no_repetidos.contains(elemento_columna)) elementos_no_repetidos.add(elemento_columna);
        }
        return elementos_no_repetidos.size();
    }
    
    */
    
    
    /*
    // Calcula el tiempo necesario para desencolar
    
    public void calcular_tiempo_futuro_estado(double percentil_tiempo_descanso){
        ArrayList<Double> lista_percentiles_representantes = new ArrayList<>();
        ArrayList<Double> lista_percentiles_fechas = new ArrayList<>();
        for (Object representante : mapa_busqueda.keySet()) {
            lista_percentiles_fechas.clear();
            for (Object fecha : mapa_busqueda.get(representante).keySet()) {
                lista_percentiles_fechas.add(calcular_promedio_fecha(mapa_busqueda.get(representante).get(fecha)));
            }
            lista_percentiles_representantes.add(calcular_percentil_arreglo_double(percentil_tiempo_descanso, lista_percentiles_fechas));
        }
        tiempo_futuro_estado = calcular_promedio_double(lista_percentiles_representantes);
    }
    
    // Calcula el tiempo promedio en una fecha especifica
    
    public double calcular_promedio_fecha(ArrayList<Caso> arreglo){
        double suma = 0;
        for (Caso caso : arreglo) {
            suma += caso.getDuracion();
        }
        return suma/arreglo.size();
    }
    
    // Calcula el promedio de un arreglo de double
    
    public double calcular_promedio_double(ArrayList<Double> arreglo){
        double suma = 0;
        for (Double numero : arreglo) {
            suma += numero;
        }
        return suma/arreglo.size();
    }
    
    // Calcula un percentil en un arreglo de doubles ordenado
    
    public double calcular_percentil_arreglo_double(double percentil, ArrayList<Double> arreglo){
        Collections.sort(arreglo);
        double posicion_percentil = arreglo.size() * (percentil / 100);
        if((posicion_percentil % 1) != 0){
            return arreglo.get((int) posicion_percentil);
        }else{
            return (arreglo.get((int) posicion_percentil) + arreglo.get((int) posicion_percentil-1))/2;
        }
    }
    
    // Simula una jornada laboral minuto a minuto
    
    public void simular_dia_representante(int percentil_inferior, int percentil_superior, double percentil_procesos){
        simulacion_jornada = new double[320];
        crear_filtros(3, 8, 3);
     
        System.out.println(mapa_busqueda.size());
        mapa_busqueda.keySet().removeIf(nombre_proceso -> !es_cotidiano(percentil_inferior, percentil_superior, realizar_busqueda_un_filtro(nombre_proceso)));
        System.out.println("----------------");
        System.out.println(mapa_busqueda.size());
    }
    
    // Define si un proceso es cotidiano o no
    
    public boolean es_cotidiano(double percentil_inferior, double percentil_superior, ArrayList<Caso> proceso){
        double dia = calcular_representatividad_dia(percentil_inferior, percentil_superior, proceso);
        double general = calcular_representatividad_general(proceso);
        if(dia <= general) return true;
        return false;
    }
    
    // Calcula la representatividad que tendria un caso de un proceso en la jornada de trabajo
    
    public double calcular_representatividad_dia(double percentil_inferior, double percentil_superior, ArrayList<Caso> proceso){
        return  calcular_tiempo_promedio_proceso_rango(percentil_inferior, percentil_superior, proceso)/19200;
    }
    
    // Calcula el tiempo promedio de respuesta en el rango definido por dos percentiles
    
    public double calcular_tiempo_promedio_proceso_rango(double percentil_inferior, double percentil_superior, ArrayList<Caso> proceso){
        double suma = 0;
        int numero_casos = 0;
        for (int i = 0; i < proceso.size(); i++) {
            if((proceso.get(i).getDuracion() >= calcular_percentil_arreglo_casos(percentil_inferior, proceso)) && (proceso.get(i).getDuracion() <= calcular_percentil_arreglo_casos(percentil_superior, proceso))){
                suma += proceso.get(i).getDuracion();
                numero_casos ++;
            }
        }
        return suma / numero_casos;
    }
    
    // Calcula un percentil en un arreglo de doubles ordenado
    
    public double calcular_percentil_arreglo_casos(double percentil, ArrayList<Caso> arreglo){
        Collections.sort(arreglo);
        double posicion_percentil = arreglo.size() * (percentil / 100);
        if((posicion_percentil % 1) != 0){
            return arreglo.get((int) posicion_percentil).getDuracion();
        }else{
            return (arreglo.get((int) posicion_percentil).getDuracion() + arreglo.get((int) posicion_percentil-1).getDuracion())/2;
        }
    }
    
    // Calcula la representatividad del proceso con respecto a los otros
    
    public double calcular_representatividad_general(ArrayList<Caso> arreglo){
        System.out.println(((double) arreglo.size())/ casos.size());
        return ((double) arreglo.size())/ casos.size();
    }
    
    // Realiza la busqueda de los datos a partir de un filtro
    
    public ArrayList<Caso> realizar_busqueda_un_filtro(Object filtro){
        System.out.println(filtro);
        if (!mapa_busqueda.containsKey(filtro)) return new ArrayList<>();
        Collection<ArrayList<Caso>> lista_listas_casos = mapa_busqueda.get(filtro).values(); 
        ArrayList<Caso> lista_casos_filtro = new ArrayList<>();
        Iterator<ArrayList<Caso>> iterador_lista_listas_casos = lista_listas_casos.iterator();
        while (iterador_lista_listas_casos.hasNext()) {
            lista_casos_filtro.addAll(iterador_lista_listas_casos.next());
        }
        return lista_casos_filtro;
    }
    
    // Realiza la busqueda de los datos a partir de dos filtros
    
    public ArrayList<Caso> realizar_busqueda_dos_filtros(Object primer_filtro, Object segunto_filtro){
        if (!mapa_busqueda.containsKey(primer_filtro)){
            return new ArrayList<>();
        }else{
            if (!mapa_busqueda.containsKey(primer_filtro)) return new ArrayList<>();
            return mapa_busqueda.get(primer_filtro).get(segunto_filtro);
        }
    }
*/
    
    // Muestra un arreglo de casos por consola
    
    public void mostrar_arreglo_consola(ArrayList<Caso> arreglo){
        Iterator<Caso> iterador_casos = arreglo.iterator();
        while (iterador_casos.hasNext()) {
            iterador_casos.next().mostrar_caso_consola();
        }
    }

    // G&S

    public ArrayList<Caso> getCasos() {
        return casos;
    }

    public void setCasos(ArrayList<Caso> casos) {
        this.casos = casos;
    }

    public HashMap getMapa_busqueda() {
        return mapa_busqueda;
    }

    public void setMapa_busqueda(HashMap mapa_busqueda) {
        this.mapa_busqueda = mapa_busqueda;
    }

    public double getTiempo_futuro_estado() {
        return tiempo_futuro_estado;
    }

    public void setTiempo_futuro_estado(double tiempo_futuro_estado) {
        this.tiempo_futuro_estado = tiempo_futuro_estado;
    }

    public double[] getSimulacion_jornada() {
        return simulacion_jornada;
    }

    public void setSimulacion_jornada(double[] simulacion_jornada) {
        this.simulacion_jornada = simulacion_jornada;
    }

}

