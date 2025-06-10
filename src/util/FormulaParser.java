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
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public double parseSumFormula(String formula, int currentRow, int currentCol) {
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
                    
                    if (coords[0] < 0 || coords[1] < 0 || 
                        coords[0] >= sheet.getMatrix().getRows() || 
                        coords[1] >= sheet.getMatrix().getCols()) {
                        continue;
                    }


                    Cell cell = sheet.getMatrix().getCell(coords[0], coords[1]);
                    
                    if (cell != null && cell.getContent() != null && !cell.getContent().isEmpty()) {
                        try {
                            sum += Double.parseDouble(cell.getContent());
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                return sum;
            }
            // Caso 2: Sumar un rango clásico (=suma(Hoja1, (1,1), (5,5))
            else {
                // Buscamos los tres componentes: hoja, (x1,y1), (x2,y2)
                Pattern pattern = Pattern.compile("^(.+?),\\s*\\((\\d+),(\\d+)\\),\\s*\\((\\d+),(\\d+)\\)$");
                Matcher matcher = pattern.matcher(paramsStr);

                if (!matcher.matches()) {
                    System.out.println("Fórmula mal escrita o incompleta");
                    return 0.0;
                }

                String sheetName = matcher.group(1).trim().replaceAll("^\\(*|\\)*$", "");
                int row1 = Integer.parseInt(matcher.group(2)) - 1;
                int col1 = Integer.parseInt(matcher.group(3)) - 1;
                int row2 = Integer.parseInt(matcher.group(4)) - 1;
                int col2 = Integer.parseInt(matcher.group(5)) - 1;


                System.out.println("Hoja: '" + sheetName + "'");
                System.out.println("Rango fila: " + row1 + " a " + row2);
                System.out.println("Rango col: " + col1 + " a " + col2);
                Sheet sheet = controller.getSheetByName(sheetName);
                if (sheet == null) {
                    System.out.println("No se encontró la hoja: " + sheetName);
                    return 0.0;
                }

                int maxRows = sheet.getMatrix().getRows();
                int maxCols = sheet.getMatrix().getCols();

                System.out.println("Filas hoja: " + maxRows + ", Columnas hoja: " + maxCols);
                
                row1 = Math.max(0, row1);
                col1 = Math.max(0, col1);
                row2 = Math.min(maxRows - 1, row2);
                col2 = Math.min(maxCols - 1, col2);

                double sum = 0;
                for (int row = row1; row <= row2; row++) {
                    for (int col = col1; col <= col2; col++) {
                        Cell cell = sheet.getMatrix().getCell(row, col);
                        String content = (cell != null) ? cell.getContent() : "null";
                        System.out.println("Celda (" + row + "," + col + "): " + content);
                        if (cell != null && cell.getContent() != null && !cell.getContent().isEmpty()) {
                            try {
                                sum += Double.parseDouble(cell.getContent());
                            } catch (NumberFormatException e) {
                                System.out.println("No es número: " + cell.getContent());
                            }
                        }
                    }
                }
                System.out.println("Suma resultado: " + sum);
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
    public double parseMultiplicationFormula(String formula, int currentRow, int currentCol) {
        try {
            String paramsStr;

            // Soportar =multiplicacion(...) o =mult(...)
            if (formula.startsWith("multiplicacion(")) {
                paramsStr = formula.substring("multiplicacion(".length(), formula.length() - 1).trim();
            } else if (formula.startsWith("mult(")) {
                paramsStr = formula.substring("mult(".length(), formula.length() - 1).trim();
            } else {
                return 0.0;
            }

            // Caso 1: Multiplicar celdas individuales (=mult((1,1),(2,2)))
            if (paramsStr.contains("),(")) {
                String[] cellParts = paramsStr.split(",\\s*(?=\\()");
                double product = 1;
                Sheet sheet = controller.getWorkbook().getCurrentSheet();
                boolean found = false;

                for (String part : cellParts) {
                    String coordStr = part.replaceAll("[()]", "").trim();
                    int[] coords = parseCellCoordinates(coordStr);

                    if (coords[0] < 0 || coords[1] < 0 ||
                        coords[0] >= sheet.getMatrix().getRows() ||
                        coords[1] >= sheet.getMatrix().getCols()) {
                        continue;
                    }

                    Cell cell = sheet.getMatrix().getCell(coords[0], coords[1]);
                    if (cell != null && cell.getContent() != null && !cell.getContent().isEmpty()) {
                        try {
                            product *= Double.parseDouble(cell.getContent());
                            found = true;
                        } catch (NumberFormatException e) {
                            System.out.println("No es número: " + cell.getContent());
                        }
                    }
                }
                return found ? product : 0.0;
            }

            // Caso 2: Multiplicar un rango clásico (=mult(Hoja1, (1,1), (5,5)))
            else {
                Pattern pattern = Pattern.compile("^(.+?),\\s*\\((\\d+),(\\d+)\\),\\s*\\((\\d+),(\\d+)\\)$");
                Matcher matcher = pattern.matcher(paramsStr);

                if (!matcher.matches()) {
                    System.out.println("Fórmula mal escrita o incompleta");
                    return 0.0;
                }

                String sheetName = matcher.group(1).trim().replaceAll("^\\(*|\\)*$", "");
                int row1 = Integer.parseInt(matcher.group(2)) - 1;
                int col1 = Integer.parseInt(matcher.group(3)) - 1;
                int row2 = Integer.parseInt(matcher.group(4)) - 1;
                int col2 = Integer.parseInt(matcher.group(5)) - 1;

                System.out.println("Hoja: '" + sheetName + "'");
                System.out.println("Rango fila: " + row1 + " a " + row2);
                System.out.println("Rango col: " + col1 + " a " + col2);
                Sheet sheet = controller.getSheetByName(sheetName);
                if (sheet == null) {
                    System.out.println("No se encontró la hoja: " + sheetName);
                    return 0.0;
                }

                int maxRows = sheet.getMatrix().getRows();
                int maxCols = sheet.getMatrix().getCols();
                System.out.println("Filas hoja: " + maxRows + ", Columnas hoja: " + maxCols);

                row1 = Math.max(0, row1);
                col1 = Math.max(0, col1);
                row2 = Math.min(maxRows - 1, row2);
                col2 = Math.min(maxCols - 1, col2);

                double product = 1;
                boolean found = false;
                for (int row = row1; row <= row2; row++) {
                    for (int col = col1; col <= col2; col++) {
                        Cell cell = sheet.getMatrix().getCell(row, col);
                        String content = (cell != null) ? cell.getContent() : "null";
                        System.out.println("Celda (" + row + "," + col + "): " + content);
                        if (cell != null && cell.getContent() != null && !cell.getContent().isEmpty()) {
                            try {
                                product *= Double.parseDouble(cell.getContent());
                                found = true;
                            } catch (NumberFormatException e) {
                                System.out.println("No es número: " + cell.getContent());
                            }
                        }
                    }
                }
                System.out.println("Producto resultado: " + product);
                return found ? product : 0.0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
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
