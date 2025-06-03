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

public class PrettyPrinter {
    
    public static void printDocument(Document doc) {
        System.out.println("╔══════════════════════════════╗");
        doc.forEach((key, value) -> 
            System.out.printf("║ %-15s: %-10s ║%n", key, value)
        );
        System.out.println("╚══════════════════════════════╝");
    }
    
    public static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();
        
        // Calcular anchos de columnas
        int[] colWidths = new int[colCount];
        for (int i = 1; i <= colCount; i++) {
            colWidths[i-1] = Math.max(meta.getColumnDisplaySize(i), meta.getColumnLabel(i).length());
        }
        
        printLine(colWidths, "╔", "╦", "╗");
        printHeader(rs, meta, colWidths);
        printLine(colWidths, "╠", "╬", "╣");
        
        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
            printRow(rs, meta, colWidths);
            
            if (!rs.isAfterLast()) {
                printLine(colWidths, "╠", "╬", "╣");
            }
        }
        
        if (!hasRows) {
            printEmptyRow(colWidths);
        }
        
        printLine(colWidths, "╚", "╩", "╝");
    }
    
    private static void printLine(int[] widths, String left, String middle, String right) {
        System.out.print(left);
        for (int i = 0; i < widths.length; i++) {
            System.out.print("═".repeat(widths[i] + 2));
            if (i < widths.length - 1) System.out.print(middle);
        }
        System.out.println(right);
    }
    
    private static void printHeader(ResultSet rs, ResultSetMetaData meta, int[] widths) throws SQLException {
        System.out.print("║");
        for (int i = 1; i <= widths.length; i++) {
            System.out.printf(" %-" + widths[i-1] + "s ║", meta.getColumnLabel(i));
        }
        System.out.println();
    }
    
    private static void printRow(ResultSet rs, ResultSetMetaData meta, int[] widths) throws SQLException {
        System.out.print("║");
        for (int i = 1; i <= widths.length; i++) {
            Object value = rs.getObject(i);
            String strValue = (value != null) ? value.toString() : "NULL";
            System.out.printf(" %-" + widths[i-1] + "s ║", strValue);
        }
        System.out.println();
    }
    
    private static void printEmptyRow(int[] widths) {
        System.out.print("║");
        for (int i = 0; i < widths.length; i++) {
            System.out.printf(" %-" + widths[i] + "s ║", "NO DATA");
        }
        System.out.println();
    }
}
