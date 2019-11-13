
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
        int tiempo_minimo_caso_admitido = 120;
        int percentil_tiempo_futuro_estado = 95;
        Equipo chat = new Equipo(tiempo_minimo_caso_admitido, percentil_tiempo_futuro_estado);
        
        /*
        int[] i = {8, 7, 2};
        HashMap mapita = chat.filtrar_mapa_busqueda(chat.getCasos(), i);
        
        for (Object llave : mapita.keySet()) {
            System.out.println("PRIMER FILTRO: " + llave);
            for (Object sub_llave : ((HashMap) mapita.get(llave)).keySet()) {
                //System.out.println("SEGUNDO FILTRO: " + sub_llave);
                for (Object sub_sub_llave : ((HashMap) ((HashMap) mapita.get(llave)).get(sub_llave)).keySet()) {
                    //System.out.println("TERCER FILTRO: " + sub_sub_llave);
                    //chat.mostrar_arreglo_consola((ArrayList<Caso>) ((HashMap) ((HashMap) mapita.get(llave)).get(sub_llave)).get(sub_sub_llave));
                }
            }
        }
        */
        System.out.println("----------------------------------------------");
        for (String llave : chat.getGlosario_busquedas().keySet()) {
            System.out.println(llave + " : " + chat.getGlosario_busquedas().get(llave) + " (tiempo en segundos)");
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
