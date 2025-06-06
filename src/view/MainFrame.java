/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Interfaz grafica principal del sistema. 
 * Permite la edicion de la hoja, manejo de archivos y ver la tabla hash.
 * 
 * Usa un TabbedPane para mostrar la pestaña de la hoja y tabla hash.
 * Usa un JMenuBar para guardar, abrir y ayuda.
 * @author maryori
 */
package view;

import controller.HashTableController;
import controller.SheetController;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SheetPanel sheetPanel;
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private SheetController sheetController;
    private HashTableController hashTableController;

    /**
     * Constructor del marco principal.
     * Inicializa los controlodaroes, interfaz del usuario y barra de menu.
     */
    public MainFrame() {
        super("MAxcell");
        this.sheetController = new SheetController();
        this.hashTableController = new HashTableController();
        
        initializeUI();
        setupMenuBar();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Inicializa componentese gráficos pricnipales.
     * (Pestañas y panel principal de la hoja de calculo).
     */
    private void initializeUI() {
        tabbedPane = new JTabbedPane();
        sheetPanel = new SheetPanel(sheetController);
        tabbedPane.addTab("Hoja de Cálculo", sheetPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Crea y configura la barra de menu para manejar hojas, guardar/abirr un archivo.
     */
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        
        JMenuItem newSheetItem = new JMenuItem("Nueva Hoja");
        newSheetItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Nombre de la nueva hoja:");
            if (name != null && !name.trim().isEmpty()) {
                sheetController.addSheet(name);
                sheetPanel.refresh();
            }
        });
        
        JMenuItem saveItem = new JMenuItem("Guardar");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                sheetController.saveToFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        JMenuItem loadItem = new JMenuItem("Abrir");
        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                sheetController.loadFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                sheetPanel.refresh();
            }
        });
        
        JMenuItem hashTableItem = new JMenuItem("Tabla Hash");
        hashTableItem.addActionListener(e -> {
            HashTablePanel hashTablePanel = new HashTablePanel(hashTableController);
            tabbedPane.addTab("Tabla Hash", hashTablePanel);
            tabbedPane.setSelectedComponent(hashTablePanel);
        });
        
        fileMenu.add(newSheetItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(hashTableItem);
        
        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Hoja Electrónica\nProyecto IV - Programación III\nMaryori Acifuina Juarez", 
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
}
