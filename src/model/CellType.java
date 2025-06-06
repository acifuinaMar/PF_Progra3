/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Clase que define y clasifica los posibles tipos de celda que van a haber
 * en mi hoja de calculo.
 * <ul>
 *   <li>{@code NUMBER} – La celda tiene valor numérico.</li>
 *   <li>{@code TEXT} – La celda tiene texto plano.</li>
 *   <li>{@code FORMULA} – La celda tiene una fórmula que debe ser evaluada.</li>
 *   <li>{@code EMPTY} – La celda está vacía, sin contenido ni fórmula.</li>
 * </ul>
 * @author maryori
 */

public enum CellType {
    NUMBER,
    TEXT,
    FORMULA,
    EMPTY
}
