/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase que representa una matriz ortogonal de celdas. 
 * Acepta numeros, texto y formulas. Las celdas estan enlazadas a sus vecinas.
 * @author maryori
 */
package model;

import java.io.Serializable;
import util.FormulaParser;

public class OrthogonalMatrix implements Serializable {
    private static final long serialVersionUID = 3L;
    private Cell[][] matrix;
    private int rows;
    private int cols;
    private transient FormulaParser formulaParser;

    /**
     * Constructor que crea una matriz de 20x20 de celdas enlazadas.
     */
    public OrthogonalMatrix() {
        this.rows = 20; // Tamaño fijo inicial
        this.cols = 20;
        this.matrix = new Cell[rows][cols];
        initializeMatrix();
    }

    /**
     * Establece el analizador de formulas para las expresiones
     * @param parser Instancia de FormulaParser que evalua formulas.
     */
    public void setFormulaParser(FormulaParser parser) {
        this.formulaParser = parser;
    }
    
    /**
     * Inicializa la matriz con celdas vacias y las enlaza
     */
    private void initializeMatrix() {
        // Crear todas las celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = new Cell();
            }
        }

        // Establecer conexiones entre celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell current = matrix[i][j];
                
                if (i > 0) current.setUp(matrix[i-1][j]);
                if (i < rows - 1) current.setDown(matrix[i+1][j]);
                if (j > 0) current.setLeft(matrix[i][j-1]);
                if (j < cols - 1) current.setRight(matrix[i][j+1]);
            }
        }
    }

    /**
     * Asigna un valor o formula a una celda especifica de la matirz.
     * Si empieza con = lo toma como una formula.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param value Valor o formula de la celda.
     */
    public void setCellValue(int row, int col, String value) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) return;
        
        Cell cell = matrix[row][col];
        if (cell == null) return;
        
        cell.setContent(value);
        
        if (value != null && value.startsWith("=")) {
            handleFormula(cell, value, row, col);
        } else {
            handlePlainValue(cell, value);
        }
        
        //notifyDependents(row, col);
    }

    /**
     * Maneja el procesamiento de formula y su evaluacion con el parser.
     * @param cell Celda a evaluar.
     * @param formula Formula que pasara por el parser.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     */
    private void handleFormula(Cell cell, String formula, int row, int col) {
        cell.setFormula(formula);
        cell.setType(CellType.FORMULA);
        
        if (formulaParser != null) {
            Object result = formulaParser.parseFormula(
                formula.substring(1), row, col);
            cell.setValue(result);
        } else {
            cell.setValue("#ERROR: No parser");
        }
    }

    /**
     * Procesa los valores normales, osea, numeros o texto, de una celda.
     * @param cell Celda a evaluar.
     * @param value Contenido de la celda.
     */
    private void handlePlainValue(Cell cell, String value) {
        cell.setFormula("");
        try {
            double numValue = Double.parseDouble(value);
            cell.setValue(numValue);
            cell.setType(CellType.NUMBER);
        } catch (NumberFormatException e) {
            cell.setValue(value);
            cell.setType(CellType.TEXT);
        }
    }
    
    /*
     * Vuelve a evaluar las formulas de la matriz cuando una celda se modifique.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
   
    private void notifyDependents(int row, int col) {
        String changedCellKey = createCellKey(row, col);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = matrix[i][j];
                if (cell != null && cell.getType() == CellType.FORMULA) {
                    if (dependsOn(cell.getFormula(), changedCellKey)) {
                        handleFormula(cell, cell.getFormula(), i, j);
                    }
                }
            }
        }
    }

     * Verifica si la formula depende de una celda por su clave.
     * @param formula Formula a evaluar/modificar.
     * @param cellKey Clave de la celda (coordenada).
     * @return 
    private boolean dependsOn(String formula, String cellKey) {
        return formula != null && formula.contains(cellKey);
    }
    */
    
    /**
     * Crea la representacion de una celda
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Reperesentacion de la celda (coordenada).
     */
    private String createCellKey(int row, int col) {
        char colChar = (char) ('A' + col);
        return "" + colChar + (row + 1);
    }

    /**
     * Obtiene la celda ubicada en la fila y columna especifica.
     * @param row FIla de la celda.
     * @param col Columna de la celda.
     * @return Celda correspondiente o null si esta fuera del rango.
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols) {
            return null;
        }
        return matrix[row][col];
    }

    /**
     * Regresa la cantidad total de filas
     * @return Numero de filas
     */
    public int getRows(){
        return rows; 
    }
    /**
     * Regresa la cantidad total de columnas.
     * @return Numero de columnas.
     */
    public int getCols(){ 
        return cols; 
    }
}