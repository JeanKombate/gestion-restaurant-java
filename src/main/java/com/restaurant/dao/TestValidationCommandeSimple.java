package com.restaurant.dao;

import java.util.Date;

public class TestValidationCommandeSimple {
    public static void main(String[] args) {
        System.out.println("=== Test simple validation commande ===");
        
        try {
            // Créer une commande simple
            CommandeDAO commandeDAO = new CommandeDAO();
            
            // Créer une commande
            com.restaurant.models.Commande commande = new com.restaurant.models.Commande();
            commande.setDateCommande(new Date());
            commande.setEtat("EN_COURS");
            commande.setTotal(0.0);
            
            int commandeId = commandeDAO.create(commande);
            System.out.println("1. Commande créée: #" + commandeId);
            
            // Ajouter une ligne (il faut un produit existant)
            LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
            ProduitDAO produitDAO = new ProduitDAO();
            
            var produits = produitDAO.getAll();
            if (produits.isEmpty()) {
                System.out.println("❌ Aucun produit disponible");
                return;
            }
            
            var produit = produits.get(0);
            System.out.println("2. Produit sélectionné: " + produit.getNom() + 
                             ", Stock: " + produit.getStockActuel());
            
            // Créer une ligne de commande
            com.restaurant.models.LigneCommande ligne = 
                new com.restaurant.models.LigneCommande(produit, 1);
            ligneDAO.create(ligne, commandeId);
            System.out.println("3. Ligne ajoutée");
            
            // Valider la commande
            System.out.println("4. Validation en cours...");
            commandeDAO.validerCommande(commandeId);
            
            // Vérifier
            var commandeApres = commandeDAO.read(commandeId);
            var produitApres = produitDAO.read(produit.getId());
            
            System.out.println("5. État commande: " + commandeApres.getEtat());
            System.out.println("6. Nouveau stock: " + produitApres.getStockActuel());
            
            System.out.println("\n✅ Test réussi !");
            
        } catch (Exception e) {
            System.err.println("\n❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}