
package panoptico.clases;

import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args){
        
        try{
            Equipo chat = new Equipo("TMC.xlsx", 95, 90, 5, 95, 95, 25);
           
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
