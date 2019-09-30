
package panoptico.clases;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Simulaciones {
    
    // Factorial de un numero

    static double factorial(int numero){
        if(numero < 2){
            return 1;
        }else{
            return numero * factorial(numero-1);
        }
    }

    // Numero de permutaciones sin repeticion de un conjunto de n numeros con n elegidos

    static double permutacion_sin_repeticion(int numeros, int numeros_elegidos){
        if((numeros_elegidos < 1) || (numeros_elegidos > numeros) || (numeros < 1)){
            return 1;
        }else{
            return numeros * permutacion_sin_repeticion(numeros-1, numeros_elegidos-1);
        }
    }

    // Numero de combinaciones sin repeticion de un conjunto de n numeros con n elegidos

    static double combinacion_sin_repeticion(int numeros, int numeros_elegidos){
        return permutacion_sin_repeticion(numeros, numeros_elegidos) / factorial(numeros_elegidos);
    }

    // Verifica si en un arreglo se cumple que los elementos van de menor a mayor desde el indice 0

    static boolean verificar_orden_arreglo_menor_a_mayor(int[] arreglo_numeros){
        for (int i = 0; i < arreglo_numeros.length-1; i++) {
            if(arreglo_numeros[i] > arreglo_numeros[i+1]){
                return false;
            }
        }
        return true;
    }
   
    // Devuelve la siguiente combinacion sin repeticion a partir de una combinacion sin repeticion de n numeros sobre un total de m numeros
   
    static int[] siguiente_combinacion_sin_repeticion(int[] combinacion_sin_repeticion, int numeros_que_se_combinan){
        int[] siguiente_combinacion = new int[combinacion_sin_repeticion.length];
        System.arraycopy(combinacion_sin_repeticion, 0, siguiente_combinacion, 0, combinacion_sin_repeticion.length);
        if(verificar_orden_arreglo_menor_a_mayor(combinacion_sin_repeticion) && (combinacion_sin_repeticion[combinacion_sin_repeticion.length-1] <= numeros_que_se_combinan)){
            for(int i = siguiente_combinacion.length-1; i >= 0; i--){
                if(siguiente_combinacion[i] < numeros_que_se_combinan-(siguiente_combinacion.length-(i+1))){
                    siguiente_combinacion[i] += 1;
                    for(int j = i+1; j < siguiente_combinacion.length; j++){
                        siguiente_combinacion[j] = siguiente_combinacion[i]+j-i;
                    }
                    return siguiente_combinacion;
                }
            }
            return siguiente_combinacion;
        }else{
            for(int i = 0; i < siguiente_combinacion.length; i++){
                siguiente_combinacion[i] = i+1;
            }
            return siguiente_combinacion;
        }
    }
    
}
