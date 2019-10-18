
package panoptico.clases;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Combinatoria {
    
    // Atributos

    private byte[][] combinaciones_sin_repeticion; // Es una matriz tridimensional que contiene 
    
    // Constructor

    public Combinatoria(byte numeros, int numeros_elegidos) {
        if((numeros_elegidos > 0) && (numeros_elegidos <= numeros)){
            combinaciones_sin_repeticion = new byte[(int) combinacion_sin_repeticion(numeros, numeros_elegidos)][numeros_elegidos];
            byte[] combinacion_por_fila = new byte[numeros_elegidos];
            for (int i = 0; i < combinacion_por_fila.length; i++) {
                combinacion_por_fila[i] = (byte) (i+1);
            }
            combinaciones_sin_repeticion[0] = combinacion_por_fila;
            for (int i = 1; i < combinaciones_sin_repeticion.length; i++) {
                combinaciones_sin_repeticion[i] = siguiente_combinacion_sin_repeticion(combinacion_por_fila, numeros);
                combinacion_por_fila = siguiente_combinacion_sin_repeticion(combinacion_por_fila, numeros);
            } 
        }else{
            combinaciones_sin_repeticion = new byte[1][1];
        }
    }
    
    // Devuelve la siguiente combinacion sin repeticion a partir de una combinacion sin repeticion de n numeros sobre un total de m numeros
   
    static byte[] siguiente_combinacion_sin_repeticion(byte[] combinacion_sin_repeticion, byte numeros){
        byte[] siguiente_combinacion = new byte[combinacion_sin_repeticion.length];
        System.arraycopy(combinacion_sin_repeticion, 0, siguiente_combinacion, 0, combinacion_sin_repeticion.length);
        if(verificar_orden_arreglo_menor_a_mayor(combinacion_sin_repeticion) && (combinacion_sin_repeticion[combinacion_sin_repeticion.length-1] <= numeros)){
            for(int i = siguiente_combinacion.length-1; i >= 0; i--){
                if(siguiente_combinacion[i] < numeros-(siguiente_combinacion.length-(i+1))){
                    siguiente_combinacion[i] += 1;
                    for(int j = i+1; j < siguiente_combinacion.length; j++){
                        siguiente_combinacion[j] = (byte) (siguiente_combinacion[i]+j-i);
                    }
                    return siguiente_combinacion;
                }
            }
            return siguiente_combinacion;
        }else{
            for(int i = 0; i < siguiente_combinacion.length; i++){
                siguiente_combinacion[i] = (byte) (i+1);
            }
            return siguiente_combinacion;
        }
    }
    
    // Verifica si en un arreglo se cumple que los elementos van de menor a mayor desde el indice 0

    static boolean verificar_orden_arreglo_menor_a_mayor(byte[] arreglo_numeros){
        for (int i = 0; i < arreglo_numeros.length-1; i++) {
            if(arreglo_numeros[i] > arreglo_numeros[i+1]){
                return false;
            }
        }
        return true;
    }
    
    // Numero de combinaciones sin repeticion de un conjunto de n numeros con n elegidos

    static double combinacion_sin_repeticion(int numeros, int numeros_elegidos){
        return permutacion_sin_repeticion(numeros, numeros_elegidos) / factorial(numeros_elegidos);
    }
    
    // Numero de permutaciones sin repeticion de un conjunto de n numeros con n elegidos

    static double permutacion_sin_repeticion(int numeros, int numeros_elegidos){
        if((numeros_elegidos < 1) || (numeros_elegidos > numeros) || (numeros < 1)){
            return 1;
        }else{
            return numeros * permutacion_sin_repeticion(numeros-1, numeros_elegidos-1);
        }
    }
    
    // Factorial de un numero

    static double factorial(int numero){
        if(numero < 2){
            return 1;
        }else{
            return numero * factorial(numero-1);
        }
    }
    
    // Getters and Setters

    public byte[][] getCombinaciones_sin_repeticion() {
        return combinaciones_sin_repeticion;
    }

    public void setCombinaciones_sin_repeticion(byte[][] combinaciones_sin_repeticion) {
        this.combinaciones_sin_repeticion = combinaciones_sin_repeticion;
    }

}
