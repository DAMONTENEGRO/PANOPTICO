
package panoptico.clases;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args){
        
        try{
            String nombre_ruta_archivo = "TMC MES.xlsx";
            int representatividad_procesos = 95;
            int tiempo_minimo_admitido = 120;
            int porcentaje_dias_representativo = 25;
            int percentil_inferior_promedios = 5;
            int percentil_superior_promedios = 95;
            int percentil_tiempo_preparacion = 95;
            Equipo chat = new Equipo(nombre_ruta_archivo, representatividad_procesos, tiempo_minimo_admitido, percentil_inferior_promedios, percentil_superior_promedios, percentil_tiempo_preparacion, 25);
            
            chat.getRepresentantes().get(1).simular_dia_representante(1);
            System.out.println(chat.getRepresentantes().get(1).getNombre_usuario());
            System.out.println("Es representativo: " + chat.getRepresentantes().get(1).isRepresentativo());
            DecimalFormat formato = new DecimalFormat("#.000");
            
            for (int i = 0; i < chat.getRepresentantes().get(1).getSimulacion_dia().length; i++) {
                if(i == chat.getRepresentantes().get(1).getSimulacion_dia().length/2) System.out.println("");
                System.out.print("Rango de " + i + " a " + (i+1) + " es: ");
                System.out.println(formato.format(chat.getRepresentantes().get(1).getSimulacion_dia()[i]*100) + " % ");
            }
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
