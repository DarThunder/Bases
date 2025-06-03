/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bases.controllers;

/**
 *
 * @author dard
 */
import java.sql.*;
import com.mycompany.bases.config.DatabaseConfig;
import static com.mycompany.bases.utils.PrettyPrinter.printResultSet;
import java.util.Scanner;

public class OracleController {
    private final Connection oracleConn;
    private final Scanner scanner;
    
    public OracleController() throws SQLException {
        this.oracleConn = DriverManager.getConnection(
            DatabaseConfig.ORACLE_URL,
            DatabaseConfig.ORACLE_USER,
            DatabaseConfig.ORACLE_PASSWORD
        );
        this.scanner = new Scanner(System.in);
    }
    
    public void manageClothes() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE ROPA (OracleDB) ===");
            System.out.println("1. Agregar prenda");
            System.out.println("2. Mostrar todo el inventario");
            System.out.println("3. Buscar prenda por tipo");
            System.out.println("4. Eliminar prenda");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 ->
                    addClothing();
                case 2 ->
                    showAllClothes();
                case 3 ->
                    findClothingByType();
                case 4 ->
                    deleteClothing();
                case 0 ->
                    System.out.println("Volviendo al menú principal...");
                default ->
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    public void addClothing() {
        try {
            System.out.print("\nIngrese tipo de prenda (camisa/pantalón/vestido): ");
            String tipo = scanner.nextLine();

            System.out.print("Ingrese color: ");
            String color = scanner.nextLine();

            System.out.print("Ingrese talla: ");
            String talla = scanner.nextLine();

            System.out.print("Ingrese precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            String sql = "INSERT INTO ropa (id, tipo, color, talla, precio, fecha_ingreso) VALUES (ropa_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setString(1, tipo);
            pstmt.setString(2, color);
            pstmt.setString(3, talla);
            pstmt.setDouble(4, precio);

            pstmt.executeUpdate();
            System.out.println("Prenda agregada con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al agregar prenda: " + e.getMessage());
            if (e.getErrorCode() == 942) {
                createClothesTable();
                addClothing();
            }
        }
    }

    public void createClothesTable() {
        try {
            System.out.println("Creando tabla ropa...");

            String sql = "CREATE TABLE ropa ("
                    + "id NUMBER PRIMARY KEY,"
                    + "tipo VARCHAR2(50) NOT NULL,"
                    + "color VARCHAR2(30) NOT NULL,"
                    + "talla VARCHAR2(10) NOT NULL,"
                    + "precio NUMBER(10,2) NOT NULL,"
                    + "fecha_ingreso DATE NOT NULL)";

            Statement stmt = oracleConn.createStatement();
            stmt.execute(sql);

            stmt.execute("CREATE SEQUENCE ropa_seq START WITH 1 INCREMENT BY 1");

            System.out.println("Tabla ropa creada exitosamente!");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }

    public void showAllClothes() {
        try {
            System.out.println("\n=== INVENTARIO DE ROPA ===");

            Statement stmt = oracleConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ropa ORDER BY id");

            while (rs.next()) {
                printResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar ropa: " + e.getMessage());
        }
    }

    public void findClothingByType() {
        try {
            System.out.print("\nIngrese tipo de prenda a buscar: ");
            String tipo = scanner.nextLine();

            String sql = "SELECT * FROM ropa WHERE LOWER(tipo) = LOWER(?)";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setString(1, tipo);

            ResultSet rs = pstmt.executeQuery();

            boolean encontrado = false;
            while (rs.next()) {
                encontrado = true;
                printResultSet(rs);
            }

            if (!encontrado) {
                System.out.println("No se encontraron prendas de ese tipo");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar prenda: " + e.getMessage());
        }
    }

    public void deleteClothing() {
        try {
            System.out.print("\nIngrese ID de la prenda a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM ropa WHERE id = ?";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int deleted = pstmt.executeUpdate();

            if (deleted > 0) {
                System.out.println("Prenda eliminada con éxito!");
            } else {
                System.out.println("No se encontró prenda con ese ID");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar prenda: " + e.getMessage());
        }
    }
    
    public void close() throws SQLException{
        oracleConn.close();
    }
}
    
   
