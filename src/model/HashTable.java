/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Clase que implementa tabla hash usando sondeo lineal para colisiones.
 * Admite cadenas como claves y da el valor hash.
 * La tabla hash tiene tamaño fijo (10 posiciones) y encuentra el indice por medio
 * de una funcion hash.
 * @author maryori
 */

public class HashTable {
    private static final int TABLE_SIZE = 10;
    private String[] keys;
    private int[] hashValues; // valores hash calculados

    /**
     * Constructor que inicializa la tabla con tamaño fijo y espacios vacios.
     */
    public HashTable() {
        keys = new String[TABLE_SIZE];
        hashValues = new int[TABLE_SIZE];
    }

    /**
     * Funcion hash que convierte la clave en indice dentro del rango de la tabla (10p).
     * @param key Clave a convertir en indice.
     * @return Indice hash dentro de los limites de la tabla.
     */
    public int hashFunction(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (31 * hash + key.charAt(i)) % TABLE_SIZE;
        }
        return Math.abs(hash); // Aseguro que sea positivo
    }

    /**
     * Inserta la clave en la tabla. Si ya existe, la omite.
     * Sy hay colision, usa sondeo lineal para encontrar la siguiente posicion.
     * @param key Clave a insertar
     */
    public void put(String key) {
        if (key == null || key.trim().isEmpty()) return;
        
        int hashValue = hashFunction(key);
        int index = hashValue;
        int originalIndex = index;
        
        // Manejo de colisiones con sondeo lineal
        while (keys[index] != null && !keys[index].equals(key)) {
            index = (index + 1) % TABLE_SIZE;
            if (index == originalIndex) {
                // Tabla llena
                return;
            }
        }
        
        keys[index] = key;
        hashValues[index] = hashValue; // Almacenamos el valor hash calculado
    }

    /**
     * Devuelve el valor hash de una clave que esta almacenada.
     * @param key Clave buscada.
     * @return Valor hash si se enceutnre la clave, - si no existe.
     */
    public int getHashValue(String key) {
        int index = hashFunction(key);
        int originalIndex = index;
        
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                return hashValues[index];
            }
            index = (index + 1) % TABLE_SIZE;
            if (index == originalIndex) {
                break;
            }
        }
        
        return -1; // No encontrado
    }

    /**
     * Devuelve el arreglo de claves guardadas.
     * @return Arrego de cadenas que contiene las claves en la tabla.
     */
    public String[] getKeys(){ 
        return keys; 
    }
    /**
     * Devuelve el arreglo de valores hash de cada clave guardada.
     * @return Arreglo de enteror con valores hash.
     */
    public int[] getHashValues(){ 
        return hashValues; 
    }
    /**
     * Devuelve el tamaño fijo de la tabla.
     * @return Tamaño total de la tabla (10 fijos).
     */
    public int getSize(){ 
        return TABLE_SIZE; 
    }
}