/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 * Clase que modela una celda en una hoja de calculo.
 * Esta enlazada con celdas vecinas (arriba, abajo, izquierda y derecha).
 * Puede contener texto, formula o numero.
 * @author maryori
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 4L;
    private String content;
    private String formula;
    private Object value;
    private CellType type;
    private Cell up, down, left, right;

    /**
     * Constructor que inicializa la celda vacia.
     */
    public Cell() {
        this.content = "";
        this.formula = "";
        this.value = "";
        this.type = CellType.EMPTY;
    }

    /**
     * Obtiene el contenido de la celda.
     * @return Texto de la celda.
     */
    public String getContent(){ 
        return content; 
    }
    /**
     * Establece el contenido de la celda.
     * @param content Texto a almacenar.
     */
    public void setContent(String content){ 
        this.content = content; 
    }
    /**
     * Obtiene la formula escrita en la celda (si es que hay una).
     * @return Formula como cadena String.
     */
    public String getFormula(){ 
        return formula; 
    }
    /**
     * Establece una formula en una celda.
     * @param formula Cadena de formula.
     */
    public void setFormula(String formula){ 
        this.formula = formula; 
    }
    /**
     * Obtiene el valor de la celda.
     * @return Valor de la celda actual.
     */
    public Object getValue(){ 
        return value; 
    }
    /**
     * Establece el valor evaluado de la celda.
     * @param value Valor que se mostrara o usara para calculos.
     */
    public void setValue(Object value){ 
        this.value = value; 
    }
    /**
     * Obtiene la celda vecina de arriba.
     * @return Referencia de la celda de arriba.
     */
    public Cell getUp(){ 
        return up; 
    }
    /**
     * Establece la celda vecina de arriba.
     * @param up Celda de arriba.
     */
    public void setUp(Cell up){ 
        this.up = up; 
    }
    /**
     * Obtiene la celda vecina de abajo.
     * @return Referencia de la celda de abajo.
     */
    public Cell getDown(){ 
        return down; 
    }
    /**
     * Establece la celda vecina de abajo.
     * @param down Celda de abajo.
     */
    public void setDown(Cell down){ 
        this.down = down; 
    }
    /**
     * Obtiene la celda vecina de la izquierda.
     * @return Referencia de la celda de la izquierda.
     */
    public Cell getLeft(){ 
        return left; 
    }
    /**
     * Establece la celda vecina de la izquierda.
     * @param left Celda de la izquierda.
     */
    public void setLeft(Cell left){ 
        this.left = left; 
    }
    /**
     * Obtiene la celda vecina de la derecha.
     * @return Referencia de la celda de la derecha.
     */
    public Cell getRight(){ 
        return right; 
    }
    /**
     * Establece la celda vecina de la derecha.
     * @param right Celda de la derecha.
     */
    public void setRight(Cell right){ 
        this.right = right; 
    }
    /**
     * obtiene el tipo de celda (texto, numero, formula, vacia).
     * @return Tipo de la celda.
     */
    public CellType getType(){ 
        return type; 
    }
    /**
     * Establece el tipo de celda.
     * @param type Tipo de celda.
     */
    public void setType(CellType type){
        this.type = type;
    }
}
