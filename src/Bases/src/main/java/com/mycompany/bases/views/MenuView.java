/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bases.views;

/**
 *
 * @author dard
 */
import com.mycompany.bases.controllers.*;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Vista principal del sistema que maneja la interfaz de usuario y navegación.
 * 
 * Esta clase actúa como el punto central de navegación de la aplicación,
 * proporcionando un menú principal que permite acceder a los diferentes
 * módulos de gestión del sistema.
 * 
 * Responsabilidades principales:
 * - Mostrar el menú principal de navegación
 * - Coordinar la interacción entre diferentes controladores
 * - Manejar la entrada del usuario y validación de opciones
 * - Gestionar el cierre adecuado de recursos al salir del sistema
 * 
 * La clase implementa el patrón View del MVC, separando la presentación
 * de la lógica de negocio que reside en los controladores.
 * 
 * Módulos disponibles:
 * - Gestión de Usuarios (MongoDB)
 * - Gestión de Ropa (Oracle)
 * - Gestión de Ventas (Oracle)
 * 
 * @author DarThunder
 * @version 1.0
 */
public class MenuView {
    /** Scanner para capturar entrada del usuario desde consola */
    private final Scanner scanner;
    
    /** Controlador para operaciones con MongoDB (gestión de usuarios) */
    private final MongoController mongoController;
    
    /** Controlador para operaciones con Oracle (gestión de ropa y ventas) */
    private final OracleController oracleController;
    
    /**
     * Constructor que inicializa todos los componentes necesarios para la vista.
     * 
     * Crea las instancias de los controladores de base de datos y configura
     * el scanner para la entrada del usuario. La inicialización de los
     * controladores establece automáticamente las conexiones a las bases de datos.
     * 
     * @throws Exception Si ocurre algún error durante la inicialización de
     *                   los controladores o las conexiones a base de datos.
     *                   Esto puede incluir errores de conectividad, configuración
     *                   incorrecta o problemas de autenticación.
     */
    public MenuView() throws Exception {
        // Inicializa el scanner para entrada de datos del usuario
        this.scanner = new Scanner(System.in);
        
        // Inicializa el controlador de MongoDB
        // Establece conexión automática según configuración
        this.mongoController = new MongoController();
        
        // Inicializa el controlador de Oracle
        // Establece conexión automática según configuración
        this.oracleController = new OracleController(mongoController);
    }
    
    /**
     * Muestra y maneja el menú principal de la aplicación.
     * 
     * Este método implementa el bucle principal de la interfaz de usuario,
     * mostrando repetidamente las opciones disponibles y procesando la
     * selección del usuario hasta que elija salir del sistema.
     * 
     * Flujo de operación:
     * 1. Muestra las opciones del menú principal
     * 2. Captura la selección del usuario
     * 3. Valida y procesa la opción seleccionada
     * 4. Delega el control al controlador correspondiente
     * 5. Retorna al menú principal cuando el usuario termina la operación
     * 6. Repite hasta que el usuario elija salir
     * 
     * Opciones disponibles:
     * 1. Gestión de Usuarios (MongoDB) - Accede al módulo de usuarios
     * 2. Gestión de Ropa (Oracle) - Accede al módulo de inventario de ropa
     * 3. Gestión de Ventas (Oracle) - Accede al módulo de registro de ventas
     * 4. Salir - Cierra conexiones y termina la aplicación
     * 
     * El método utiliza un bucle infinito que se rompe únicamente cuando
     * el usuario selecciona la opción de salir.
     */
    public void showMainMenu() {
        while (true) {
            // Muestra el encabezado y opciones del menú principal
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestión de Usuarios (MongoDB)");
            System.out.println("2. Gestión de Ropa (Oracle)");
            System.out.println("3. Gestión de Ventas (Oracle)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            // Captura la opción seleccionada por el usuario
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume el salto de línea pendiente
            
            // Procesa la opción seleccionada
            switch (option) {
                case 1 -> {
                    // Delega el control al controlador de MongoDB para gestión de usuarios
                    mongoController.manageUsers();
                }
                case 2 -> {
                    // Delega el control al controlador de Oracle para gestión de ropa
                    oracleController.manageClothes();
                }
                case 3 -> {
                    // Delega el control al controlador de Oracle para gestión de ventas
                    oracleController.manageVentas();
                }
                case 4 -> {
                    // Opción de salida: cierra recursos y termina la aplicación
                    System.out.println("Saliendo del sistema...");
                    closeConnections(); // Limpieza de recursos
                    return; // Sale del bucle y termina el método
                }
                default -> {
                    // Maneja opciones inválidas sin terminar la aplicación
                    System.out.println("Opción no válida");
                }
            }
        }
    }
    
    /**
     * Cierra adecuadamente todas las conexiones de base de datos.
     * 
     * Este método es crucial para la limpieza de recursos y debe ser llamado
     * antes de terminar la aplicación. Asegura que todas las conexiones
     * activas se cierren correctamente para evitar:
     * - Memory leaks
     * - Conexiones huérfanas en las bases de datos
     * - Posibles bloqueos de recursos
     * 
     * El método maneja cada conexión de forma independiente para asegurar
     * que un error en una no impida el cierre de las otras. Los errores
     * se capturan y reportan sin detener el proceso de limpieza.
     * 
     * Orden de cierre:
     * 1. Oracle Database (maneja SQLException específicamente)
     * 2. MongoDB (manejo de errores genérico)
     * 
     * Cada cierre se confirma con un mensaje informativo al usuario.
     */
    private void closeConnections() {
        // Cierre de conexión Oracle con manejo específico de SQLException
        try {
            if (oracleController != null) {
                oracleController.close();
                System.out.println("\nConexión a OracleDB cerrada.");
            }
        } catch (SQLException e) {
            // Manejo específico de errores SQL sin interrumpir el proceso
            System.err.println("Error al cerrar Oracle: " + e.getMessage());
        }
        
        // Cierre de conexión MongoDB
        // MongoDB no lanza SQLException, manejo más simple
        if (mongoController != null) {
            mongoController.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
}