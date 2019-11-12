
package panoptico.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args) {
        HashMap<String, Integer> mapita = new HashMap<>();
        try{
            String nombre_ruta_archivo = "Base de datos.xlsx";
            FileInputStream archivo = new FileInputStream(nombre_ruta_archivo);
            XSSFWorkbook libro = new XSSFWorkbook(archivo);
            XSSFSheet hoja = libro.getSheetAt(0);
            Row fila;
            String skill, nombre_representante, nombre_proceso, tl, turno;
            Date fecha;
            boolean drop;
            int duracion;
            int id;
            for (int i = 1; i <= 100; i++) {
                fila = hoja.getRow(i);
                skill = fila.getCell(0).getStringCellValue().toLowerCase();
                nombre_representante = fila.getCell(1).getStringCellValue().toLowerCase();
                nombre_proceso = fila.getCell(2).getStringCellValue().toLowerCase();
                //if(fila.getCell(3).getStringCellValue().equals("")) drop = false;
                fecha = fila.getCell(4).getDateCellValue();
                drop = true;
                id = (int) fila.getCell(5).getNumericCellValue();
                duracion = (int) fila.getCell(6).getNumericCellValue();
                tl = fila.getCell(7).getStringCellValue().toLowerCase();
                turno = fila.getCell(8).getStringCellValue().toLowerCase();
                System.out.println(skill + " " + nombre_representante + " " + nombre_proceso + " " + drop + " " + fecha + " " + id + " " + duracion + " " + tl + " " + turno);
                /*
                if(drop || (duracion < tiempo_minimo_admitido)){
                    agregar_caso_representante(duracion, fecha, id, "drop", nombre_representante, skill);
                }else if(duracion >= tiempo_minimo_admitido){
                    agregar_caso_representante(duracion, fecha, id, nombre_proceso, nombre_representante, skill);
                }
                */
            }
            libro.close();
            archivo.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
