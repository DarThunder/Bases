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

public class MenuView {
    private final Scanner scanner;
    private final MongoController mongoController;
    private final OracleController oracleController;
    
    public MenuView() throws Exception {
        this.scanner = new Scanner(System.in);
        this.mongoController = new MongoController();
        this.oracleController = new OracleController();
    }
    
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestión de Usuarios (MongoDB)");
            System.out.println("2. Gestión de Ropa (Oracle)");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            int option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1 -> mongoController.manageUsers();
                case 2 -> oracleController.manageClothes();
                case 3 -> {
                    System.out.println("Saliendo del sistema...");
                    closeConnections();
                    return;
                }
                default -> System.out.println("Opción no válida");
            }
        }
    }
    
    private void closeConnections() {
        try {
            if (oracleController != null) {
                oracleController.close();
                System.out.println("\nConexión a OracleDB cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar Oracle: " + e.getMessage());
        }

        if (mongoController != null) {
            mongoController.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
}