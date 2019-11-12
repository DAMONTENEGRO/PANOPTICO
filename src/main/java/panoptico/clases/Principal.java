
package panoptico.clases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args) throws IOException {
        Equipo chat = new Equipo(120, 25, 95);
        int[] i = {8, 7, 2};
        HashMap mapita = chat.filtrar_mapa_busqueda(chat.getCasos(), i);
        
        for (Object llave : mapita.keySet()) {
            System.out.println("PRIMER FILTRO: " + llave);
            for (Object sub_llave : ((HashMap) mapita.get(llave)).keySet()) {
                System.out.println("SEGUNDO FILTRO: " + sub_llave);
                for (Object sub_sub_llave : ((HashMap) ((HashMap) mapita.get(llave)).get(sub_llave)).keySet()) {
                    System.out.println("TERCER FILTRO: " + sub_sub_llave);
                    chat.mostrar_arreglo_consola((ArrayList<Caso>) ((HashMap) ((HashMap) mapita.get(llave)).get(sub_llave)).get(sub_sub_llave));
                }
            }
        }
       
        /*
        for (Object llave : mapita.keySet()) {
            for (Object sub_llave : ((HashMap) mapita.get(llave)).keySet()) {
                chat.mostrar_arreglo_consola((ArrayList<Caso>) ((HashMap) mapita.get(llave)).get(sub_llave));
            }
        }
        
        /*
        HashMap<Object, ArrayList<Caso>> mapita = chat.crear_mapa_busqueda(chat.getCasos(), 2);
        for (Object llave : mapita.keySet()) {
            chat.mostrar_arreglo_consola(mapita.get(llave));
        }
 */
    }
    
}
