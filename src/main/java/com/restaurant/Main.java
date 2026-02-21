/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant;

import com.restaurant.views.LoginView;
import com.restaurant.controllers.LoginController;
import javax.swing.*;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class Main {
    public static void main(String[] args) {
        // Pour tester directement sans login, décommentez les lignes ci-dessous
        // et commentez le code du login
        
        /*
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Test direct de la gestion des produits
                com.restaurant.views.GestionProduitsView testView = new com.restaurant.views.GestionProduitsView();
                testView.setVisible(true);
                
                // Ou test du menu principal
                // com.restaurant.views.MainMenuView mainView = new com.restaurant.views.MainMenuView();
                // new com.restaurant.controllers.MainMenuController(mainView);
                // mainView.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        */
        
        // Code original avec login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    LoginView loginView = new LoginView();
                    LoginController loginController = new LoginController(loginView);
                    
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Erreur au démarrage: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }
}