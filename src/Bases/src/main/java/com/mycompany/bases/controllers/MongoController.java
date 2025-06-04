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

/**
 * Controlador para la gestión de usuarios en MongoDB.
 *
 * Esta clase proporciona una interfaz completa para realizar operaciones CRUD
 * (Create, Read, Update, Delete) sobre una colección de usuarios en MongoDB.
 *
 * Funcionalidades principales: - Conexión y configuración automática con
 * MongoDB - Gestión completa de usuarios (agregar, mostrar, buscar, eliminar) -
 * Interfaz de consola interactiva para el usuario - Manejo de recursos con
 * cierre adecuado de conexiones
 *
 * La clase utiliza el patrón Controller del MVC, encapsulando toda la lógica de
 * negocio relacionada con la gestión de usuarios en MongoDB.
 *
 * @author DarThunder
 * @version 1.0
 */
public class MongoController {

    /**
     * Cliente de conexión a MongoDB
     */
    private final MongoClient client;

    /**
     * Referencia a la base de datos MongoDB específica
     */
    private final MongoDatabase mongoDb;

    /**
     * Scanner para entrada de datos del usuario
     */
    private final Scanner scanner;

    /**
     * Constructor que inicializa la conexión a MongoDB y los recursos
     * necesarios.
     *
     * Establece la conexión utilizando la configuración definida en
     * DatabaseConfig, selecciona la base de datos correspondiente e inicializa
     * el scanner para la entrada de datos del usuario.
     *
     * La conexión se establece de forma síncrona y está lista para usar
     * inmediatamente después de la construcción del objeto.
     */
    public MongoController() {
        // Establece conexión con MongoDB usando la URI de configuración
        this.client = MongoClients.create(DatabaseConfig.MONGO_URI);

        // Selecciona la base de datos específica según configuración
        this.mongoDb = client.getDatabase(DatabaseConfig.MONGO_DB);

        // Inicializa scanner para entrada de datos del usuario
        this.scanner = new Scanner(System.in);
    }

    /**
     * Método principal para la gestión interactiva de usuarios.
     *
     * Presenta un menú de opciones al usuario y maneja la navegación entre las
     * diferentes operaciones disponibles. El menú se repite hasta que el
     * usuario seleccione la opción de salir.
     *
     * Opciones disponibles: 1. Agregar usuario - Crea un nuevo usuario en la
     * colección 2. Mostrar todos los usuarios - Lista todos los usuarios
     * existentes 3. Buscar usuario por nombre - Encuentra un usuario específico
     * 4. Eliminar usuario - Borra un usuario de la colección 0. Volver al menú
     * principal - Sale del módulo de gestión
     *
     * Utiliza un bucle do-while para mantener el menú activo y switch
     * expressions para un manejo limpio de las opciones.
     */
    public void manageUsers() {
        int opcion;
        do {
            // Muestra el menú de opciones disponibles
            System.out.println("\n=== GESTIÓN DE USUARIOS (MongoDB) ===");
            System.out.println("1. Agregar usuario");
            System.out.println("2. Mostrar todos los usuarios");
            System.out.println("3. Buscar usuario por nombre");
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            // Lee la opción del usuario
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consume el salto de línea pendiente

            // Procesa la opción seleccionada usando switch expression
            switch (opcion) {
                case 1 ->
                    addUser();                    // Agregar nuevo usuario
                case 2 ->
                    showAllUsers();               // Mostrar todos los usuarios
                case 3 ->
                    findUserByName();             // Buscar usuario específico
                case 4 ->
                    deleteUser();                 // Eliminar usuario
                case 0 ->
                    System.out.println("Volviendo al menú principal..."); // Salir
                default ->
                    System.out.println("Opción no válida"); // Opción inválida
            }
        } while (opcion != 0); // Continúa hasta que el usuario elija salir
    }

    /**
     * Agrega un nuevo usuario a la colección de MongoDB.
     *
     * Solicita al usuario los datos necesarios (nombre, email, edad) y crea un
     * documento MongoDB con esta información más la fecha de registro
     * automática.
     *
     * Campos del documento creado: - nombre: String con el nombre del usuario -
     * email: String con el correo electrónico - edad: Integer con la edad -
     * fechaRegistro: Date con la fecha y hora actual de creación
     *
     * La operación es atómica y se confirma inmediatamente en la base de datos.
     */
    private void addUser() {
        // Solicita los datos del nuevo usuario
        System.out.print("\nIngrese nombre del usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese email: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea pendiente

        // Crea el documento con los datos del usuario
        Document user = new Document("nombre", nombre)
                .append("email", email)
                .append("edad", edad)
                .append("fechaRegistro", new java.util.Date()); // Fecha automática

        // Inserta el documento en la colección "usuarios"
        mongoDb.getCollection("usuarios").insertOne(user);

        System.out.println("Usuario agregado con éxito!");
    }

    /**
     * Muestra todos los usuarios existentes en la colección.
     *
     * Realiza una consulta que recupera todos los documentos de la colección
     * "usuarios" y los muestra utilizando el método printDocument para un
     * formato visualmente atractivo.
     *
     * Si no hay usuarios en la colección, simplemente no mostrará ningún
     * resultado. La consulta es eficiente y recupera todos los documentos en
     * una sola operación.
     */
    public void showAllUsers() {
        System.out.println("\n=== LISTA DE USUARIOS ===");

        // Itera sobre todos los documentos de la colección "usuarios"
        for (Document user : mongoDb.getCollection("usuarios").find()) {
            // Utiliza método auxiliar para mostrar cada documento formateado
            printDocument(user);
        }
    }

    /**
     * Busca y muestra un usuario específico por su nombre.
     *
     * Solicita al usuario el nombre a buscar y realiza una consulta exacta
     * utilizando el filtro eq() de MongoDB. Si encuentra el usuario, lo muestra
     * formateado; si no, informa que no fue encontrado.
     *
     * La búsqueda es case-sensitive y debe coincidir exactamente con el nombre
     * almacenado en la base de datos.
     */
    public void findUserByName() {
        // Solicita el nombre a buscar
        System.out.print("\nIngrese nombre a buscar: ");
        String nombre = scanner.nextLine();

        // Busca el primer documento que coincida con el nombre
        Document user = mongoDb.getCollection("usuarios")
                .find(eq("nombre", nombre)) // Filtro de igualdad exacta
                .first();                    // Obtiene solo el primer resultado

        // Verifica si se encontró el usuario
        if (user != null) {
            printDocument(user); // Muestra el usuario encontrado
        } else {
            System.out.println("Usuario no encontrado");
        }
    }

    public String findUserByIndex(int index) {
    Document user = mongoDb.getCollection("usuarios")
                           .find()
                           .skip(index)
                           .first();

    if (user != null) {
        return user.getString("nombre");
    } else {
        return null;
    }
}

    /**
     * Elimina un usuario de la colección por su nombre.
     *
     * Solicita al usuario el nombre del usuario a eliminar y realiza la
     * operación de borrado. Informa el resultado de la operación basándose en
     * el número de documentos eliminados.
     *
     * La operación elimina únicamente el primer documento que coincida con el
     * nombre especificado (deleteOne). Si existen múltiples usuarios con el
     * mismo nombre, solo se eliminará uno.
     */
    public void deleteUser() {
        // Solicita el nombre del usuario a eliminar
        System.out.print("\nIngrese nombre del usuario a eliminar: ");
        String nombre = scanner.nextLine();

        // Realiza la operación de eliminación y obtiene el conteo de documentos borrados
        long deletedCount = mongoDb.getCollection("usuarios")
                .deleteOne(eq("nombre", nombre)) // Elimina un documento que coincida
                .getDeletedCount();               // Obtiene cantidad de documentos eliminados

        // Informa el resultado de la operación
        if (deletedCount > 0) {
            System.out.println("Usuario eliminado con éxito!");
        } else {
            System.out.println("No se encontró usuario con ese nombre");
        }
    }

    /**
     * Cierra la conexión con MongoDB liberando recursos.
     *
     * Es importante llamar este método al finalizar el uso del controlador para
     * evitar memory leaks y liberar adecuadamente las conexiones de red.
     *
     * Debe ser llamado en un bloque finally o try-with-resources para
     * garantizar que se ejecute incluso si ocurren excepciones.
     */
    public void close() {
        // Cierra la conexión del cliente MongoDB
        client.close();
    }
}
