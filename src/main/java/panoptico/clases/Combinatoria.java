
package panoptico.clases;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class Combinatoria {
    
    // Atributos

    public String[][] matriz_combinaciones_sin_repeticion; // Es una matriz que contiene todas las combinaciones sin repeticion
    public int[][] matriz_suma_indices; // Es una matriz que tiene la suma de todas las combinaciones sin repeticion
    
    // Constructor

    public Combinatoria(int[] indices_a_sumar) {
        matriz_combinaciones_sin_repeticion = new String[indices_a_sumar.length-1][];
        matriz_suma_indices = new int[indices_a_sumar.length-1][];
        for (int i = 0; i < matriz_combinaciones_sin_repeticion.length; i++) {
            combinaciones_sin_repeticion_numeros_elegidos(indices_a_sumar.length, i+1, i);
        }
        llenar_matriz_suma_indices(indices_a_sumar);
    }
    
    // Devuelve la suma de los valores de una lista de todas las posiciones indicadas en un arreglo de enteros
   
    public void llenar_matriz_suma_indices(int[] indices_a_sumar){
        for (int i = 0; i < matriz_combinaciones_sin_repeticion.length; i++) {
            for (int j = 0; j < matriz_combinaciones_sin_repeticion[i].length; j++) {
                matriz_suma_indices[i][j] = sumar_indices_combinacion_sin_repeticion(matriz_combinaciones_sin_repeticion[i][j] , indices_a_sumar);
            }
        }
    }
    
    //
   
    public int sumar_indices_combinacion_sin_repeticion(String cadena, int[] indices_a_sumar){
        if(indice_inicial_subcadena(cadena) == 1){
            return indices_a_sumar[ultimo_numero_cadena(cadena)-1];
        }else{
            return indices_a_sumar[ultimo_numero_cadena(cadena)-1] + sumar_indices_combinacion_sin_repeticion(cadena.substring(0, indice_inicial_subcadena(cadena)-1), indices_a_sumar);
        }
    }
    
    // Genera todas las combinaciones sin repeticion de una cantidad de numeros elegidos
   
    public void combinaciones_sin_repeticion_numeros_elegidos(int numeros, int numeros_elegidos, int indice_matriz){
        matriz_suma_indices[indice_matriz] = new int[(int)combinacion_sin_repeticion(numeros, numeros_elegidos)];
        matriz_combinaciones_sin_repeticion[indice_matriz] = new String[(int)combinacion_sin_repeticion(numeros, numeros_elegidos)];
        matriz_combinaciones_sin_repeticion[indice_matriz][0] = ".1";
        for (int i = 2; i <= numeros_elegidos; i++) {
            matriz_combinaciones_sin_repeticion[indice_matriz][0] += ("." + i);
        }
        for (int i = 1; i < matriz_combinaciones_sin_repeticion[indice_matriz].length; i++) {
            matriz_combinaciones_sin_repeticion[indice_matriz][i] = siguiente_combinacion_sin_repeticion(matriz_combinaciones_sin_repeticion[indice_matriz][i-1], numeros);
        }
    }
    
    // Devuelve la siguiente combinacion sin repeticion
           
    public String siguiente_combinacion_sin_repeticion(String cadena, int numeros_combinados){
        if(ultimo_numero_cadena(cadena) < numeros_combinados){
            cadena = cadena.substring(0, indice_inicial_subcadena(cadena)) + (ultimo_numero_cadena(cadena)+1);
            return cadena;
        }else{
            cadena = siguiente_combinacion_sin_repeticion(cadena.substring(0, indice_inicial_subcadena(cadena)-1), numeros_combinados-1);
            return cadena + '.'  + (ultimo_numero_cadena(cadena)+1);
        }
    }
    
    // Devuelve el ultimo numero de una cadena de caracteres
   
    public int ultimo_numero_cadena(String cadena){
        return Integer.parseInt(cadena.substring(indice_inicial_subcadena(cadena), cadena.length()));
    }
    
    // Indica cual es el indice del primer numero de derecha a izquierda
   
    public int indice_inicial_subcadena(String subcadena){
        if(subcadena.charAt(subcadena.length()-1) == '.'){
            return subcadena.length();
        }else{
            return indice_inicial_subcadena(subcadena.substring(0, subcadena.length()-1));
        }
    }
    
    // Numero de combinaciones sin repeticion de un conjunto de n numeros con n elegidos

    public double combinacion_sin_repeticion(int numeros, int numeros_elegidos){
        return permutacion_sin_repeticion(numeros, numeros_elegidos) / factorial(numeros_elegidos);
    }
    
    // Numero de permutaciones sin repeticion de un conjunto de n numeros con n elegidos

    public double permutacion_sin_repeticion(int numeros, int numeros_elegidos){
        if((numeros_elegidos < 1) || (numeros_elegidos > numeros) || (numeros < 1)){
            return 1;
        }else{
            return numeros * permutacion_sin_repeticion(numeros-1, numeros_elegidos-1);
        }
    }
   
    // Factorial de un numero

    public double factorial(int numero){
        if(numero < 2){
            return 1;
        }else{
            return numero * factorial(numero-1);
        }
    }
    
    // Getters and Setters

    public String[][] getMatriz_combinaciones_sin_repeticion() {
        return matriz_combinaciones_sin_repeticion;
    }

    public void setMatriz_combinaciones_sin_repeticion(String[][] matriz_combinaciones_sin_repeticion) {
        this.matriz_combinaciones_sin_repeticion = matriz_combinaciones_sin_repeticion;
    }

    public int[][] getMatriz_suma_indices() {
        return matriz_suma_indices;
    }

    public void setMatriz_suma_indices(int[][] matriz_suma_indices) {
        this.matriz_suma_indices = matriz_suma_indices;
    }

}
