/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase encargada de interpretar y procesar formulas en una celda de la hoja.
 * Soporta suma y multiplicacion con coordenadas individuales y rangos.
 * 
 * Las coordenadas son 1 indexadas cuando las escribe el usuario, pero el programa
 * las maneja con base 0.
 * @author maryori
 */
package util;

import controller.SheetController;
import model.Cell;
import model.Sheet;

public class FormulaParser {
    private SheetController controller;

    /**
     * Constructor del parser que recibe el controlador para acceder a la hoja y matriz.
     * @param controller COntrolador que maneja el libro y sus hojas.
     */
    public FormulaParser(SheetController controller) {
        this.controller = controller;
    }

    /**
     * Metodo principal para interpretar una formula.
     * @param formula Contenido de la formula sin el signo =.
     * @param currentRow Fila de la celda actual.
     * @param currentCol Columna de la celda actual.
     * @return Resultado evaluado de la formula o error.
     */
    public Object parseFormula(String formula, int currentRow, int currentCol) {
        formula = formula.trim().toLowerCase();
        
        if (formula.startsWith("suma(")) {
            return parseSumFormula(formula, currentRow, currentCol);
        } else if (formula.startsWith("multiplicacion(") || formula.startsWith("mult(")) {
            return parseMultiplicationFormula(formula, currentRow, currentCol);
        }
        
        return "Fórmula no reconocida";
    }

    /**
     * Interpreta y evalua la formula de la suma.
     * @param formula Contenido de la formula.
     * @param currentRow Fila de la celda actual.
     * @param currentCol Columna de la celda actual.
     * @return Resultado evaluado de la formula o error.
     */
    private double parseSumFormula(String formula, int currentRow, int currentCol) {
        try {
            String paramsStr = formula.substring(5, formula.length() - 1).trim();

            // Caso 1: Sumar celdas individuales (=suma((1,1),(2,2)))
            if (paramsStr.contains("),(")) {
                String[] cellParts = paramsStr.split(",\\s*(?=\\()");
                double sum = 0;
                Sheet sheet = controller.getWorkbook().getCurrentSheet();

                for (String part : cellParts) {
                    String coordStr = part.replaceAll("[()]", "").trim();
                    int[] coords = parseCellCoordinates(coordStr);

                    Cell cell = sheet.getMatrix().getCell(coords[0], coords[1]);
                    if (cell != null && cell.getValue() instanceof Number) {
                        sum += ((Number) cell.getValue()).doubleValue();
                    }
                }
                return sum;
            }
            // Caso 2: Sumar un rango clásico (=suma(Hoja1, (1,1), (5,5))
            else {
                String[] parts = paramsStr.split(",\\s*(?=\\()");
                if (parts.length != 3) return 0.0;

                String sheetName = parts[0].trim();
                String startCell = parts[1].replaceAll("[()]", "").trim();
                String endCell = parts[2].replaceAll("[()]", "").trim();

                Sheet sheet = controller.getSheetByName(sheetName);
                if (sheet == null) return 0.0;

                int[] startCoords = parseCellCoordinates(startCell);
                int[] endCoords = parseCellCoordinates(endCell);

                double sum = 0;
                for (int row = startCoords[0]; row <= endCoords[0]; row++) {
                    for (int col = startCoords[1]; col <= endCoords[1]; col++) {
                        Cell cell = sheet.getMatrix().getCell(row, col);
                        if (cell != null && cell.getValue() instanceof Number) {
                            sum += ((Number) cell.getValue()).doubleValue();
                        }
                    }
                }
                return sum;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Adapata la coordenada a base cero.
     * @param coordStr Cadena de coordenada sinparentesis.
     * @return Arreglo con fila y columna base 0.
     */
    private int[] parseCellCoordinates(String coordStr) {
        String[] parts = coordStr.split(",");
        return new int[]{
            Integer.parseInt(parts[0].trim()) - 1, // Convertir a índice base 0
            Integer.parseInt(parts[1].trim()) - 1
        };
    }

    /**
     * Interpreta y evalua la formula de multiplicacion.
     * @param formula Contenido de la formula.
     * @param currentRow Fila de la celda actual.
     * @param currentCol Columna de la celda actual.
     * @return Resultado evaluado de la formula o error.
     */
    private double parseMultiplicationFormula(String formula, int currentRow, int currentCol) {
        try {
            // Limpiar la fórmula (soporta tanto "multiplicacion" como "mult")
            String paramsStr = formula.replaceFirst("multiplicacion\\(", "")
                                    .replaceFirst("mult\\(", "")
                                    .replace(")", "")
                                    .trim();

            // Caso 1: Multiplicar celdas individuales (ej: =multiplicacion((1,1),(1,2)))
            if (paramsStr.startsWith("(")) {
                String[] cellParts = paramsStr.split(",\\s*(?=\\()");
                double product = 1.0;
                Sheet sheet = controller.getWorkbook().getCurrentSheet();

                for (String part : cellParts) {
                    String coordStr = part.replaceAll("[()]", "").trim();
                    int[] coords = parseCellCoordinates(coordStr);

                    Cell cell = sheet.getMatrix().getCell(coords[0], coords[1]);
                    if (cell != null && cell.getValue() instanceof Number) {
                        product *= ((Number) cell.getValue()).doubleValue();
                    }
                }
                return product;
            }
            // Caso 2: Multiplicar un rango (ej: =multiplicacion(Hoja1, (1,1), (5,5)))
            else {
                String[] parts = paramsStr.split(",\\s*(?=\\()");
                if (parts.length != 3) return 1.0;

                String sheetName = parts[0].trim();
                String startCell = parts[1].replaceAll("[()]", "").trim();
                String endCell = parts[2].replaceAll("[()]", "").trim();

                Sheet sheet = controller.getSheetByName(sheetName);
                if (sheet == null) return 1.0;

                int[] startCoords = parseCellCoordinates(startCell);
                int[] endCoords = parseCellCoordinates(endCell);

                double product = 1.0;
                for (int row = startCoords[0]; row <= endCoords[0]; row++) {
                    for (int col = startCoords[1]; col <= endCoords[1]; col++) {
                        Cell cell = sheet.getMatrix().getCell(row, col);
                        if (cell != null && cell.getValue() instanceof Number) {
                            product *= ((Number) cell.getValue()).doubleValue();
                        }
                    }
                }
                return product;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1.0;
        }
    }
    /*
    private int[] parseCellReference(String cellRef) {
        cellRef = cellRef.replace("(", "").replace(")", "").trim();
        String[] parts = cellRef.split(",");
        return new int[]{Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())};
    }
    */
}
