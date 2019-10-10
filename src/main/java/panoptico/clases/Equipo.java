
package panoptico.clases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private List<Representante> lista_representantes; // Es una lista dinamica de representantes que compone al equipo
    private List<Date> lista_dias_trabajo; // Es una lista de fechas que contiene las fechas en las que todo el equipo ha trabajado
    private FileInputStream archivo; // Es la representacion de la base de datos que se va a leer
    private XSSFWorkbook libro; // Es la conversion del archivo en un libro de excel para su lectura en Java

    // Constructor

    public Equipo(String nombre_ruta_archivo, double representatividad_procesos, int tiempo_minimo_admitido, double percentil_inferior_promedios, double percentil_superior_promedios, double percentil_tiempo_preparacion, double representatividad_dias_representante) throws FileNotFoundException, IOException {
        this.nombre_ruta_archivo = nombre_ruta_archivo;
        lista_representantes = new ArrayList<>();
        lista_dias_trabajo = new ArrayList<>();
        archivo = new FileInputStream(nombre_ruta_archivo);
        libro = new XSSFWorkbook(archivo);
        XSSFSheet hoja = libro.getSheetAt(0);
        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            String skill = fila.getCell(0).getStringCellValue().toLowerCase();
            String nombre_representante = fila.getCell(1).getStringCellValue().toLowerCase();
            String nombre_proceso = fila.getCell(2).getStringCellValue().toLowerCase();
            Date fecha = fila.getCell(3).getDateCellValue();
            boolean drop = true;
            if(fila.getCell(4).getStringCellValue().equals("")){
                drop = false;
            }
            int duracion = (int) fila.getCell(5).getNumericCellValue();
            int id = (int) fila.getCell(6).getNumericCellValue();
            if(drop){
                agregar_caso_representante(duracion, fecha, id, "drop", nombre_representante, skill);
            }else if(duracion >= tiempo_minimo_admitido){
                agregar_caso_representante(duracion, fecha, id, nombre_proceso, nombre_representante, skill);
            }
        }
        for (int i = 0; i < lista_representantes.size(); i++) {
            if(lista_representantes.get(i).getLista_dias_trabajo().size() > lista_dias_trabajo.size() * (representatividad_dias_representante/100)){
                lista_representantes.get(i).setRepresentativo(true);
                lista_representantes.get(i).organizar_representante(representatividad_procesos, percentil_inferior_promedios, percentil_superior_promedios, percentil_tiempo_preparacion);
            }else{
                lista_representantes.get(i).eliminar_datos_representante();
            }
        }
        libro.close();
        archivo.close();
    }
    
    // Agrega un caso a un representante en especifico, sino existe lo crea y lo asigna
    
    public void agregar_caso_representante(int duracion, Date fecha, int id, String nombre_proceso, String nombre_representante, String skill){
        if(!existe_representante(nombre_representante)){
            lista_representantes.add(new Representante(nombre_representante, skill));
        }
        if(!existe_fecha(fecha, getLista_dias_trabajo())){
            lista_dias_trabajo.add(fecha);
        }
        for (int i = 0; i < lista_representantes.size(); i++) {
            if(lista_representantes.get(i).getNombre_usuario().equals(nombre_representante)){
                lista_representantes.get(i).agregar_caso_proceso(duracion, fecha, id, nombre_proceso);
            }
        }
    }
    
    // Verifica si existe o no un representante
    
    public boolean existe_representante(String nombre_representante){
        for(int i = 0; i < lista_representantes.size(); i++){
            if(lista_representantes.get(i).getNombre_usuario().equals(nombre_representante)){
                return true;
            }
        }
        return false;
    }
    
    // Verifica si existe una fecha entre todos los dias que ha trabajado el equipo
    
    static boolean existe_fecha(Date fecha, List<Date> lista_dias_trabajo){
        for(int i = 0; i < lista_dias_trabajo.size(); i++){
            if(lista_dias_trabajo.get(i).compareTo(fecha) == 0){
                return true;
            }
        }
        return false;
    }
    
    // Calcula el promedio del tiempo de preparacion de todos los representantes
    
    public double calcular_tiempo_preparacion_equipo(double porcentaje_dias_representativo, String skill){
        int suma_tiempos_preparacion = 0;
        int suma_representantes_representantivos = 0;
        for (int i = 0; i < lista_representantes.size(); i++) {
            if(lista_representantes.get(i).getLista_dias_trabajo().size() >= lista_dias_trabajo.size()*(porcentaje_dias_representativo/100)){
                if(lista_representantes.get(i).getSkill().equals(skill) || skill.equals("equipo")){
                    suma_tiempos_preparacion += lista_representantes.get(i).getTiempo_preparacion();
                    suma_representantes_representantivos ++; 
                }
            }
        }
        if(suma_representantes_representantivos > 0){
            return suma_tiempos_preparacion / suma_representantes_representantivos;
        }else{
            return 0; 
        }
    }
    
    /* EN REVISION */
    
    // Codigo de referencia para pruebas del proyecto "HORUS"
    
    /*
    public void crear_lista_nombres(double representatividad_procesos){
        XSSFSheet hoja = libro.getSheetAt(0);
        for (int i = 0; i < hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            String nombre_usuario = fila.getCell(0).getStringCellValue().toLowerCase();
            String nombre_proceso = fila.getCell(1).getStringCellValue().toLowerCase();
            int duracion = (int) fila.getCell(4).getNumericCellValue();
            Date fecha = fila.getCell(3).getDateCellValue();
            boolean drop = true;
            if(fila.getCell(2).getStringCellValue().equals("")){
                drop = false;
            }
            for (int j = 0; j < representantes.size(); j++){
                if(nombre_usuario.equals(representantes.get(j).getNombre_usuario())){
                    representantes.get(j).agregar_caso_proceso(duracion, drop, fecha, nombre_proceso);
                }
            }
        }
        for(int i = 0; i < representantes.size(); i++) {
            representantes.get(i).calcular_tiempo_preparacion();
            representantes.get(i).calcular_horas_break();
        }
        libro.close();
        archivo.close();
    }
    
    // Calcula el tiempo de descanso de todos los representantes
    
    public double tiempo_promedio_preparacion_representantes(){
        double tiempo_promedio_preparacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            tiempo_promedio_preparacion += representantes.get(i).getTiempo_preparacion();
        }
        return tiempo_promedio_preparacion / numero_representantes_con_casos();
    }
    
    // Calcula el tiempo de descanso de todos los representantes en un skill
    
    public double tiempo_promedio_preparacion_representantes_por_skill(String skill){
        double tiempo_promedio_preparacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).getSkill().equals(skill)){
                tiempo_promedio_preparacion += representantes.get(i).getTiempo_preparacion();
            }
        }
        return tiempo_promedio_preparacion / numero_representantes_con_casos_por_skill(skill);
    }
    
    // Calcula el tiempo promedio de conversacion de todos los representantes
    
    public double tiempo_promedio_conversacion_representantes_sin_drop(){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            tiempo_promedio_conversacion += representantes.get(i).tiempo_promedio_conversacion_sin_drop();
        }
        return tiempo_promedio_conversacion / numero_representantes_con_casos();
    }
    
    public double tiempo_promedio_conversacion_representantes_con_drop(){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            tiempo_promedio_conversacion += representantes.get(i).tiempo_promedio_conversacion_con_drop();
        }
        return tiempo_promedio_conversacion / numero_representantes_con_casos();
    }
    
    // Calcula el tiempo promedio de conversacion de todos los representantes por skill
    
    public double tiempo_promedio_conversacion_skill_sin_drop(String skill){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).getSkill().equals(skill)){
                tiempo_promedio_conversacion += representantes.get(i).tiempo_promedio_conversacion_sin_drop();
            }
        }
        return tiempo_promedio_conversacion / numero_representantes_con_casos_por_skill(skill);
    }
    
    public double tiempo_promedio_conversacion_skill_con_drop(String skill){
        double tiempo_promedio_conversacion = 0;
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).getSkill().equals(skill)){
                tiempo_promedio_conversacion += representantes.get(i).tiempo_promedio_conversacion_con_drop();
            }
        }
        return tiempo_promedio_conversacion / numero_representantes_con_casos_por_skill(skill);
    }
    
    // Retorna el numero de todos los representantes con casos
    
    public int numero_representantes_con_casos(){
        int numero_representantes_no_vacios = 0;
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).numero_casos_no_vacios() > 0){
                numero_representantes_no_vacios ++;
            }
        }
        return numero_representantes_no_vacios;
    }
    
    // Retorna el numero de todos los representantes por skill
    
    public int numero_representantes_con_casos_por_skill(String skill){
        int numero_representantes_skill = 0;
        for (int i = 0; i < representantes.size(); i++) {
            if(representantes.get(i).numero_casos_no_vacios() > 0){
                if(representantes.get(i).getSkill().equals(skill)){
                    numero_representantes_skill ++;
                }
            }
        }
        return numero_representantes_skill;
    }
    
    // Genera el llenado de todas las clases y atributos del programa a partir del documento excel
    
    public void crear_equipo() throws IOException{
        representantes = crear_lista_representantes();
        XSSFSheet hoja = libro.getSheetAt(0);
        for (int i = 0; i < hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            String nombre_usuario = fila.getCell(0).getStringCellValue().toLowerCase();
            String nombre_proceso = fila.getCell(1).getStringCellValue().toLowerCase();
            int duracion = (int) fila.getCell(4).getNumericCellValue();
            Date fecha = fila.getCell(3).getDateCellValue();
            boolean drop = true;
            if(fila.getCell(2).getStringCellValue().equals("")){
                drop = false;
            }
            for (int j = 0; j < representantes.size(); j++){
                if(nombre_usuario.equals(representantes.get(j).getNombre_usuario())){
                    representantes.get(j).agregar_caso_proceso(duracion, drop, fecha, nombre_proceso);
                }
            }
        }
        for(int i = 0; i < representantes.size(); i++) {
            representantes.get(i).calcular_tiempo_preparacion();
            representantes.get(i).calcular_horas_break();
        }
        libro.close();
        archivo.close();
    }
    
    // Crea una lista con todos los representantes
    
    public List<Representante> crear_lista_representantes() throws IOException {
        List<Representante> lista_representantes = new ArrayList<>();
        XSSFSheet hoja = libro.getSheetAt(1);
        Iterator<Row> iterador_filas = hoja.iterator();
        while (iterador_filas.hasNext()) {
            Row siguiente_fila = iterador_filas.next();
            Iterator<Cell> iterador_celdas = siguiente_fila.cellIterator();
            Representante representante = new Representante();
            while (iterador_celdas.hasNext()) {
                Cell celda = iterador_celdas.next();
                switch (celda.getColumnIndex()) {
                    case 0:
                        representante.setNombre_usuario(celda.getStringCellValue().toLowerCase());
                        break;
                    case 1:
                        representante.setNombre_representante(celda.getStringCellValue().toLowerCase());
                        break;
                    case 2:
                        representante.setNombre_lider_equipo(celda.getStringCellValue().toLowerCase());
                        break;
                    case 3:
                        representante.setTurno(celda.getStringCellValue().toLowerCase().toLowerCase());
                        break;
                    case 4:
                        representante.setPais(celda.getStringCellValue().toLowerCase());
                        break;
                    case 5:
                        representante.setSkill(celda.getStringCellValue().toLowerCase());
                        break;
                    case 6:
                        representante.setCanal(celda.getStringCellValue().toLowerCase());
                        break;
                }
            }
            lista_representantes.add(representante);
        }
        libro.close();
        archivo.close();
        Iterator<Representante> iterador_representante = lista_representantes.iterator();
        while(iterador_representante.hasNext()){
            iterador_representante.next().setProcesos(crear_lista_procesos());
        }
        return lista_representantes;
    }
    
    //  Crea una lista con todos los procesos
    
    public List<Proceso> crear_lista_procesos() throws IOException {
        List<Proceso> lista_procesos = new ArrayList<>();
        XSSFSheet hoja = libro.getSheetAt(2);
        Iterator<Row> iterador_filas = hoja.iterator();
        while (iterador_filas.hasNext()) {
            Row siguiente_fila = iterador_filas.next();
            Cell celda = siguiente_fila.getCell(0);
            Proceso proceso = new Proceso();
            proceso.setNombre(celda.getStringCellValue().toLowerCase());
            lista_procesos.add(proceso);
        }
        libro.close();
        archivo.close();
        return lista_procesos;
    }
    
    // Getters and Setters

    public List<Representante> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<Representante> representantes) {
        this.representantes = representantes;
    }
    */

    // Getters and Setters

    public String getNombre_ruta_archivo() {
        return nombre_ruta_archivo;
    }

    public void setNombre_ruta_archivo(String nombre_ruta_archivo) {
        this.nombre_ruta_archivo = nombre_ruta_archivo;
    }

    public List<Representante> getLista_representantes() {
        return lista_representantes;
    }

    public void setLista_representantes(List<Representante> lista_representantes) {
        this.lista_representantes = lista_representantes;
    }

    public List<Date> getLista_dias_trabajo() {
        return lista_dias_trabajo;
    }

    public void setLista_dias_trabajo(List<Date> lista_dias_trabajo) {
        this.lista_dias_trabajo = lista_dias_trabajo;
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

}
