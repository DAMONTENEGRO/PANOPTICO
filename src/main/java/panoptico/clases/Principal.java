
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
            
            int suma_r = 0;
            int suma_c = 0;
            for (int i = 0; i < chat.getLista_representantes().size(); i++) {
                for (int j = 0; j < chat.getLista_representantes().get(i).getLista_procesos().size(); j++) {
                    if(chat.getLista_representantes().get(i).getLista_procesos().get(j).isRepresentativo()) suma_r++;
                    if(chat.getLista_representantes().get(i).getLista_procesos().get(j).isCotidiano()) suma_c++;
                }
                System.out.println(chat.getLista_representantes().get(i).getNombre_usuario());
                System.out.println("Tiempo descanso: " + chat.getLista_representantes().get(i).getNombre_usuario());
                System.out.println("Representativos: " + suma_r);
                System.out.println("Cotidianos: " + suma_c);
                System.out.println("Total procesos: " + chat.getLista_representantes().get(i).getLista_procesos().size());
                System.out.println();
                suma_c = 0;
                suma_r = 0;
            }
            
            /*
            System.out.println(chat.calcular_tiempo_preparacion_equipo(porcentaje_dias_representativo, "hsp_v_1")/60);
            System.out.println(chat.calcular_tiempo_preparacion_equipo(porcentaje_dias_representativo, "hsp_v_3")/60);
            System.out.println(chat.calcular_tiempo_preparacion_equipo(porcentaje_dias_representativo, "equipo")/60);
            */
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
