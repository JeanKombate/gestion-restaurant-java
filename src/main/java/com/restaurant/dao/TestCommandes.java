/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.Commande;
import com.restaurant.models.LigneCommande;
import com.restaurant.models.Produit;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class TestCommandes {
    public static void main(String[] args) {
        System.out.println("=== Test des commandes ===");
        
        try {
            // Récupérer un produit EXISTANT de la base
            ProduitDAO produitDAO = new ProduitDAO();
            List<Produit> produits = produitDAO.getAll();
            
            if (produits.isEmpty()) {
                System.out.println("❌ Aucun produit trouvé dans la base !");
                System.out.println("Veuillez créer des produits d'abord via l'application.");
                return;
            }
            
            Produit produit = produits.get(0); // Prendre le premier produit
            System.out.println("Produit utilisé pour le test: " + produit.getNom() + 
                             ", Prix: " + produit.getPrixVente() + 
                             ", Stock: " + produit.getStockActuel());
            
            // Test CommandeDAO
            CommandeDAO commandeDAO = new CommandeDAO();
            
            // Créer une commande
            Commande commande = new Commande();
            commande.setDateCommande(new Date());
            commande.setEtat("EN_COURS");
            commande.setTotal(0.0);
            
            int commandeId = commandeDAO.create(commande);
            System.out.println("✅ Commande créée avec ID: " + commandeId);
            
            // Lire la commande
            Commande commandeLue = commandeDAO.read(commandeId);
            if (commandeLue != null) {
                System.out.println("✅ Commande lue - État: " + commandeLue.getEtat());
            } else {
                System.out.println("❌ Impossible de lire la commande");
                return;
            }
            
            // Test LigneCommandeDAO
            LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
            
            // Créer une ligne de commande AVEC UN PRODUIT RÉEL
            LigneCommande ligne = new LigneCommande(produit, 2); // 2 unités
            System.out.println("✅ Ligne créée - Prix unitaire: " + ligne.getPrixUnitaire() + 
                             ", Montant: " + ligne.getMontantLigne());
            
            ligneDAO.create(ligne, commandeId);
            System.out.println("✅ Ligne commande créée avec succès");
            
            // Obtenir les lignes de la commande
            var lignes = ligneDAO.getByCommande(commandeId);
            System.out.println("✅ Nombre de lignes: " + lignes.size());
            
            // Afficher les lignes
            for (LigneCommande l : lignes) {
                System.out.println("  - " + l.getQuantite() + " x " + 
                                 l.getProduit().getNom() + " = " + l.getMontantLigne());
            }
            
            // Calculer le total
            double total = ligneDAO.getTotalCommande(commandeId);
            System.out.println("✅ Total commande: " + total);
            
            // Mettre à jour le total de la commande
            commandeLue.setTotal(total);
            commandeDAO.update(commandeLue);
            
            System.out.println("\n✅ Test des commandes terminé avec succès !");
            
            // Optionnel : Valider la commande pour tester la gestion des stocks
            System.out.println("\n⚠️  Pour tester la validation, décommentez la ligne suivante dans le code.");
            System.out.println("⚠️  Attention : Cela diminuera le stock de 2 unités pour " + produit.getNom());
            // commandeDAO.validerCommande(commandeId);
            
            // Optionnel : Nettoyage
            // commandeDAO.delete(commandeId);
            // System.out.println("Commande de test supprimée");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}