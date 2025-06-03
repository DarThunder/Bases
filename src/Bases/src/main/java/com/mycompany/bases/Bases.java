/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bases;

import com.mycompany.bases.views.MenuView;

/**
 *
 * @author dard
 */


public class Bases {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando aplicación de gestión...");
            MenuView menu = new MenuView();
            menu.showMainMenu();
        } catch (Exception e) {
            System.err.println("Error inicializando la aplicación:");
            e.printStackTrace();
        }
    }
}
