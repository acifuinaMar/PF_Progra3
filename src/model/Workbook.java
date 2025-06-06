/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Clase que representa un libro formado de una lista de hojas. 
 * Cada hoja se maneja como una matriz ortogonal de celdas.
 * @author maryori
 */

import java.io.Serializable;
import java.util.LinkedList;

public class Workbook implements Serializable{
    private static final long serialVersionUID = 1L;
    private LinkedList<Sheet> sheets;
    private int currentSheetIndex;

    /**
     * Constructor que inicializa el libro con una hoja "Hoja 1".
     */
    public Workbook() {
        this.sheets = new LinkedList<>();
        this.sheets.add(new Sheet("Hoja 1"));
        this.currentSheetIndex = 0;
    }

    /**
     * Agrega una nueva hoja al libro con el nombre dado por el usuario.
     * @param name Nombre de la nueva hoja.
     */
    public void addSheet(String name) {
        sheets.add(new Sheet(name));
    }

    /**
     * Retorna la hoja en la que estamos trabajando actualmente.
     * @return Hoja seleccionada actualmente.
     */
    public Sheet getCurrentSheet() {
        return sheets.get(currentSheetIndex);
    }

    /**
     * REgresa la lista completa de hojas en el libro.
     * @return Lista de hojas.
     */
    public LinkedList<Sheet> getSheets(){ 
        return sheets; 
    }
    /**
     * Establece el indice de la hoja actual/activa
     * @param index Indice de la hoja que se quiere activar.
     */
    public void setCurrentSheetIndex(int index){ 
        this.currentSheetIndex = index; 
    }
    /**
     * regresa el indice de la hoja activa.
     * @return Indice de la hoja activa.
     */
    public int getCurrentSheetIndex(){ 
        return currentSheetIndex; 
    }
}
