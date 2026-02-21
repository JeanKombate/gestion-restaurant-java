/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.controllers;

import com.restaurant.dao.LogDAO;
import com.restaurant.views.LoginView;
import com.restaurant.views.MainMenuView;
import com.restaurant.dao.UtilisateurDAO;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class LoginController {
    private final LoginView loginView;
    private final UtilisateurDAO utilisateurDAO;
    
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.utilisateurDAO = new UtilisateurDAO();
        
        // Ajouter les listeners
        this.loginView.addLoginListener(new LoginListener());
        this.loginView.addCancelListener(new CancelListener());
    }
    
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = loginView.getLogin();
            String password = loginView.getPassword();
            
            // Validation
            if (login.isEmpty() || password.isEmpty()) {
                loginView.showMessage("Veuillez remplir tous les champs", true);
                return;
            }
            
            // Authentification
            var utilisateur = utilisateurDAO.authentifier(login, password);
            
                        // Après authentification réussie
            //Utilisateur utilisateur = utilisateurDAO.authenticate(login, password);

            if (utilisateur != null) {
                // IMPORTANT : Enregistrer la session
                SessionManager.getInstance().login(utilisateur);

                // IMPORTANT : Logger la connexion
                LogDAO logDAO = new LogDAO();
                logDAO.logLogin(utilisateur.getId(), utilisateur.getLogin());

                // Fermer la fenêtre de login
                loginView.dispose();

                // OUVRIR LE MENU PRINCIPAL
                openMainMenu();  // ← Cette ligne est-elle présente ?

            } else {
                // Afficher erreur
                loginView.showError("Login ou mot de passe incorrect");
            }
        }
    }
    
    class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(
                loginView,
                "Voulez-vous vraiment quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
    
    private void openMainMenu() {
    SwingUtilities.invokeLater(() -> {
        MainMenuView mainMenu = new MainMenuView();
         new MainMenuController(mainMenu);
        mainMenu.setVisible(true);
    });
    }
   
}
