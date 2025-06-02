/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bases;

/**
 *
 * @author dard
 */
import java.sql.*;
import java.util.Scanner;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class Bases {
   private static final String ORACLE_JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String ORACLE_USER = "system";
    private static final String ORACLE_PASSWORD = "123";
    
    private static final String MONGO_CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String MONGO_DB_NAME = "usuariosDB";
    
    private static Connection oracleConn;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDb;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Iniciando gestor de bases de datos...");
        
        try {
            connectToDatabases();
            
            int opcion;
            do {
                System.out.println("\n=== MENÚ PRINCIPAL ===");
                System.out.println("1. Gestionar usuarios (MongoDB)");
                System.out.println("2. Gestionar ropa (OracleDB)");
                System.out.println("3. Mostrar estadísticas");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch(opcion) {
                    case 1 -> manageUsers();
                    case 2 -> manageClothes();
                    case 3 -> showStatistics();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción no válida");
                }
            } while(opcion != 0);
            
        } catch(SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            closeConnections();
        }
    }
    
    private static void connectToDatabases() throws SQLException {
        /*System.out.println("\nConectando a OracleDB...");
        oracleConn = DriverManager.getConnection(ORACLE_JDBC_URL, ORACLE_USER, ORACLE_PASSWORD);
        System.out.println("Conexión a OracleDB establecida!");*/
        
        System.out.println("\nConectando a MongoDB...");
        mongoClient = MongoClients.create(MONGO_CONNECTION_STRING);
        mongoDb = mongoClient.getDatabase(MONGO_DB_NAME);
        System.out.println("Conexión a MongoDB establecida!");
    }
    
    private static void closeConnections() {
        try {
            if(oracleConn != null && !oracleConn.isClosed()) {
                oracleConn.close();
                System.out.println("\nConexión a OracleDB cerrada.");
            }
        } catch(SQLException e) {
            System.err.println("Error al cerrar Oracle: " + e.getMessage());
        }
        
        if(mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
    
    private static void manageUsers() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE USUARIOS (MongoDB) ===");
            System.out.println("1. Agregar usuario");
            System.out.println("2. Mostrar todos los usuarios");
            System.out.println("3. Buscar usuario por nombre");
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch(opcion) {
                case 1 -> addUser();
                case 2 -> showAllUsers();
                case 3 -> findUserByName();
                case 4 -> deleteUser();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida");
            }
        } while(opcion != 0);
    }
    
    private static void addUser() {
        System.out.print("\nIngrese nombre del usuario: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese email: ");
        String email = scanner.nextLine();
        
        System.out.print("Ingrese edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        
        Document user = new Document("nombre", nombre)
                        .append("email", email)
                        .append("edad", edad)
                        .append("fechaRegistro", new java.util.Date());
        
        mongoDb.getCollection("usuarios").insertOne(user);
        System.out.println("Usuario agregado con éxito!");
    }
    
    private static void showAllUsers() {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        for(Document user : mongoDb.getCollection("usuarios").find()) {
            System.out.println(user.toJson());
        }
    }
    
    private static void findUserByName() {
        System.out.print("\nIngrese nombre a buscar: ");
        String nombre = scanner.nextLine();
        
        Document user = mongoDb.getCollection("usuarios").find(eq("nombre", nombre)).first();
        
        if(user != null) {
            System.out.println("\nUsuario encontrado:");
            System.out.println(user.toJson());
        } else {
            System.out.println("Usuario no encontrado");
        }
    }
    
    private static void deleteUser() {
        System.out.print("\nIngrese nombre del usuario a eliminar: ");
        String nombre = scanner.nextLine();
        
        long deletedCount = mongoDb.getCollection("usuarios").deleteOne(eq("nombre", nombre)).getDeletedCount();
        
        if(deletedCount > 0) {
            System.out.println("Usuario eliminado con éxito!");
        } else {
            System.out.println("No se encontró usuario con ese nombre");
        }
    }
    
    private static void manageClothes() {
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
            
            switch(opcion) {
                case 1 -> addClothing();
                case 2 -> showAllClothes();
                case 3 -> findClothingByType();
                case 4 -> deleteClothing();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida");
            }
        } while(opcion != 0);
    }
    
    private static void addClothing() {
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
            
        } catch(SQLException e) {
            System.err.println("Error al agregar prenda: " + e.getMessage());
            if(e.getErrorCode() == 942) { 
                createClothesTable();
                addClothing();
            }
        }
    }
    
    private static void createClothesTable() {
        try {
            System.out.println("Creando tabla ropa...");
            
            String sql = "CREATE TABLE ropa (" +
                         "id NUMBER PRIMARY KEY," +
                         "tipo VARCHAR2(50) NOT NULL," +
                         "color VARCHAR2(30) NOT NULL," +
                         "talla VARCHAR2(10) NOT NULL," +
                         "precio NUMBER(10,2) NOT NULL," +
                         "fecha_ingreso DATE NOT NULL)";
            
            Statement stmt = oracleConn.createStatement();
            stmt.execute(sql);
            
            stmt.execute("CREATE SEQUENCE ropa_seq START WITH 1 INCREMENT BY 1");
            
            System.out.println("Tabla ropa creada exitosamente!");
        } catch(SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }
    
    private static void showAllClothes() {
        try {
            System.out.println("\n=== INVENTARIO DE ROPA ===");
            
            Statement stmt = oracleConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ropa ORDER BY id");
            
            while(rs.next()) {
                System.out.printf("ID: %d | Tipo: %s | Color: %s | Talla: %s | Precio: %.2f | Fecha: %s%n",
                                  rs.getInt("id"),
                                  rs.getString("tipo"),
                                  rs.getString("color"),
                                  rs.getString("talla"),
                                  rs.getDouble("precio"),
                                  rs.getDate("fecha_ingreso"));
            }
        } catch(SQLException e) {
            System.err.println("Error al mostrar ropa: " + e.getMessage());
        }
    }
    
    private static void findClothingByType() {
        try {
            System.out.print("\nIngrese tipo de prenda a buscar: ");
            String tipo = scanner.nextLine();
            
            String sql = "SELECT * FROM ropa WHERE LOWER(tipo) = LOWER(?)";
            
            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setString(1, tipo);
            
            ResultSet rs = pstmt.executeQuery();
            
            boolean encontrado = false;
            while(rs.next()) {
                encontrado = true;
                System.out.printf("ID: %d | Tipo: %s | Color: %s | Talla: %s | Precio: %.2f%n",
                                  rs.getInt("id"),
                                  rs.getString("tipo"),
                                  rs.getString("color"),
                                  rs.getString("talla"),
                                  rs.getDouble("precio"));
            }
            
            if(!encontrado) {
                System.out.println("No se encontraron prendas de ese tipo");
            }
        } catch(SQLException e) {
            System.err.println("Error al buscar prenda: " + e.getMessage());
        }
    }
    
    private static void deleteClothing() {
        try {
            System.out.print("\nIngrese ID de la prenda a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            
            String sql = "DELETE FROM ropa WHERE id = ?";
            
            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int deleted = pstmt.executeUpdate();
            
            if(deleted > 0) {
                System.out.println("Prenda eliminada con éxito!");
            } else {
                System.out.println("No se encontró prenda con ese ID");
            }
        } catch(SQLException e) {
            System.err.println("Error al eliminar prenda: " + e.getMessage());
        }
    }
    
    private static void showStatistics() {
        try {
            System.out.println("\n=== ESTADÍSTICAS ===");
            
            long userCount = mongoDb.getCollection("usuarios").countDocuments();
            System.out.println("Total usuarios registrados: " + userCount);
            
            Statement stmt = oracleConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total, SUM(precio) as valor FROM ropa");
            
            if(rs.next()) {
                System.out.println("Total prendas en inventario: " + rs.getInt("total"));
                System.out.printf("Valor total del inventario: $%.2f%n", rs.getDouble("valor"));
            }
            
        } catch(SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}
