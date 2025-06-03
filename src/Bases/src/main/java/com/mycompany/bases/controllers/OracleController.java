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

/**
 * Controlador para la gestión de productos y ventas en base de datos Oracle.
 * Proporciona funcionalidades CRUD para el inventario de ropa y sistema de ventas.
 * 
 * <p>Esta clase maneja la conexión con Oracle Database y ofrece interfaces
 * de usuario por consola para:</p>
 * <ul>
 *   <li>Gestión de productos (ropa)</li>
 *   <li>Gestión de ventas</li>
 *   <li>Creación de ventas completas con detalles</li>
 * </ul>
 * 
 * @author DarThunder
 * @version 1.0
 * @since 2025
 */
public class OracleController {

    /** Conexión a la base de datos Oracle */
    private final Connection oracleConn;
    
    /** Scanner para entrada de datos por consola */
    private final Scanner scanner;

    /**
     * Constructor que inicializa la conexión a Oracle Database y el scanner.
     * 
     * @throws SQLException si ocurre un error al conectar con la base de datos
     */
    public OracleController() throws SQLException {
        this.oracleConn = DriverManager.getConnection(
                DatabaseConfig.ORACLE_URL,
                DatabaseConfig.ORACLE_USER,
                DatabaseConfig.ORACLE_PASSWORD
        );
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra el menú principal para la gestión de productos de ropa.
     * Permite agregar, mostrar, buscar y eliminar productos del inventario.
     * 
     * <p>Opciones disponibles:</p>
     * <ul>
     *   <li>1. Agregar prenda</li>
     *   <li>2. Mostrar todo el inventario</li>
     *   <li>3. Buscar prenda por tipo</li>
     *   <li>4. Eliminar prenda</li>
     *   <li>0. Volver al menú principal</li>
     * </ul>
     */
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
                    addProduct();
                case 2 ->
                    showAllProducts();
                case 3 ->
                    findProductByCategory();
                case 4 ->
                    deleteProduct();
                case 0 ->
                    System.out.println("Volviendo al menú principal...");
                default ->
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    /**
     * Agrega un nuevo producto al inventario.
     * Solicita al usuario los datos del producto: nombre, categoría, color, talla y precio.
     * 
     * <p>Los datos se insertan en la tabla Producto usando una secuencia para el ID.</p>
     * 
     * @see #showAllProducts()
     * @see #findProductByCategory()
     */
    public void addProduct() {
        try {
            System.out.print("\nIngrese nombre del producto: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese categoría (camisa/pantalón/vestido/otros): ");
            String categoria = scanner.nextLine();

            System.out.print("Ingrese color: ");
            String color = scanner.nextLine();

            System.out.print("Ingrese talla: ");
            String talla = scanner.nextLine();

            System.out.print("Ingrese precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            String sql = "INSERT INTO Producto (idProducto, nombre, precio, Categorias, color, Talla) VALUES (producto_seq.NEXTVAL, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setString(3, categoria);
            pstmt.setString(4, color);
            pstmt.setString(5, talla);

            pstmt.executeUpdate();
            System.out.println("Producto agregado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
        }
    }

    /**
     * Muestra todos los productos del inventario en formato tabular.
     * Los productos se ordenan por ID de forma ascendente.
     * 
     * <p>Información mostrada por producto:</p>
     * <ul>
     *   <li>ID del producto</li>
     *   <li>Nombre</li>
     *   <li>Categoría</li>
     *   <li>Precio</li>
     *   <li>Color</li>
     *   <li>Talla</li>
     * </ul>
     */
    public void showAllProducts() {
        try {
            System.out.println("\n=== INVENTARIO DE PRODUCTOS ===");

            Statement stmt = oracleConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Producto ORDER BY idProducto");

            System.out.printf("%-5s %-20s %-15s %-12s %-10s %-8s%n",
                    "ID", "NOMBRE", "CATEGORÍA", "PRECIO", "COLOR", "TALLA");
            System.out.println("─".repeat(75));

            while (rs.next()) {
                printResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar productos: " + e.getMessage());
        }
    }

    /**
     * Busca y muestra productos por categoría.
     * La búsqueda es insensible a mayúsculas y minúsculas.
     * 
     * @see #showAllProducts()
     */
    public void findProductByCategory() {
        try {
            System.out.print("\nIngrese categoría a buscar: ");
            String categoria = scanner.nextLine();

            String sql = "SELECT * FROM Producto WHERE LOWER(Categorias) = LOWER(?)";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setString(1, categoria);

            ResultSet rs = pstmt.executeQuery();

            boolean encontrado = false;
            System.out.printf("%-5s %-20s %-15s %-12s %-10s %-8s%n",
                    "ID", "NOMBRE", "CATEGORÍA", "PRECIO", "COLOR", "TALLA");
            System.out.println("─".repeat(75));

            while (rs.next()) {
                encontrado = true;
                printResultSet(rs);
            }

            if (!encontrado) {
                System.out.println("No se encontraron productos de esa categoría");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto del inventario por su ID.
     * 
     * @see #addProduct()
     */
    public void deleteProduct() {
        try {
            System.out.print("\nIngrese ID del producto a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM Producto WHERE idProducto = ?";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int deleted = pstmt.executeUpdate();

            if (deleted > 0) {
                System.out.println("Producto eliminado con éxito!");
            } else {
                System.out.println("No se encontró producto con ese ID");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú principal para la gestión de ventas.
     * 
     * <p>Opciones disponibles:</p>
     * <ul>
     *   <li>1. Agregar venta</li>
     *   <li>2. Mostrar todas las ventas</li>
     *   <li>3. Buscar venta por ID</li>
     *   <li>4. Actualizar venta</li>
     *   <li>5. Eliminar venta</li>
     *   <li>0. Volver al menú principal</li>
     * </ul>
     * 
     * @see #createCompleteVenta()
     */
    public void manageVentas() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE VENTAS (OracleDB) ===");
            System.out.println("1. Agregar venta");
            System.out.println("2. Mostrar todas las ventas");
            System.out.println("3. Buscar venta por ID");
            System.out.println("4. Actualizar venta");
            System.out.println("5. Eliminar venta");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 ->
                    addVenta();
                case 2 ->
                    showAllVentas();
                case 3 ->
                    findVentaById();
                case 5 ->
                    deleteVenta();
                case 0 ->
                    System.out.println("Volviendo al menú principal...");
                default ->
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    /**
     * Agrega una nueva venta básica al sistema.
     * Solicita el ID del usuario (cliente) y el total de la venta.
     * La fecha se establece automáticamente con SYSDATE.
     * 
     * @see #createCompleteVenta()
     */
    public void addVenta() {
        try {
            System.out.print("\nIngrese ID del usuario (cliente): ");
            int idUsuario = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ingrese total de la venta: ");
            double total = scanner.nextDouble();
            scanner.nextLine();

            String sql = "INSERT INTO Ventas (idVenta, fecha, total, idUsuario) VALUES (venta_seq.NEXTVAL, SYSDATE, ?, ?)";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setDouble(1, total);
            pstmt.setInt(2, idUsuario);

            pstmt.executeUpdate();
            System.out.println("Venta agregada con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al agregar venta: " + e.getMessage());
        }
    }

    /**
     * Muestra todas las ventas registradas en el sistema.
     * Incluye información del cliente mediante JOIN con la tabla Usuario.
     * 
     * <p>Información mostrada por venta:</p>
     * <ul>
     *   <li>ID de la venta</li>
     *   <li>Fecha</li>
     *   <li>Total</li>
     *   <li>Nombre del cliente</li>
     * </ul>
     */
    public void showAllVentas() {
        try {
            System.out.println("\n=== REGISTRO DE VENTAS ===");

            String sql = "SELECT v.idVenta, v.fecha, v.total, u.Username as cliente "
                    + "FROM Ventas v "
                    + "LEFT JOIN Usuario u ON v.idUsuario = u.idUsuario "
                    + "ORDER BY v.idVenta";

            Statement stmt = oracleConn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.printf("%-5s %-15s %-12s %-20s%n", "ID", "FECHA", "TOTAL", "CLIENTE");
            System.out.println("─".repeat(55));

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                printResultSet(rs);
            }

            if (!hasResults) {
                System.out.println("No hay ventas registradas.");
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar ventas: " + e.getMessage());
        }
    }

    /**
     * Busca y muestra una venta específica por su ID.
     * También muestra los detalles de productos asociados a la venta.
     * 
     * @see #showVentaDetails(int)
     */
    public void findVentaById() {
        try {
            System.out.print("\nIngrese ID de la venta a buscar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "SELECT v.idVenta, v.fecha, v.total, u.Username as cliente "
                    + "FROM Ventas v "
                    + "LEFT JOIN Usuario u ON v.idUsuario = u.idUsuario "
                    + "WHERE v.idVenta = ?";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                printResultSet(rs);
                showVentaDetails(id);
            } else {
                System.out.println("No se encontró venta con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }
    }

    /**
     * Elimina una venta del sistema.
     * Primero elimina los detalles de la venta y luego la venta principal.
     * Solicita confirmación antes de proceder con la eliminación.
     * 
     * @see #ventaExists(int)
     */
    public void deleteVenta() {
        try {
            System.out.print("\nIngrese ID de la venta a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            if (!ventaExists(id)) {
                System.out.println("No se encontró venta con ID: " + id);
                return;
            }

            System.out.print("¿Está seguro de eliminar esta venta? (s/n): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("s")) {
                String sqlDetails = "DELETE FROM DetallesVenta WHERE idVenta = ?";
                PreparedStatement pstmtDetails = oracleConn.prepareStatement(sqlDetails);
                pstmtDetails.setInt(1, id);
                pstmtDetails.executeUpdate();

                String sql = "DELETE FROM Ventas WHERE idVenta = ?";
                PreparedStatement pstmt = oracleConn.prepareStatement(sql);
                pstmt.setInt(1, id);
                int deleted = pstmt.executeUpdate();

                if (deleted > 0) {
                    System.out.println("Venta eliminada con éxito!");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
        }
    }

    /**
     * Verifica si existe una venta con el ID especificado.
     * 
     * @param id el ID de la venta a verificar
     * @return true si la venta existe, false en caso contrario
     */
    private boolean ventaExists(int id) {
        try {
            String sql = "SELECT COUNT(*) FROM Ventas WHERE idVenta = ?";
            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de venta: " + e.getMessage());
        }
        return false;
    }

    /**
     * Muestra los detalles de productos de una venta específica.
     * Incluye cantidad, subtotal y nombre del producto.
     * 
     * @param idVenta el ID de la venta cuyos detalles se mostrarán
     */
    private void showVentaDetails(int idVenta) {
        try {
            String sql = "SELECT dv.Cantidad, dv.Subtotal, p.nombre as producto "
                    + "FROM DetallesVenta dv "
                    + "JOIN Producto p ON dv.idProducto = p.idProducto "
                    + "WHERE dv.idVenta = ?";

            PreparedStatement pstmt = oracleConn.prepareStatement(sql);
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();

            boolean hasDetails = false;
            while (rs.next()) {
                if (!hasDetails) {
                    printResultSet(rs);
                    hasDetails = true;
                }

                System.out.printf("%-20s %-10d $%-11.2f%n",
                        rs.getString("producto"),
                        rs.getInt("Cantidad"),
                        rs.getDouble("Subtotal"));
            }

            if (!hasDetails) {
                System.out.println("(Sin detalles de productos)");
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar detalles de venta: " + e.getMessage());
        }
    }

    /**
     * Crea una venta completa con sus detalles de productos.
     * Permite agregar múltiples productos a la venta y calcula automáticamente
     * el total basado en los subtotales de cada producto.
     * 
     * <p>Proceso:</p>
     * <ol>
     *   <li>Crea la venta con total inicial de 0</li>
     *   <li>Permite agregar productos uno por uno</li>
     *   <li>Calcula subtotales automáticamente</li>
     *   <li>Actualiza el total final de la venta</li>
     * </ol>
     * 
     * @see #addVenta()
     * @see #manageVentas()
     */
    public void createCompleteVenta() {
        try {
            System.out.print("\nIngrese ID del usuario (cliente): ");
            int idUsuario = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement pstmt = oracleConn.prepareStatement(
                    "INSERT INTO Ventas (idVenta, fecha, total, idUsuario) VALUES (venta_seq.NEXTVAL, SYSDATE, 0, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            int idVenta = 0;
            if (generatedKeys.next()) {
                idVenta = generatedKeys.getInt(1);
            }

            System.out.println("Venta creada con ID: " + idVenta);

            double totalVenta = 0;
            String continuar = "s";

            while (continuar.equalsIgnoreCase("s")) {
                System.out.print("Ingrese ID del producto: ");
                int idProducto = scanner.nextInt();

                System.out.print("Ingrese cantidad: ");
                int cantidad = scanner.nextInt();
                scanner.nextLine();

                PreparedStatement pstmtPrecio = oracleConn.prepareStatement("SELECT precio, nombre FROM Producto WHERE idProducto = ?");
                pstmtPrecio.setInt(1, idProducto);
                ResultSet rs = pstmtPrecio.executeQuery();

                if (rs.next()) {
                    double precio = rs.getDouble("precio");
                    String nombreProducto = rs.getString("nombre");
                    double subtotal = precio * cantidad;
                    totalVenta += subtotal;

                    PreparedStatement pstmtDetalle = oracleConn.prepareStatement(
                            "INSERT INTO DetallesVenta (idVenta, idProducto, Cantidad, Subtotal) VALUES (?, ?, ?, ?)");
                    pstmtDetalle.setInt(1, idVenta);
                    pstmtDetalle.setInt(2, idProducto);
                    pstmtDetalle.setInt(3, cantidad);
                    pstmtDetalle.setDouble(4, subtotal);
                    pstmtDetalle.executeUpdate();

                    System.out.println("Producto '" + nombreProducto + "' agregado - Subtotal: $" + subtotal);
                } else {
                    System.out.println("Producto no encontrado");
                }

                System.out.print("¿Agregar otro producto? (s/n): ");
                continuar = scanner.nextLine();
            }

            PreparedStatement pstmtTotal = oracleConn.prepareStatement("UPDATE Ventas SET total = ? WHERE idVenta = ?");
            pstmtTotal.setDouble(1, totalVenta);
            pstmtTotal.setInt(2, idVenta);
            pstmtTotal.executeUpdate();

            System.out.println("Venta completada - Total: $" + totalVenta);

        } catch (SQLException e) {
            System.err.println("Error al crear venta: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para imprimir resultados de consultas.
     * Este método debe ser implementado según el formato específico
     * requerido para mostrar los datos del ResultSet.
     * 
     * @param rs el ResultSet con los datos a imprimir
     * @throws SQLException si ocurre un error al leer los datos
     */
    private void printResultSet(ResultSet rs) throws SQLException {
        // Implementación específica para formatear y mostrar los datos
        // del ResultSet según el contexto de la consulta
    }

    /**
     * Cierra la conexión a la base de datos Oracle.
     * Debe llamarse al finalizar el uso del controlador para liberar recursos.
     * 
     * @throws SQLException si ocurre un error al cerrar la conexión
     */
    public void close() throws SQLException {
        oracleConn.close();
    }
}