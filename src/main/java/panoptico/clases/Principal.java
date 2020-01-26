
package panoptico.clases;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Principal {
    
    static void busqueda_filtrada(int cantidad_datos, int[] filtros, ArrayList<Caso> arreglo){
        ArrayList<Caso> arreglo_cantidad_datos = new ArrayList<>();
        int longitud_arreglo = cantidad_datos/8;
        int indice = 0;
        for (int i = 0; i < longitud_arreglo; i++) {
            if(indice >= arreglo.size()-1){
                indice = 0;
            }else{
                indice++ ;
            }
            arreglo_cantidad_datos.add(arreglo.get(indice)); 
        }
        HashMap mapa_busqueda = Equipo.filtrar_mapa_busqueda(arreglo_cantidad_datos, filtros);
        ArrayList<Object> objetos_buscados = new ArrayList<>();
        objetos_buscados = llenar_objetos_buscados(objetos_buscados, mapa_busqueda, filtros.length);
        long inicio = System.nanoTime();
        long fin = realizar_busqueda(objetos_buscados, mapa_busqueda, filtros.length);
        System.out.println("Mostrando resultados prueba...\n---------------------------------------------------");
        System.out.println("NOMBRE FUNCIONALIDAD: Busqueda filtrada");
        System.out.print("TIPO ESTRUCTURA DATOS: HashMap");
        switch (filtros.length) {
            case 1:
                System.out.println(" simple");
                break;
            case 2:
                System.out.println(" doblemente anidado");
                break;
            case 3:
                System.out.println(" triplemente anidado");
                break;
            case 4:
                System.out.println(" cuadruplemente anidado");
                break;
            case 5:
                System.out.println(" quintuplemente anidado");
                break;
            case 6:
                System.out.println(" sextuplemente anidado");
                break;
        }
        System.out.println("CANTIDAD DATOS PROBADOS: " + cantidad_datos);
        System.out.println("NOTACION BIG O: O(1)");
        System.out.println("TIPO OBJETO DEVUELTO: ArrayList<Caso>");
        System.out.println("VALORES ALEATORIOS USADOS EN LA BUSQUEDA: " + objetos_buscados);
        System.out.println("TIEMPO EJECUCION: " + (fin-inicio) + " nanosegundos");
        System.out.println("---------------------------------------------------");
    }

    static ArrayList<Object> llenar_objetos_buscados(ArrayList<Object> objetos_buscados, HashMap mapa_busqueda, int numero_busquedas){
        mapa_busqueda.keySet().toArray();
        int aleatorio = (int) (Math.random()*mapa_busqueda.size()+1)-1;
        Object llave = mapa_busqueda.keySet().toArray()[aleatorio];
        objetos_buscados.add(llave);
        if(numero_busquedas > 1){
            return llenar_objetos_buscados(objetos_buscados, (HashMap) mapa_busqueda.get(llave), numero_busquedas-1);
        }else{
            return objetos_buscados;
        }
    }
    
    static long realizar_busqueda(ArrayList<Object> objetos_buscados, HashMap mapa_busqueda, int numero_busquedas){
        if(numero_busquedas > 1){
            return realizar_busqueda(objetos_buscados, (HashMap) mapa_busqueda.get(objetos_buscados.get(0)), numero_busquedas-1);
        }else{
            return System.nanoTime();
        }
    }

    public static void main(String[] args) throws IOException {
        int tiempo_minimo_caso_admitido = 120;
        int representatividad_representante = 40;
        int percentil_tiempo_futuro_estado = 95;
        int percentil_procesos = 95;
        int percentil_inferior_promedios = 5;
        int percentil_superior_promedios = 95;
        
        System.out.println("EJECUTANDO PANOPTICO");
        System.out.println("Accediendo a la base de datos... ");
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
                if(busqueda.startsWith("simulacion jornada equipo")){
                    for (int i = 0; i < ((double[])buscado).length; i++) {
                        System.out.println("Probabilidad de salir del minuto " + i + " al minuto " + (i+1) + " : " + formato.format(((double[])buscado)[i]*100) + " % ");
                    }
                }
            }else if(busqueda.length() > 5){
                System.out.println("---------------------------------------------------");
                if(busqueda.equals("instrucciones")){
                    System.out.println("Puedes realizar busquedas de los siguientes datos o ejecutar alguna de las opciones presentadas a continuacion: ");
                    System.out.println("- Tiempo futuro estado (busqueda)\n- Tiempo medio conversacion (busqueda)\n- Representatividad (busqueda)\n- Simulacion jornada equipo (busqueda)");
                    System.out.println("- Ver datos (ejecutable)\n- Ver posibles busquedas (ejecutable)\n- Realizar pruebas (ejecutable)\n- Configuracion (ejecutable)");
                    System.out.println("Ejemplo: Prueba escribiendo en el buscador <tiempo futuro estado equipo> o <Configuracion>");
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
                if(busqueda.equals("ver datos")){
                    System.out.println("A continuacion se muestran todos los datos con los que cuenta el programa: \n");
                    chat.mostrar_arreglo_consola(chat.getCasos());
                    System.out.println("\nEl programa cuenta con un total de " + chat.getCasos().size() + " casos filtrados y " + (chat.getCasos().size()*8) + " datos filtrados.");
                }
                if(busqueda.equals("ver posibles busquedas")){
                    System.out.println("A continuacion se muestran todas las posibles busquedas que se pueden realizar en el programa: \n");
                    for (String llave : chat.getGlosario_busquedas().keySet()) {
                        System.out.println("- " + llave);
                    }
                }
                if(busqueda.equals("realizar pruebas")){
                    System.out.println("Los filtros que se pueden utilizar son los siguientes (no se puede repetir filtro): ");
                    System.out.println("- 1 Skill\n- 2 Representante\n- 3 Proceso\n- 4 Fecha\n- 7 Lider\n- 8 Turno");
                    System.out.print("Ingrese por favor el numero de filtros que desea utilizar para esta prueba: ");
                    int numero_filtros = lectura.nextInt();
                    int[] filtros = new int[numero_filtros];
                    for (int i = 0; i < numero_filtros; i++) {
                        System.out.print("Ingrese un numero de filtro: ");
                        filtros[i] = lectura.nextInt();
                    }
                    System.out.print("Ingrese la cantidad de datos que desea evaluar: ");
                    busqueda_filtrada(lectura.nextInt(), filtros, chat.getCasos());
                }
            }
        }
        System.out.println("Gracias por usar PANOPTICO.");
    }
    
}
