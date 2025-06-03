/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bases.config;

/**
 *
 * @author dard
 */
public class DatabaseConfig {
    public static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    public static final String ORACLE_USER = "system";
    public static final String ORACLE_PASSWORD = "tu_contraseña";
    
    public static final String MONGO_URI = "mongodb://localhost:27017";
    public static final String MONGO_DB = "tiendaDB";
}