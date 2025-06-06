/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 * Panel grafico que permite ingresar claves y calcular el valor hash con el controlador.
 * Muestra una tabla con las claves y sus valores hash.
 * @author maryori
 */

import controller.HashTableController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HashTablePanel extends JPanel {
    private JTable table;
    private HashTableModel tableModel;
    private HashTableController controller;
    private JTextField keyField;
    private JButton addButton;

    /**
     * Constructor del panel de la tabla hash.
     * @param controller Controlador que maneja la logica de la tabla hash.
     */
    public HashTablePanel(HashTableController controller) {
        this.controller = controller;
        initializeUI();
    }

    /**
     * Inicializa los componentes del panel (txtField, boton y tabla).
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Panel superior para entrada de datos
        JPanel inputPanel = new JPanel(new BorderLayout());
        
        keyField = new JTextField();
        keyField.setToolTipText("Ingrese la clave");
        
        addButton = new JButton("Calcular Hash");
        addButton.addActionListener(this::addKeyAction);
        
        inputPanel.add(keyField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        
        // Tabla para mostrar la tabla hash
        tableModel = new HashTableModel();
        table = new JTable(tableModel);
        
        // Agregar componentes al panel
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Toma la clave del txtField, proceso, agrega a la tabla hash y muestra el valor calculado.
     * @param e Evento de accion generado por el boton.
     */
    private void addKeyAction(ActionEvent e) {
        String key = keyField.getText();
        if (!key.isEmpty()) {
            controller.addKey(key);
            tableModel.fireTableDataChanged();
            keyField.setText("");
            
            // Mostrar el valor hash calculado
            int hashValue = controller.getHashValue(key);
            JOptionPane.showMessageDialog(this, 
                "Clave: " + key + "\nValor Hash: " + hashValue,
                "Resultado del Hash", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Modelo para la JTable que represenat la estructura de la tabla hash 
     * (clave y valor).
     */
    private class HashTableModel extends javax.swing.table.AbstractTableModel {
        @Override
        public int getRowCount() {
            return controller.getTableSize();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                String key = controller.getKeys()[rowIndex];
                return key != null ? key : "";
            } else {
                int value = controller.getHashValues()[rowIndex];
                return value != 0 ? value : "";
            }
        }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Clave" : "Valor Hash";
        }
    }
}
