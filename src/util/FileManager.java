/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 * Clase que vela por la persistencia del libro (guardarlo y abrirlo).
 * Se apoyo del controlador SheetController para acceder al modelo.
 * @author maryori
 */

import controller.SheetController;
import model.*;

import java.io.*;

public class FileManager {
    private SheetController controller;

    /**
    * Constructor que recibe el controlador principal de las hojas.
    */
    public FileManager(SheetController controller) {
        this.controller = controller;
    }

    /**
     * Guarda el estado actual del libro en un archivo especifico.
     * @param filename Ruta del archivo donde se guardara el libro.
     */
    public void saveWorkbook(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(controller.getWorkbook());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga un libro antes guardado desde un archivo.
     * @param filename Ruta del archivo donde se leera el libro.
     */
    public void loadWorkbook(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Workbook workbook = (Workbook) ois.readObject();
            controller.setWorkbook(workbook);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
