package com.restaurant.dao;

import com.restaurant.models.Commande;
import com.restaurant.models.LigneCommande;
import com.restaurant.models.Produit;
import java.util.Date;

public class TestValidationCommande {
    public static void main(String[] args) {
        System.out.println("=== Test de validation de commande ===");
        
        try {
            // 1. Récupérer un produit avec du stock
            ProduitDAO produitDAO = new ProduitDAO();
            var produits = produitDAO.getAll();
            
            if (produits.isEmpty()) {
                System.out.println("❌ Aucun produit trouvé");
                return;
            }
            
            Produit produit = produits.get(0);
            System.out.println("Produit: " + produit.getNom() + 
                             ", Stock: " + produit.getStockActuel());
            
            // 2. Créer une commande
            CommandeDAO commandeDAO = new CommandeDAO();
            Commande commande = new Commande();
            commande.setDateCommande(new Date());
            commande.setEtat("EN_COURS");
            commande.setTotal(produit.getPrixVente() * 2);
            
            int commandeId = commandeDAO.create(commande);
            System.out.println("✅ Commande créée #" + commandeId);
            
            // 3. Ajouter une ligne
            LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
            LigneCommande ligne = new LigneCommande(produit, 2);
            ligneDAO.create(ligne, commandeId);
            System.out.println("✅ Ligne ajoutée");
            
            // 4. Afficher l'état avant validation
            Commande cmdAvant = commandeDAO.read(commandeId);
            System.out.println("État avant: " + cmdAvant.getEtat());
            
            // 5. Valider la commande
            System.out.println("Validation en cours...");
            commandeDAO.validerCommande(commandeId);
            
            // 6. Vérifier l'état après
            Commande cmdApres = commandeDAO.read(commandeId);
            System.out.println("État après: " + cmdApres.getEtat());
            
            // 7. Vérifier le nouveau stock
            Produit produitApres = produitDAO.read(produit.getId());
            System.out.println("Stock après: " + produitApres.getStockActuel());
            
            System.out.println("✅ Test terminé avec succès !");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
