
package panoptico.clases;

import java.io.IOException;
import javax.swing.JOptionPane;

/**

* @author montenegro
 */
public class Principal {

    public static void main(String[] args){
        try{
            Equipo chat = new Equipo("TMC.xlsx", 95, 5, 5, 95, 95);
            for (int i = 0; i < chat.getLista_representantes().size(); i++) {
                System.out.println(chat.getLista_representantes().get(i).getNombre_usuario().toUpperCase());
                System.out.println("Tiempo preparacion: " + chat.getLista_representantes().get(i).getTiempo_preparacion()/60);
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
