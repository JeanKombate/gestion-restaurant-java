/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.controllers;

import com.restaurant.views.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class MainMenuController {
    private final MainMenuView mainMenuView;
    
    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        addMenuListeners();
    }
    
    private void addMenuListeners() {
        System.out.println("✅ MainMenuController : Listeners ajoutés !");
    
        // Menu Gestion
        mainMenuView.getItemProduits().addActionListener(e -> {
            System.out.println("🔵 Clic sur Gestion des produits");
            openGestionProduits();
        });
        // Menu Gestion
        mainMenuView.getItemProduits().addActionListener(e -> openGestionProduits());
        mainMenuView.getItemCategories().addActionListener(e -> openGestionCategories());
        mainMenuView.getItemUtilisateurs().addActionListener(e -> openGestionUtilisateurs());
        
        // Menu Stock
        mainMenuView.getItemEntreeStock().addActionListener(e -> openEntreeStock());
        mainMenuView.getItemSortieStock().addActionListener(e -> openSortieStock());
        mainMenuView.getItemHistorique().addActionListener(e -> openHistoriqueStock());
        mainMenuView.getItemAlertesStock().addActionListener(e -> openAlertesStock());
        
        // Menu Commandes
        mainMenuView.getItemNouvelleCommande().addActionListener(e -> openNouvelleCommande());
        mainMenuView.getItemListeCommandes().addActionListener(e -> openListeCommandes());
        
        // Menu Statistiques
        mainMenuView.getItemCAByDate().addActionListener(e -> openCAByDate());
        mainMenuView.getItemTopProduits().addActionListener(e -> openTopProduits());
        
        // Menu Aide
        mainMenuView.getItemAide().addActionListener(e -> openAide());
        mainMenuView.getItemAPropos().addActionListener(e -> showAPropos());
        mainMenuView.getItemQuitter().addActionListener(e -> quitterApplication());
    }
    
    private void openGestionProduits() {
        try {
            System.out.println("Ouverture de GestionProduitsView...");
            GestionProduitsView view = new GestionProduitsView();

            // Positionnement correct
            view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            view.setSize(900, 600);
            view.setLocationRelativeTo(mainMenuView); // Centre par rapport à la fenêtre principale
            view.setVisible(true);

            System.out.println("GestionProduitsView ouverte avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainMenuView,
                "Erreur lors de l'ouverture : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openGestionCategories() {
        GestionCategoriesView view = new GestionCategoriesView();
        view.setVisible(true);
    }
    
    private void openGestionUtilisateurs() {
        // À implémenter plus tard
        JOptionPane.showMessageDialog(mainMenuView,
            "Gestion des utilisateurs - À implémenter dans une version future",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openEntreeStock() {
        EntreeStockView dialog = new EntreeStockView(mainMenuView);
        dialog.setVisible(true);
    }
    
    private void openSortieStock() {
        SortieStockView dialog = new SortieStockView(mainMenuView);
        dialog.setVisible(true);
    }
    
    private void openHistoriqueStock() {
        HistoriqueStockView view = new HistoriqueStockView();
        view.setVisible(true);
    }
    
    private void openAlertesStock() {
        AlertesStockView view = new AlertesStockView();
        view.setVisible(true);
    }
    
    private void openNouvelleCommande() {
        NouvelleCommandeView view = new NouvelleCommandeView();
        view.setVisible(true);
    }
    
    private void openListeCommandes() {
        ListeCommandesView view = new ListeCommandesView();
        view.setVisible(true);
    }
    
    private void openCAByDate() {
        // Ouvrir la vue des statistiques
        StatistiquesView statistiquesView = new StatistiquesView();
        statistiquesView.setVisible(true);
    }
    
    private void openTopProduits() {
        // Ouvrir la vue des top produits
        TopProduitsView topProduitsView = new TopProduitsView();
        topProduitsView.setVisible(true);
    }
    
    private void openAide() {
        JOptionPane.showMessageDialog(mainMenuView,
            "<html><b>Aide du système</b><br><br>" +
            "1. <b>Gestion</b>: Gérez les produits et catégories<br>" +
            "2. <b>Stock</b>: Entrées, sorties et historiques<br>" +
            "3. <b>Commandes</b>: Prise de commandes clients<br>" +
            "4. <b>Statistiques</b>: Rapports et analyses<br><br>" +
            "<b>Raccourcis utiles :</b><br>" +
            "- F5 : Actualiser les listes<br>" +
            "- Double-clic : Modifier/voir détails<br>" +
            "- Ctrl+N : Nouvelle commande</html>",
            "Aide",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAPropos() {
        JOptionPane.showMessageDialog(mainMenuView,
            "<html><center><b>Gestion de Restaurant</b><br>" +
            "Version 2.0<br>" +
            "Développé par votre équipe<br>" +
            "IAI-TOGO 2025-2026<br><br>" +
            "Technologies :<br>" +
            "- Java SE 11+<br>" +
            "- Swing UI<br>" +
            "- SQL Server<br>" +
            "- Architecture MVC<br><br>" +
            "© Tous droits réservés</center></html>",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void quitterApplication() {
        int response = JOptionPane.showConfirmDialog(
            mainMenuView,
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