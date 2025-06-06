/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app;

import javax.swing.SwingUtilities;
import view.MainFrame;

/**
 * Clase principal que contiene el método `main` para iniciar la aplicación.
 * Se encarga de crear e inicializar la interfaz gráfica principal 
 * {@link MainFrame} en el hilo de despacho de eventos de Swing, 
 * garantizando una ejecución segura y fluida de la interfaz de usuario.
 * @author maryori
 */

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
