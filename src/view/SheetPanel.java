/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 * Representa una hoja de calculo. 
 * Permite ver y editar las celdas de una matriz, aplicar formulas y cambiar
 * entre hojas del libro.
 * Este panel se conecta con el controlador {@link SheetController} para
 * gestionar la lógica del modelo.
 * @author maryori
 */

import controller.SheetController;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import model.Cell;

public class SheetPanel extends JPanel {
    private JTable table;
    private SheetTableModel tableModel;
    private SheetController controller;
    private JComboBox<String> sheetComboBox;
    private JTextField formulaField;
    private JButton applyButton;
    private JButton rejectButton;

    /**
     * Constructor del panel que recibe al controlador de hojas.
     * @param controller Controlador que gestiona los datos de la hoja.
     */
    public SheetPanel(SheetController controller) {
        this.controller = controller;
        initializeUI();
    }

    /**
     * Inicializa la interfaz grafica del panel.
     * - Superior con selector de hoja, txtField de formula y botonoes.
     * - Tabla para las celdas de la hoja activa.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Panel superior con controles
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // ComboBox para seleccionar hojas
        sheetComboBox = new JComboBox<>();
        updateSheetComboBox();
        sheetComboBox.addActionListener(e -> {
            controller.switchSheet(sheetComboBox.getSelectedIndex());
            tableModel.fireTableDataChanged();
        });
        
        // Campo de fórmula
        formulaField = new JTextField();
        formulaField.setToolTipText("Ingrese aquí la fórmula para la celda actual");
        
        // Botones Aplicar/Rechazar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        applyButton = new JButton("Aplicar");
        applyButton.addActionListener(e -> applyFormula());
        rejectButton = new JButton("Rechazar");
        rejectButton.addActionListener(e -> formulaField.setText(""));
        
        buttonPanel.add(applyButton);
        buttonPanel.add(rejectButton);
        
        topPanel.add(sheetComboBox, BorderLayout.WEST);
        topPanel.add(formulaField, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Tabla principal
        tableModel = new SheetTableModel();
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Configurar el ancho de las columnas
        for (int i = 0; i < 20; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        // Agregar componentes al panel
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Aplica la formula escrita en el txtField a la celda seleccionada.
     */
    private void applyFormula() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        
        if (row >= 0 && col >= 0) {
            String formula = formulaField.getText();
            // Si es una fórmula de suma
            if (formula.toLowerCase().startsWith("=suma(")) {
                double resultado = controller.evaluarFormula(formula, row, col);
                controller.setCellValue(row, col, String.valueOf(resultado));
            } else if (formula.toLowerCase().startsWith("=multiplicacion(") || formula.toLowerCase().startsWith("=mult(")){
                double resultado = controller.evaluarFormula(formula, row, col);
                controller.setCellValue(row, col, String.valueOf(resultado));
            } else {
                controller.setCellValue(row, col, formula);
            }

            tableModel.fireTableDataChanged();
            formulaField.setText("");
        }
    }

    /**
     * Actualiza el comboBox y recarga los datos de la tabla.
     * Se llama despues de agregar o carga hojas nuevas.
     */
    public void refresh() {
        updateSheetComboBox();
        tableModel.fireTableDataChanged();
    }

    /**
     * Actualiza el comboBox de seleccion de hoja con los nombres del libro.
     */
    private void updateSheetComboBox() {
        sheetComboBox.removeAllItems();
        for (var sheet : controller.getWorkbook().getSheets()) {
            sheetComboBox.addItem(sheet.getName());
        }
        sheetComboBox.setSelectedIndex(controller.getWorkbook().getCurrentSheetIndex());
    }
    
    /**
     * Modelo de la tabla que vincula las celdas de la hoja con JTable. 
     * Permite la edicion y visualizacion.
     */
    private class SheetTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return controller.getWorkbook().getCurrentSheet().getMatrix().getRows();
        }

        @Override
        public int getColumnCount() {
            return controller.getWorkbook().getCurrentSheet().getMatrix().getCols();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Cell cell = controller.getWorkbook()
                                .getCurrentSheet()
                                .getMatrix()
                                .getCell(row, col);
            return (cell != null && cell.getContent() != null) ? cell.getContent() : "";
        }
        /*@Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return controller.getCellValue(rowIndex, columnIndex);
        }*/

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            controller.setCellValue(rowIndex, columnIndex, aValue.toString());
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public String getColumnName(int column) {
            return Character.toString((char) ('A' + column));
        }
    }
}
