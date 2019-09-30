
package panoptico.clases;

import java.io.IOException;
import javax.swing.JOptionPane;

/**

* @author montenegro
 */
public class Principal {

    public static void main(String[] args){
        try{
          
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
