
package panoptico.clases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public final class Equipo {
    
    // Atributos
    
    private String nombre_ruta_archivo; // Es la ruta en la cual se encuentra la base de datos
    private FileInputStream archivo; // Es la representacion de la base de datos que se va a leer
    private XSSFWorkbook libro; // Es la conversion del archivo en un libro de excel para su lectura en Java
    private ArrayList<Representante> representantes; // Es un arreglo dinamico con los representantes que componen al equipo
    private ArrayList<Date> dias_trabajo; // Es un arreglo dinamico que contiene las fechas en las que todo el equipo ha trabajado
    private double[] simulacion_dia_equipo; // Son las probabilidades que existen de que se cierre un caso en un rango de tiempo definido por el usuario

    // Constructor

    public Equipo(String nombre_ruta_archivo, int representatividad_procesos, int tiempo_minimo_admitido, int percentil_inferior_promedios, int percentil_superior_promedios, int percentil_tiempo_preparacion, int representatividad_dias_representante) throws FileNotFoundException, IOException {
        this.nombre_ruta_archivo = nombre_ruta_archivo;
        archivo = new FileInputStream(nombre_ruta_archivo);
        libro = new XSSFWorkbook(archivo);
        representantes = new ArrayList<>();
        dias_trabajo = new ArrayList<>();
        XSSFSheet hoja = libro.getSheetAt(0);
        Row fila;
        String skill, nombre_representante, nombre_proceso;
        Date fecha;
        boolean drop;
        int duracion;
        int id;
        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            fila = hoja.getRow(i);
            skill = fila.getCell(0).getStringCellValue().toLowerCase();
            nombre_representante = fila.getCell(1).getStringCellValue().toLowerCase();
            nombre_proceso = fila.getCell(2).getStringCellValue().toLowerCase();
            fecha = fila.getCell(3).getDateCellValue();
            drop = true;
            if(fila.getCell(4).getStringCellValue().equals("")) drop = false;
            duracion = (int) fila.getCell(5).getNumericCellValue();
            id = (int) fila.getCell(6).getNumericCellValue();
            if(drop || (duracion < tiempo_minimo_admitido)){
                agregar_caso_representante(duracion, fecha, id, "drop", nombre_representante, skill);
            }else if(duracion >= tiempo_minimo_admitido){
                agregar_caso_representante(duracion, fecha, id, nombre_proceso, nombre_representante, skill);
            }
        }
        libro.close();
        archivo.close();
        Collections.sort(representantes);
        for (int i = 0; i < representantes.size(); i++) {
            if (representantes.get(i).getDias_trabajo().size() > dias_trabajo.size() * ((double) representatividad_dias_representante / 100)) {
                representantes.get(i).setRepresentativo(true);
                representantes.get(i).organizar_representante(representatividad_procesos, percentil_inferior_promedios, percentil_superior_promedios, percentil_tiempo_preparacion);
            } else {
                representantes.get(i).eliminar_datos_representante();
            }
        }
        //simular_dia_representantes();
    }
    
    // Agrega un caso a un representante en especifico, sino existe lo crea y lo asigna
    
    public void agregar_caso_representante(int duracion, Date fecha, int id, String nombre_proceso, String nombre_representante, String skill){
        if(!existe_representante(nombre_representante)){
            representantes.add(new Representante(nombre_representante, skill));
        }
        if(!dias_trabajo.contains(fecha)){
            dias_trabajo.add(fecha);
        }
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).getNombre_usuario().equals(nombre_representante)){
                representantes.get(i).agregar_caso_proceso(duracion, fecha, id, nombre_proceso);
            }
        }
    }
    
    // Verifica si existe o no un representante
    
    public boolean existe_representante(String nombre_representante){
        for(int i = 0; i < representantes.size(); i++){
            if(representantes.get(i).getNombre_usuario().equals(nombre_representante)){
                return true;
            }
        }
        return false;
    }
    
    // Simula un dia de trabajo para todos los representantes representativos
    
    public void simular_dia_representantes(){
        int numero_representantes_representativos = 0;
        simulacion_dia_equipo = new double[320];
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).isRepresentativo()){
                representantes.get(i).simular_dia_representante(1);
                double[] simulacion_dia_representante = representantes.get(i).getSimulacion_dia();
                for (int j = 0; j < simulacion_dia_equipo.length; j++) {
                    simulacion_dia_equipo[j] += simulacion_dia_representante[j];
                }
                numero_representantes_representativos ++;
            }
        }
        for (int i = 0; i < simulacion_dia_equipo.length; i++) {
            simulacion_dia_equipo[i] /= numero_representantes_representativos;
        }
    }
    
    // Calcula el promedio del tiempo de preparacion de todos los representantes
    
    public double calcular_tiempo_preparacion_equipo(String skill){
        double suma_tiempos_preparacion = 0;
        int suma_representantes = 0;
        for (Representante representante : representantes) {
            if((representante.isRepresentativo()) && (skill.equals("equipo") || representante.getSkill().equals(skill)) ){
                suma_tiempos_preparacion += representante.getTiempo_preparacion();
                suma_representantes ++;
            }
        }
        if(suma_representantes > 0) return suma_tiempos_preparacion / suma_representantes;
        return 0;
    }

    // Getters and Setters

    public String getNombre_ruta_archivo() {
        return nombre_ruta_archivo;
    }

    public void setNombre_ruta_archivo(String nombre_ruta_archivo) {
        this.nombre_ruta_archivo = nombre_ruta_archivo;
    }

    public FileInputStream getArchivo() {
        return archivo;
    }

    public void setArchivo(FileInputStream archivo) {
        this.archivo = archivo;
    }

    public XSSFWorkbook getLibro() {
        return libro;
    }

    public void setLibro(XSSFWorkbook libro) {
        this.libro = libro;
    }

    public ArrayList<Representante> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(ArrayList<Representante> representantes) {
        this.representantes = representantes;
    }

    public ArrayList<Date> getDias_trabajo() {
        return dias_trabajo;
    }

    public void setDias_trabajo(ArrayList<Date> dias_trabajo) {
        this.dias_trabajo = dias_trabajo;
    }

    public double[] getSimulacion_dia_equipo() {
        return simulacion_dia_equipo;
    }

    public void setSimulacion_dia_equipo(double[] simulacion_dia_equipo) {
        this.simulacion_dia_equipo = simulacion_dia_equipo;
    }

}
