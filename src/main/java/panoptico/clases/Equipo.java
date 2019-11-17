
package panoptico.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    
    public class Contenedor implements Comparable<Contenedor>{
        Object nombre;
        double dato;

        public Contenedor(Object nombre, double dato) {
            this.nombre = nombre;
            this.dato = dato;
        }

        @Override
        public int compareTo(Contenedor contenedor) {
            if(dato > contenedor.dato){
                return -1;
            }else if(dato < contenedor.dato){
                return 1;
            }else{
                return 0;
            }
        }
    }

    // Atributos
    
    private ArrayList<Caso> casos; // Es un arreglo dinamico que contiene todos los casos
    private HashMap mapa_busqueda; // Es el mapa de busqueda del programa para los datos
    private HashMap glosario_busquedas; // Es el mapa de busqueda del programa para los resultados
    
    // Constructor
    
    public Equipo(int tiempo_minimo, double representatividad_representante, double percentil_tiempo_futuro_estado, double percentil_procesos, double inferior, double superior) throws FileNotFoundException, IOException {
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
        casos.removeIf(caso -> esta_incompleto(caso));
        mapa_busqueda = new HashMap<>();
        glosario_busquedas = new HashMap<>();
        eliminar_no_representativos(representatividad_representante, percentil_tiempo_futuro_estado, percentil_procesos);
        simular_jornada(inferior, superior);
    }

    // Metodos
    
    // Verifica si el caso tiene celdas vacias o datos faltantes
    
    public boolean esta_incompleto(Caso caso){
        if(caso.getSkill().equals("")) return true;
        if(caso.getNombre_usuario().equals("")) return true;
        if(caso.getProceso().equals("")) return true;
        if(caso.getTl().equals("")) return true;
        if(caso.getTurno().equals("")) return true;
        return false;
    }
    
    // Elimina los valores que no son representativos
    
    public void eliminar_no_representativos(double representatividad_representante, double percentil_tiempo_futuro_estado, double percentil_procesos){
        int[][] filtros = {{4}, {2,4}, {3}};
        generar_mapa_busqueda(filtros[0]);
        double casos_minimos = mapa_busqueda.size()*(representatividad_representante/100);
        generar_mapa_busqueda(filtros[1]);
        ArrayList<Object> no_representativos = new ArrayList<>();
        for (Object representante : mapa_busqueda.keySet()) {
            if(((HashMap) mapa_busqueda.get(representante)).size() < casos_minimos){
                no_representativos.add(representante);
            }
        }
        casos.removeIf(caso -> no_representativos.contains(caso.getNombre_usuario()));
        calcular_tiempo_futuro_estado(percentil_procesos);
        no_representativos.clear();
        int suma_representatividad = 0;
        generar_mapa_busqueda(filtros[2]);
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        for (Object proceso : mapa_busqueda.keySet()) {
            contenedores.add(new Contenedor(proceso, ((ArrayList<Caso>) mapa_busqueda.get(proceso)).size()));
        }
        Collections.sort(contenedores);
        for (Contenedor contenedor : contenedores) {
            if(suma_representatividad > casos.size()*(percentil_procesos/100)){
                no_representativos.add(contenedor.nombre);
            }
            suma_representatividad += contenedor.dato;
        }
        casos.removeIf(caso -> no_representativos.contains(caso.getProceso()));
    }
    
    // Aplica los filtros indicados al mapa de busqueda
    
    public void generar_mapa_busqueda(int[] filtros){
        mapa_busqueda = filtrar_mapa_busqueda(casos, filtros);
    }
    
    // Retorna un mapa de busqueda con varios filtros
    
    public HashMap filtrar_mapa_busqueda(ArrayList<Caso> arreglo, int[] filtros){
        HashMap mapa_filtrado = crear_mapa_busqueda(arreglo, filtros[0]);
        if(filtros.length == 1){
            return mapa_filtrado;
        }else{
            int[] nuevos_filtros = new int[filtros.length-1];
            System.arraycopy(filtros, 1, nuevos_filtros, 0, filtros.length - 1);   
            for (Object llave : mapa_filtrado.keySet()) {
                mapa_filtrado.put(llave, filtrar_mapa_busqueda((ArrayList<Caso>) mapa_filtrado.get(llave), nuevos_filtros));
            }
            return mapa_filtrado;
        }
    }
    
    // Crea un mapa de busqueda a partir de un arreglo dinamico
    
    public HashMap crear_mapa_busqueda(ArrayList<Caso> arreglo, int numero_columna_llave){
        HashMap<Object, ArrayList<Caso>> mapa_filtrado = new HashMap<>();
        Object llave = null;
        for (Caso caso : arreglo) {
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
    
    // Calcula el tiempo necesario para desencolar
    
    public void calcular_tiempo_futuro_estado(double percentil_tiempo_futuro_estado){
        int[] filtros = {8, 7, 2, 4};
        generar_mapa_busqueda(filtros);
        ArrayList<Double> turno = new ArrayList<>();
        ArrayList<Double> tl = new ArrayList<>();
        ArrayList<Double> nombre_usuario = new ArrayList<>();
        ArrayList<Double> fecha = new ArrayList<>(); 
        double valor = 0;
        for (Object a : mapa_busqueda.keySet()) {
            for (Object b : ((HashMap) mapa_busqueda.get(a)).keySet()) {
                for (Object c : ((HashMap) ((HashMap) mapa_busqueda.get(a)).get(b)).keySet()) {
                    for (Object d : ((HashMap) ((HashMap) ((HashMap) mapa_busqueda.get(a)).get(b)).get(c)).keySet()) {
                        fecha.add(calcular_promedio_duracion((ArrayList<Caso>) ((HashMap)((HashMap)((HashMap)mapa_busqueda.get(a)).get(b)).get(c)).get(d)));
                    }
                    valor = calcular_percentil_arreglo_double(percentil_tiempo_futuro_estado, fecha);
                    nombre_usuario.add(valor);
                    glosario_busquedas.put(("tiempo futuro estado " + c), valor);
                }
                valor = calcular_promedio_double(nombre_usuario);
                tl.add(valor);
                glosario_busquedas.put(("tiempo futuro estado " + b), valor);
            }
            valor = calcular_promedio_double(tl);
            turno.add(valor);
            glosario_busquedas.put(("tiempo futuro estado " + a), valor);
        }
        glosario_busquedas.put("tiempo futuro estado equipo", calcular_promedio_double(turno));
    }
    
    // Calcula el tiempo promedio en una fecha especifica
    
    public double calcular_promedio_duracion(ArrayList<Caso> arreglo){
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
    
    // Simula una jornada minuto a minuto
    
    public void simular_jornada(double inferior, double superior){
        int[] filtros = {3};
        generar_mapa_busqueda(filtros);
        double[] simulacion_jornada = new double[320];
        ArrayList<Object> arreglo = new ArrayList<>();
        for (Object proceso : mapa_busqueda.keySet()) {
            if(!es_cotidiano(inferior, superior, (ArrayList<Caso>) mapa_busqueda.get(proceso), (String) proceso)) arreglo.add(proceso);
        }
        mapa_busqueda.keySet().removeIf(proceso -> arreglo.contains(proceso));
        casos.removeIf(caso -> arreglo.contains(caso.getProceso()));
        arreglo.clear();
        double suma_tiempos_casos = 0;
        for (Object proceso : mapa_busqueda.keySet()) {
            double representatividad_general = calcular_representatividad_general((ArrayList<Caso>) mapa_busqueda.get(proceso));
            double representatividad_dia = (double) glosario_busquedas.get("tiempo medio conversacion " + proceso)/19200;
            while (representatividad_dia < representatividad_general ) {
                arreglo.add(glosario_busquedas.get("tiempo medio conversacion " + proceso));
                suma_tiempos_casos += (double) arreglo.get(arreglo.size()-1);
                representatividad_dia += (double) glosario_busquedas.get("tiempo medio conversacion " + proceso)/19200;
            }
            representatividad_dia = 0;
        }
        if(suma_tiempos_casos < 19200) arreglo.add(19200 - suma_tiempos_casos);
        Combinatoria combinatoria = new Combinatoria(arreglo);
        double probabilidad = 0;
        for (double[] simulacion : combinatoria.getMatriz_suma_indices()) {
            for (double suma : simulacion) {
                probabilidad = 1.0 / simulacion.length;
                simulacion_jornada = calcular_rango(simulacion_jornada, suma, probabilidad);
            }
        }
        glosario_busquedas.put("simulacion jornada", simulacion_jornada);
    }
    
    // Define si un proceso es cotidiano o no
    
    public boolean es_cotidiano(double inferior, double superior, ArrayList<Caso> proceso, String nombre_proceso){
        glosario_busquedas.put(("representatividad " + nombre_proceso), (((double) proceso.size())/casos.size())*100);
        return calcular_tiempo_promedio_proceso_rango(inferior, superior, proceso, nombre_proceso)/19200 <= ((double) proceso.size())/casos.size();
    }
    
    // Calcula el tiempo promedio de respuesta en el rango definido por dos percentiles
    
    public double calcular_tiempo_promedio_proceso_rango(double inferior, double superior, ArrayList<Caso> proceso, String nombre_proceso){
        Collections.sort(proceso);
        double suma = 0;
        int numero_casos = 0;
        double p_inferior = calcular_percentil_arreglo_casos(inferior, proceso);
        double p_superior = calcular_percentil_arreglo_casos(superior, proceso);
        for (int i = 0; i < proceso.size(); i++) {
            if((proceso.get(i).getDuracion() >= p_inferior) && (proceso.get(i).getDuracion() <= p_superior)){
                suma += proceso.get(i).getDuracion();
                numero_casos ++;
            }
        }
        glosario_busquedas.put(("tiempo medio conversacion " + nombre_proceso), suma / numero_casos);
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
        return ((double) arreglo.size())/ casos.size();
    }
    
    // Asigna la probabilidad al rango de la jornada al que pertence el cierre de un caso
    
    public double[] calcular_rango(double[] simulacion_jornada, double suma_combinacion_sin_repeticion, double probabilidad){
        for (int i = 0; i < simulacion_jornada.length; i++) {
            if((suma_combinacion_sin_repeticion > 60*i) && (suma_combinacion_sin_repeticion <= 60*(i+1))){
                simulacion_jornada[i] += probabilidad;
                break;
            }
        }
        return simulacion_jornada;
    }
    
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

    public HashMap<String, Double> getGlosario_busquedas() {
        return glosario_busquedas;
    }

    public void setGlosario_busquedas(HashMap<String, Double> glosario_busquedas) {
        this.glosario_busquedas = glosario_busquedas;
    }

}

