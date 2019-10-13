
package panoptico.clases;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args){
        
        try{
            String nombre_ruta_archivo = "TMC.xlsx";
            double representatividad_procesos = 95;
            int tiempo_minimo_admitido = 120;
            double porcentaje_dias_representativo = 25;
            double percentil_inferior_promedios = 5;
            double percentil_superior_promedios = 95;
            double percentil_tiempo_preparacion = 95;
            Equipo chat = new Equipo(nombre_ruta_archivo, representatividad_procesos, tiempo_minimo_admitido, percentil_inferior_promedios, percentil_superior_promedios, percentil_tiempo_preparacion, 25);
            
            System.out.println(chat.getRango_maximo_minutos_equipo());
            
            for (int i = 0; i < chat.getSimulacion_dia_equipo().length; i++) {
                System.out.println(chat.getSimulacion_dia_equipo()[i]);
            }
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
