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
    private Cell head;
    private int rows=20;
    private int cols=20;
    private transient FormulaParser formulaParser;

    /**
     * Constructor que crea una matriz de 20x20 de celdas enlazadas.
     */
    public OrthogonalMatrix() {
        this.head = new Cell(0,0);
    }

    /**
     * Establece el analizador de formulas para las expresiones
     * @param parser Instancia de FormulaParser que evalua formulas.
     */
    public void setFormulaParser(FormulaParser parser) {
        this.formulaParser = parser;
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
        Cell cell = getOrCreateCell(row, col);
        if (cell != null) {
            cell.setContent(value);
        
            if (value != null && value.startsWith("=")) {
                handleFormula(cell, value, row, col);
            } else {
                handlePlainValue(cell, value);
            }
        //notifyDependents(row, col);
        }
    }

    private Cell getOrCreateCell(int row, int col) {
        // Crear o obtener la fila
        Cell rowStart = getOrCreateRow(row);
        
        // Crear o obtener la columna
        return getOrCreateColumn(rowStart, row, col);
    }
    
    private Cell getOrCreateRow(int row) {
        Cell current = head;
        Cell prev = null;
        
        // Buscar la fila
        for (int r = 0; r < row; r++) {
            prev = current;
            if (current.getDown() == null) {
                current.setDown(new Cell(0, r+1));
            }
            current = current.getDown();
        }
        
        return current;
    }
    
    private Cell getOrCreateColumn(Cell rowStart, int row, int col) {
        Cell current = rowStart;
        Cell prev = null;
        
        // Buscar la columna
        for (int c = 0; c < col; c++) {
            prev = current;
            if (current.getRight() == null) {
                current.setRight(new Cell(c+1, row));
            }
            current = current.getRight();
        }
        
        return current;
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

        // Buscar fila
        Cell rowStart = head;
        for (int r = 0; r < row; r++) {
            if (rowStart.getDown() == null) {
                return null; // No existe la fila
            }
            rowStart = rowStart.getDown();
        }

        // Buscar columna
        Cell current = rowStart;
        for (int c = 0; c < col; c++) {
            if (current.getRight() == null) {
                return null; // No existe la columna
            }
            current = current.getRight();
        }

        return current;
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
