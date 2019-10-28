
package panoptico.clases;

import java.io.IOException;
import java.text.ParseException;
import javax.swing.JOptionPane;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args) throws ParseException, IOException{
        
        try{
            Equipo chat = new Equipo("TMC REDUCIDO.xlsx", 95, 90, 5, 95, 95, 25);
            
            chat.getRepresentantes().get(1).simular_dia_representante(1);
            
            for(int i = 0; i < chat.getRepresentantes().get(1).getSimulacion_dia().length; i++){
                System.out.println("Rango de " + i + " a " + (i+1) + " minutos: " + chat.getRepresentantes().get(1).getSimulacion_dia()[i]*100 + " % ");
            }
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
