/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bases.utils;

/**
 *
 * @author dard
 */
import org.bson.Document;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Clase utilitaria para imprimir datos de manera visualmente atractiva en consola.
 * 
 * Proporciona métodos estáticos para formatear e imprimir:
 * - Documentos de MongoDB con bordes decorativos
 * - ResultSets de base de datos en formato tabular
 * 
 * Utiliza caracteres Unicode para crear bordes y separadores visuales.
 * 
 * @author DarThunder
 * @version 1.0
 */
public class PrettyPrinter {
    
    /**
     * Imprime un documento de MongoDB en formato de caja con bordes decorativos.
     * 
     * Crea una tabla visual con caracteres Unicode donde cada par clave-valor
     * del documento se muestra en una fila separada con formato fijo.
     * 
     * @param doc El documento de MongoDB a imprimir
     * 
     * Ejemplo de salida:
     * ╔══════════════════════════════╗
     * ║ nombre         : Juan       ║
     * ║ edad           : 25         ║
     * ╚══════════════════════════════╝
     */
    public static void printDocument(Document doc) {
        // Imprime la línea superior de la caja
        System.out.println("╔══════════════════════════════╗");
        
        // Itera sobre cada par clave-valor del documento
        // Formatea: 15 caracteres para clave, 10 para valor, alineado a la izquierda
        doc.forEach((key, value) -> 
            System.out.printf("║ %-15s: %-10s ║%n", key, value)
        );
        
        // Imprime la línea inferior de la caja
        System.out.println("╚══════════════════════════════╝");
    }
    
    /**
     * Imprime un ResultSet de base de datos en formato de tabla con bordes.
     * 
     * Características:
     * - Calcula automáticamente el ancho óptimo para cada columna
     * - Imprime encabezados de columna con separadores
     * - Maneja valores nulos mostrando "NULL"
     * - Muestra "NO DATA" si el ResultSet está vacío
     * - Utiliza separadores entre filas para mayor claridad
     * 
     * @param rs El ResultSet de una consulta SQL a imprimir
     * @throws SQLException Si ocurre un error al acceder a los metadatos o datos
     */
    public static void printResultSet(ResultSet rs) throws SQLException {
        // Obtiene los metadatos para información sobre las columnas
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();
        
        // Calcular anchos óptimos de columnas
        // Toma el máximo entre el tamaño de visualización y la longitud del nombre
        int[] colWidths = new int[colCount];
        for (int i = 1; i <= colCount; i++) {
            colWidths[i-1] = Math.max(
                meta.getColumnDisplaySize(i), 
                meta.getColumnLabel(i).length()
            );
        }
        
        // Construye la tabla paso a paso
        printLine(colWidths, "╔", "╦", "╗");  // Línea superior
        printHeader(rs, meta, colWidths);     // Encabezados
        printLine(colWidths, "╠", "╬", "╣");  // Separador después de encabezados
        
        // Procesa todas las filas de datos
        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
            printRow(rs, meta, colWidths);    // Imprime la fila actual
            
            // Agrega separador entre filas (excepto después de la última)
            if (!rs.isAfterLast()) {
                printLine(colWidths, "╠", "╬", "╣");
            }
        }
        
        // Si no hay datos, muestra una fila indicativa
        if (!hasRows) {
            printEmptyRow(colWidths);
        }
        
        // Línea inferior de la tabla
        printLine(colWidths, "╚", "╩", "╝");
    }
    
    /**
     * Imprime una línea horizontal de separación en la tabla.
     * 
     * Construye dinámicamente la línea basándose en los anchos de columna
     * y utiliza diferentes caracteres para bordes e intersecciones.
     * 
     * @param widths Array con los anchos calculados para cada columna
     * @param left Carácter para el borde izquierdo (ej: ╔, ╠, ╚)
     * @param middle Carácter para las intersecciones (ej: ╦, ╬, ╩)
     * @param right Carácter para el borde derecho (ej: ╗, ╣, ╝)
     */
    private static void printLine(int[] widths, String left, String middle, String right) {
        System.out.print(left);
        
        // Para cada columna, imprime el ancho correspondiente de caracteres ═
        for (int i = 0; i < widths.length; i++) {
            // +2 para los espacios de padding alrededor del contenido
            System.out.print("═".repeat(widths[i] + 2));
            
            // Agrega intersección entre columnas (excepto después de la última)
            if (i < widths.length - 1) {
                System.out.print(middle);
            }
        }
        
        System.out.println(right);
    }
    
    /**
     * Imprime la fila de encabezados de la tabla.
     * 
     * Extrae los nombres de las columnas de los metadatos del ResultSet
     * y los formatea con el ancho calculado para cada columna.
     * 
     * @param rs El ResultSet (usado para consistencia, aunque no se lee aquí)
     * @param meta Los metadatos del ResultSet que contienen información de columnas
     * @param widths Array con los anchos calculados para cada columna
     * @throws SQLException Si hay error al acceder a los metadatos
     */
    private static void printHeader(ResultSet rs, ResultSetMetaData meta, int[] widths) throws SQLException {
        System.out.print("║");
        
        // Imprime cada nombre de columna con formato y padding
        for (int i = 1; i <= widths.length; i++) {
            // Formato: espacio + texto alineado a la izquierda + espacio + separador
            System.out.printf(" %-" + widths[i-1] + "s ║", meta.getColumnLabel(i));
        }
        
        System.out.println();
    }
    
    /**
     * Imprime una fila de datos de la tabla.
     * 
     * Extrae los valores de cada columna del ResultSet actual,
     * maneja valores nulos convirtiéndolos a "NULL" y aplica
     * el formateo consistente con el resto de la tabla.
     * 
     * @param rs El ResultSet posicionado en la fila a imprimir
     * @param meta Los metadatos del ResultSet
     * @param widths Array con los anchos calculados para cada columna
     * @throws SQLException Si hay error al acceder a los datos
     */
    private static void printRow(ResultSet rs, ResultSetMetaData meta, int[] widths) throws SQLException {
        System.out.print("║");
        
        // Procesa cada columna de la fila actual
        for (int i = 1; i <= widths.length; i++) {
            // Obtiene el valor como Object para manejar cualquier tipo de dato
            Object value = rs.getObject(i);
            
            // Convierte a String, manejando valores nulos
            String strValue = (value != null) ? value.toString() : "NULL";
            
            // Aplica el mismo formato que los encabezados
            System.out.printf(" %-" + widths[i-1] + "s ║", strValue);
        }
        
        System.out.println();
    }
    
    /**
     * Imprime una fila especial indicando que no hay datos disponibles.
     * 
     * Se utiliza cuando el ResultSet está vacío para informar al usuario
     * de manera clara que la consulta no devolvió resultados.
     * 
     * @param widths Array con los anchos calculados para cada columna
     */
    private static void printEmptyRow(int[] widths) {
        System.out.print("║");
        
        // Imprime "NO DATA" en cada columna manteniendo el formato
        for (int i = 0; i < widths.length; i++) {
            System.out.printf(" %-" + widths[i] + "s ║", "NO DATA");
        }
        
        System.out.println();
    }
}