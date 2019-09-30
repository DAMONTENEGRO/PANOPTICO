
package panoptico.clases;

import java.io.IOException;
import javax.swing.JOptionPane;

/**

* @author montenegro
 */
public class Principal {

    public static void main(String[] args){
        try{
            Equipo equipo = new Equipo("TMC.xlsx", 95);
            equipo.crear_equipo();
            double suma = 0;
            for (int i = 0; i < equipo.getLista_representantes().size(); i++) {
                System.out.println(equipo.getLista_representantes().get(i).getNombre_usuario().toUpperCase());
                System.out.println("Tiempo de preparacion para descanso: " + equipo.getLista_representantes().get(i).tiempo_promedio_general_representante_rango(5, 95)/60 + " minutos");
                suma += equipo.getLista_representantes().get(i).tiempo_promedio_general_representante_rango(5, 95);
            }
            System.out.println("\nEQUIPO\n" + "Tiempo promedio de preparacion todos los representantes: " + (suma/equipo.getLista_representantes().size())/60 + " minutos");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
