/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.Categorie;
import com.restaurant.models.Produit;
import com.restaurant.models.Utilisateur;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class TestDAO {
    public static void main(String[] args) {
        System.out.println("=== Test des DAO ===");
        
        // Test CategorieDAO
        CategorieDAO categorieDAO = new CategorieDAO();
        
        // Vérifier si la catégorie existe déjà
        Categorie nouvelleCat = null;
        if (categorieDAO.libelleExists("Test Catégorie")) {
            System.out.println("⚠️ La catégorie existe déjà, récupération...");
            // Récupérer la catégorie existante
            for (Categorie cat : categorieDAO.getAll()) {
                if (cat.getLibelle().equals("Test Catégorie")) {
                    nouvelleCat = cat;
                    System.out.println("Catégorie récupérée avec ID: " + nouvelleCat.getId());
                    break;
                }
            }
        } else {
            // Créer une nouvelle catégorie
            nouvelleCat = new Categorie("Test Catégorie");
            categorieDAO.create(nouvelleCat);
            System.out.println("Catégorie créée avec ID: " + nouvelleCat.getId());
        }
        
        // Lire la catégorie
        Categorie catLue = categorieDAO.read(nouvelleCat.getId());
        System.out.println("Catégorie lue: " + catLue.getLibelle());
        
        // Tester si libellé existe
        boolean existe = categorieDAO.libelleExists("Test Catégorie");
        System.out.println("Le libellé existe? " + existe);
        
        // Lister toutes les catégories
        System.out.println("\nToutes les catégories:");
        for (Categorie cat : categorieDAO.getAll()) {
            System.out.println("- " + cat.getLibelle());
        }
        
        // Test ProduitDAO
        ProduitDAO produitDAO = new ProduitDAO();
        
        // Créer un produit
        Produit nouveauProduit = new Produit("Produit Test", nouvelleCat, 1000.0, 50, 5);
        produitDAO.create(nouveauProduit);
        System.out.println("\nProduit créé avec ID: " + nouveauProduit.getId());
        
        // Lire le produit
        Produit produitLu = produitDAO.read(nouveauProduit.getId());
        System.out.println("Produit lu: " + produitLu.getNom() + ", Catégorie: " + produitLu.getCategorie().getLibelle());
        
        // Mettre à jour le stock
        produitDAO.updateStock(nouveauProduit.getId(), -10); // Diminuer le stock de 10
        produitLu = produitDAO.read(nouveauProduit.getId());
        System.out.println("Nouveau stock: " + produitLu.getStockActuel());
        
        // Vérifier produits en stock bas
        System.out.println("\nProduits en stock bas:");
        for (Produit p : produitDAO.getProduitsStockBas()) {
            System.out.println("- " + p.getNom() + " (Stock: " + p.getStockActuel() + ")");
        }
        
        // Test UtilisateurDAO
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        // Vérifier si admin existe déjà
        boolean adminExists = false;
        for (Utilisateur u : utilisateurDAO.getAll()) {
            if (u.getLogin().equals("admin")) {
                adminExists = true;
                System.out.println("ℹ️ Utilisateur 'admin' existe déjà (ID: " + u.getId() + ")");
                break;
            }
        }

        // Créer admin seulement s'il n'existe pas
        if (!adminExists) {
            Utilisateur nouvelAdmin = new Utilisateur("admin", "admin123", "ADMIN");
            utilisateurDAO.create(nouvelAdmin);
            System.out.println("✅ Nouvel utilisateur admin créé avec ID: " + nouvelAdmin.getId());
        }

        // Tester l'authentification
        Utilisateur admin = utilisateurDAO.authentifier("admin", "admin123");
        if (admin != null) {
            System.out.println("✅ Authentification réussie pour: " + admin.getLogin() + " (Rôle: " + admin.getRole() + ")");
        } else {
            System.out.println("❌ Échec d'authentification");
        }
        
        System.out.println("\n✅ Test des DAO terminé avec succès !");
    }
}
