
package panoptico.clases;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {

    public static void main(String[] args) throws IOException {
        int tiempo_minimo_caso_admitido = 120;
        int representatividad_representante = 40;
        int percentil_tiempo_futuro_estado = 95;
        int percentil_procesos = 95;
        int percentil_inferior_promedios = 5;
        int percentil_superior_promedios = 95;
        
        System.out.println("EJECUTANDO PANOPTICO");
        System.out.println("Cargando la aplicacion... ");
        Equipo chat = new Equipo(tiempo_minimo_caso_admitido, representatividad_representante, percentil_tiempo_futuro_estado, percentil_procesos, percentil_inferior_promedios, percentil_superior_promedios);
        
        // Buscador de consola
        
        Scanner lectura = new Scanner(System.in);
        boolean salir = false;
        String busqueda; 
        Object buscado;
        System.out.println("\n¡Bienvenido al buscador de consola!");
        System.out.println("Aqui puedes escribir y buscar cada uno de los datos del programa, para salir solo escribe <Salir> en el buscador \no solicita ayuda escribiendo la palabra <Instrucciones> en el buscador.");
        while(!salir){
            System.out.print("\n¿Que te gustaria buscar?: ");
            busqueda = lectura.nextLine().toLowerCase();
            if(busqueda.equals("salir")){
                salir = true;
            }else if(chat.getGlosario_busquedas().keySet().contains(busqueda)){
                System.out.println("---------------------------------------------------");
                buscado = chat.getGlosario_busquedas().get(busqueda);
                DecimalFormat formato = new DecimalFormat("#.0000");
                if(busqueda.startsWith("tiempo futuro estado")) System.out.println(formato.format((double)buscado/60) + " minutos con " + (100-percentil_tiempo_futuro_estado) + "% margen de error");
                if(busqueda.startsWith("tiempo medio conversacion")) System.out.println(formato.format((double)buscado/60) + " minutos calculado entre los percentiles " + percentil_inferior_promedios + " y " + percentil_superior_promedios);
                if(busqueda.startsWith("representatividad")) System.out.println(formato.format((double)buscado) + " % ");
                if(busqueda.startsWith("simulacion jornada")){
                    for (int i = 0; i < ((double[])buscado).length; i++) {
                        System.out.println("Probabilidad de salir del minuto " + i + " al minuto " + (i+1) + " : " + formato.format(((double[])buscado)[i]*100) + " % ");
                    }
                }
            }else{
                System.out.println("---------------------------------------------------");
                if(busqueda.equals("instrucciones")){
                    System.out.println("Puedes realizar busquedas de los siguientes datos: " + "\n- Tiempo futuro estado");
                    System.out.println("- Tiempo medio conversacion\n- Representatividad\n- Simulacion jornada\n- Configuracion");
                    System.out.println("Ejemplo: Prueba escribiendo en el buscador <tiempo futuro estado equipo>");
                }
                if(busqueda.equals("configuracion")){
                    System.out.println("La configuracion actual del programa es la siguiente: ");
                    System.out.println("- Tiempo minimo para contabilizar un caso: " + tiempo_minimo_caso_admitido + "Sg (De no cumplir con esta duracion se tomara como un drop)");
                    System.out.println("- Representatividad representante: " + representatividad_representante + "% Si el representante cumple con este porcentaje de dias trabajados sera tenido en cuenta por el programa");
                    System.out.println("- Porcentaje calculo tiempo futuro estado: " + percentil_tiempo_futuro_estado + "% Este porcentaje indica cuantos representantes saldran en un tiempo igual o menor al calculado");
                    System.out.println("- Porcentaje diagrama de Pareto procesos: " + percentil_procesos + "% Es el procentaje con el que se realiza el diagrama de Pareto");
                    System.out.println("- Percentil inferior promedios: " + percentil_inferior_promedios + " Es el percentil desde el cual se empieza a calcular un promedio");
                    System.out.println("- Percentil superior promedios: " + percentil_superior_promedios + " Es el percentil hasta el cual se termina de calcular un promedio");
                }
            }
        }
        System.out.println("Gracias por usar PANOPTICO.");
    }
    
}
