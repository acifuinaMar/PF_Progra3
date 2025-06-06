/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.HashTable;
/**
 * Controlador que gestiona las operaciones de la tabla Hash.
 * Se comunica con la clase {@code HashTable} para insertar y obtener datos.
 * @author maryori
 */
public class HashTableController {
    private HashTable hashTable;
    /**
     * Constructor que inicializa una nueva instancia de la tabla Hash.  
     */
    public HashTableController() {
        this.hashTable = new HashTable();
    }
    /**
     * Agrega una nueva clave a la tabla hash.
     * @param key Clave que se va a insertar .
     */
    public void addKey(String key) {
        hashTable.put(key);
    }

    /**
     * Obtiene el valor hash asociado a una clave.
     * @param key Clave de la que quiero el valor hash.
     * @return Valor hash calculado.
     */
    public int getHashValue(String key) {
        return hashTable.getHashValue(key);
    }

    /**
     * Regresa todas las claves de la tabla Hash.
     * @return Arreglo string con todas las claves.
     */
    public String[] getKeys() {
        return hashTable.getKeys();
    }

    /**
     * Regresa los valores de las claves guardadas.
     * @return Arreglo de enteros con valores hash.
     */
    public int[] getHashValues() {
        return hashTable.getHashValues();
    }

    /**
     * Obtiene el tamaño actual de la tabla
     * @return Numero de elementos en la tabla.
     */
    public int getTableSize() {
        return hashTable.getSize();
    }
}