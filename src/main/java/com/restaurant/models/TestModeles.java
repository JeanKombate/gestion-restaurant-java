/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class TestModeles {
        public static void main(String[] args) {
        System.out.println("=== Test des classes modèles ===");
        
        // Test Catégorie
        Categorie cat = new Categorie(1, "Boissons");
        System.out.println("Catégorie : " + cat.getLibelle());
        
        // Test Produit
        Produit p = new Produit("Coca-Cola", cat, 1500.0, 50, 10);
        System.out.println("Produit : " + p.getNom() + ", Prix: " + p.getPrixVente());
        System.out.println("Stock bas ? " + p.estStockBas());
        
        // Test LigneCommande
        LigneCommande lc = new LigneCommande(p, 2);
        System.out.println("Ligne commande : " + lc.getQuantite() + " x " + 
                         lc.getProduit().getNom() + " = " + lc.getMontantLigne());
        
        System.out.println("✅ Test des modèles terminé avec succès !");
    }
}
