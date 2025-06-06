/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 * Clase que representa la hoja en el libro. 
 * Cada hoja tiene un nombre y una matriz con celdas.
 * La clase contiene matrices editables y permite operaciones entre las celdas
 * de una misma matriz.
 * @author maryori
 */

public class Sheet implements Serializable{
    private static final long serialVersionUID = 2L;
    private String name;
    private OrthogonalMatrix matrix;

    /**
     * Constructor que inicializa la hoja con nombre y matriz vacia.
     * @param name Nombre de la hoja.
     */
    public Sheet(String name) {
        this.name = name;
        this.matrix = new OrthogonalMatrix();
    }

    /**
     * Regresa el nombre de la hoja.
     * @return Nombre de la hoja.
     */
    public String getName(){ 
        return name; 
    }
    /**
     * Retorna la matriz ortogonal asociada a la hoja.
     * @return Instancia de OrthogonalMatriz.
     */
    public OrthogonalMatrix getMatrix(){ 
        return matrix; 
    }
}
