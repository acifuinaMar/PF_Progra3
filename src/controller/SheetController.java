/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import util.FormulaParser;
import util.FileManager;
/**
 * Controlador principal que gestiona las hojas del libro,
 * edicion de celdas y operaciones en el archivo.
 * 
 * Se encarga de la logica del modelo celda-hoja-matriz y herramientas como
 * parser de formulas y manejo de archivos.
 * @author maryori
 */
public class SheetController {
    private Workbook workbook;
    private FormulaParser formulaParser;
    private FileManager fileManager;

    /**
     * Constructor que inicializa el libro, parser de formulas y gestor de 
     * archivo. Configuta el parser en todas las hojas del libro.
     */
    public SheetController() {
        this.workbook = new Workbook();
        this.formulaParser = new FormulaParser(this);
        this.fileManager = new FileManager(this);
        
        // Configurar el parser en todas las matrices existentes
        for (Sheet sheet : workbook.getSheets()) {
            sheet.getMatrix().setFormulaParser(this.formulaParser);
        }
    }

    /**
     * Obtiene el libro donde estoy trabajando actualmente.
     * @return {@code WorkBook}.
     */
    public Workbook getWorkbook() { 
        return workbook; 
    }

    /**
     * Estable el valor o formula en la celda y hoja actual.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param value Valor o formula.
     */
    public void setCellValue(int row, int col, String value) {
        if (value == null || workbook.getSheets().isEmpty()) return;
        
        Sheet currentSheet = workbook.getCurrentSheet();
        currentSheet.getMatrix().setCellValue(row, col, value);
    }

    /**
     * Obtiene el valor de una celda/hoja actual.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Valor de la celda o null.
     */
    public Object getCellValue(int row, int col) {
        if (workbook.getSheets().isEmpty()) return "";
        
        Cell cell = workbook.getCurrentSheet().getMatrix().getCell(row, col);
        return cell != null ? cell.getValue() : "";
    }

    /**
     * Obtiene el contenido de la celda (formula o texto).
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Contenido de la celda.
     */
    public String getCellContent(int row, int col) {
        if (workbook.getSheets().isEmpty()) return "";
        
        Cell cell = workbook.getCurrentSheet().getMatrix().getCell(row, col);
        if (cell == null) return "";
        
        return cell.getFormula().isEmpty() ? cell.getContent() : cell.getFormula();
    }

    /**
     * Agrega una nueva hoja al libro.
     * @param name Nombre de la hoja nueva.
     */
    public void addSheet(String name) {
        if (name == null || name.trim().isEmpty()) return;
        
        Sheet newSheet = new Sheet(name);
        newSheet.getMatrix().setFormulaParser(this.formulaParser);
        workbook.getSheets().add(newSheet);
    }

    /**
     * Cambia la hoja activa en baje a un indice.
     * @param index Indice de la hoja a activa.
     */
    public void switchSheet(int index) {
        if (index >= 0 && index < workbook.getSheets().size()) {
            workbook.setCurrentSheetIndex(index);
        }
    }

    /**
     * Guarda el contenido del libro en un archivo.
     * @param filename Nombre del archivo.
     */
    public void saveToFile(String filename) {
        fileManager.saveWorkbook(filename);
    }

    /**
     * Carga un libro desde un archivo.
     * @param filename Nombre del archivo que recuperamos.
     */
    public void loadFromFile(String filename) {
        fileManager.loadWorkbook(filename);
        // Configurar el parser en todas las hojas cargadas
        for (Sheet sheet : workbook.getSheets()) {
            sheet.getMatrix().setFormulaParser(this.formulaParser);
        }
    }

    /**
     * Busca y regresa una hoja de acuerdo al nombre.
     * @param name Nombre del archivo.
     * @return {@code Sheet} o {@code null} si no existe.
     */
    public Sheet getSheetByName(String name) {
        for (Sheet sheet : workbook.getSheets()) {
            if (sheet.getName().equalsIgnoreCase(name.trim())) {
                return sheet;
            }
        }
        return null;
    }

    /**
     * Obtiene el valor numerico de una celda y hoja especifica.
     * @param sheetName Nombre de la hoja.
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Valor numerico de la celda o 0.0 si no existe/no es numero.
     */
    public Object getCellValueFromSheet(String sheetName, int row, int col) {
        Sheet sheet = getSheetByName(sheetName);
        if (sheet == null) return 0.0;
        
        Cell cell = sheet.getMatrix().getCell(row, col);
        if (cell == null || !(cell.getValue() instanceof Number)) return 0.0;
        
        return cell.getValue();
    }
    
    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
}