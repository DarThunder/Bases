/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bases.controllers;

/**
 *
 * @author dard
 */
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.mycompany.bases.config.DatabaseConfig;
import static com.mycompany.bases.utils.PrettyPrinter.printDocument;
import java.util.Scanner;

public class MongoController {
    private final MongoClient client;
    private final MongoDatabase mongoDb;
    private final Scanner scanner;
    
    public MongoController() {
        this.client = MongoClients.create(DatabaseConfig.MONGO_URI);
        this.mongoDb = client.getDatabase(DatabaseConfig.MONGO_DB);
        this.scanner = new Scanner(System.in);
    }
    
    public void manageUsers() {
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

            switch (opcion) {
                case 1 ->
                    addUser();
                case 2 ->
                    showAllUsers();
                case 3 ->
                    findUserByName();
                case 4 ->
                    deleteUser();
                case 0 ->
                    System.out.println("Volviendo al menú principal...");
                default ->
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private void addUser() {
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

    public void showAllUsers() {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        for (Document user : mongoDb.getCollection("usuarios").find()) {
            printDocument(user);
        }
    }

    public void findUserByName() {
        System.out.print("\nIngrese nombre a buscar: ");
        String nombre = scanner.nextLine();

        Document user = mongoDb.getCollection("usuarios").find(eq("nombre", nombre)).first();

        if (user != null) {
            printDocument(user);
        } else {
            System.out.println("Usuario no encontrado");
        }
    }

    public void deleteUser() {
        System.out.print("\nIngrese nombre del usuario a eliminar: ");
        String nombre = scanner.nextLine();

        long deletedCount = mongoDb.getCollection("usuarios").deleteOne(eq("nombre", nombre)).getDeletedCount();

        if (deletedCount > 0) {
            System.out.println("Usuario eliminado con éxito!");
        } else {
            System.out.println("No se encontró usuario con ese nombre");
        }
    }
    
    public void close(){
        client.close();
    }
}