/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bases;

import com.mycompany.bases.views.MenuView;

/**
 *
 * @author dard
 */


/**
 * Clase principal de la aplicación de gestión.
 * 
 * Esta clase contiene el punto de entrada main() del programa y se encarga de:
 * - Inicializar la aplicación
 * - Manejar errores críticos durante el arranque
 * - Delegar el control a la interfaz de usuario principal
 * 
 * La aplicación utiliza un patrón MVC donde esta clase actúa como el
 * punto de entrada que transfiere el control al sistema de menús.
 * 
 * @author DarThunder
 * @version 1.0
 */
public class Bases {
    
    /**
     * Método principal que inicia la aplicación de gestión.
     * 
     * Funcionalidades:
     * - Muestra mensaje de inicio en consola
     * - Inicializa y muestra el menú principal de la aplicación
     * - Captura y maneja cualquier excepción crítica durante el arranque
     * - Proporciona información de error detallada en caso de fallo
     * 
     * El flujo normal incluye:
     * 1. Mensaje informativo de inicio
     * 2. Creación de la vista del menú principal
     * 3. Transferencia del control al sistema de menús
     * 
     * En caso de error:
     * - Se captura cualquier excepción no controlada
     * - Se muestra un mensaje de error descriptivo
     * - Se imprime el stack trace completo para debugging
     * 
     * @param args Argumentos de línea de comandos (no utilizados actualmente)
     */
    public static void main(String[] args) {
        try {
            // Mensaje informativo para el usuario sobre el inicio de la aplicación
            System.out.println("Iniciando aplicación de gestión...");
            
            // Instancia la vista principal del menú
            // MenuView se encarga de manejar toda la interfaz de usuario
            MenuView menu = new MenuView();
            
            // Transfiere el control al sistema de menús
            // A partir de aquí, MenuView maneja toda la interacción con el usuario
            menu.showMainMenu();
            
        } catch (Exception e) {
            // Manejo de errores críticos durante la inicialización
            
            // Mensaje de error dirigido al usuario en stderr
            System.err.println("Error inicializando la aplicación:");
            
            // Imprime el stack trace completo para facilitar el debugging
            // Esto ayuda a identificar la causa raíz del problema
            e.printStackTrace();
            
            // Nota: La aplicación terminará aquí si hay un error crítico
            // Considera agregar System.exit(1) si quieres un código de salida específico
        }
    }
}