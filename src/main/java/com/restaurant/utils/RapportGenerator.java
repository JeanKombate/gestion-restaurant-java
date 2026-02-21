/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.utils;

import com.restaurant.dao.*;
import com.restaurant.models.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Générateur de rapports PDF
 * Note: Cette version génère des fichiers texte formatés
 * Pour de vrais PDF, ajoutez iText ou Apache PDFBox à votre pom.xml
 */
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class RapportGenerator {
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /**
     * Générer un rapport de ventes
     */
    public String genererRapportVentes(Date dateDebut, Date dateFin) {
        StringBuilder rapport = new StringBuilder();
        CommandeDAO commandeDAO = new CommandeDAO();
        
        rapport.append("═══════════════════════════════════════════════════\n");
        rapport.append("         RAPPORT DE VENTES\n");
        rapport.append("═══════════════════════════════════════════════════\n\n");
        rapport.append("Période: ").append(dateFormat.format(dateDebut))
               .append(" - ").append(dateFormat.format(dateFin)).append("\n");
        rapport.append("Généré le: ").append(dateFormat.format(new Date())).append("\n");
        rapport.append("Par: ").append(SessionManager.getInstance().getLogin()).append("\n\n");
        
        // Statistiques globales
        List<Commande> commandes = commandeDAO.getAll();
        int nbCommandesValidees = 0;
        double totalCA = 0;
        
        for (Commande cmd : commandes) {
            if ("VALIDÉE".equals(cmd.getEtat())) {
                Date dateCmde = cmd.getDateCommande();
                if (dateCmde.after(dateDebut) && dateCmde.before(dateFin)) {
                    nbCommandesValidees++;
                    totalCA += cmd.getTotal();
                }
            }
        }
        
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append("STATISTIQUES GLOBALES\n");
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append(String.format("Nombre de commandes: %d\n", nbCommandesValidees));
        rapport.append(String.format("Chiffre d'affaires total: %.2f FCFA\n", totalCA));
        rapport.append(String.format("Panier moyen: %.2f FCFA\n\n", 
                      nbCommandesValidees > 0 ? totalCA / nbCommandesValidees : 0));
        
        // Top produits vendus
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append("TOP 10 PRODUITS VENDUS\n");
        rapport.append("───────────────────────────────────────────────────\n");
        
        List<Object[]> topProduits = commandeDAO.getTopProduits(10);
        int rank = 1;
        for (Object[] produit : topProduits) {
            rapport.append(String.format("%d. %-30s : %d unités\n", 
                          rank++, produit[0], produit[1]));
        }
        
        rapport.append("\n═══════════════════════════════════════════════════\n");
        rapport.append("              FIN DU RAPPORT\n");
        rapport.append("═══════════════════════════════════════════════════\n");
        
        return rapport.toString();
    }
    
    /**
     * Générer un rapport de stock
     */
    public String genererRapportStock() {
        StringBuilder rapport = new StringBuilder();
        ProduitDAO produitDAO = new ProduitDAO();
        
        rapport.append("═══════════════════════════════════════════════════\n");
        rapport.append("         RAPPORT D'ÉTAT DES STOCKS\n");
        rapport.append("═══════════════════════════════════════════════════\n\n");
        rapport.append("Généré le: ").append(dateFormat.format(new Date())).append("\n");
        rapport.append("Par: ").append(SessionManager.getInstance().getLogin()).append("\n\n");
        
        // Produits en stock bas
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append("PRODUITS EN STOCK BAS (⚠️ ALERTE)\n");
        rapport.append("───────────────────────────────────────────────────\n");
        
        List<Produit> stockBas = produitDAO.getProduitsStockBas();
        if (stockBas.isEmpty()) {
            rapport.append("Aucun produit en stock bas ✓\n\n");
        } else {
            for (Produit p : stockBas) {
                rapport.append(String.format("⚠️ %-30s : Stock=%d, Seuil=%d\n", 
                              p.getNom(), p.getStockActuel(), p.getSeuilAlerte()));
            }
            rapport.append("\n");
        }
        
        // Tous les produits
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append("ÉTAT COMPLET DES STOCKS\n");
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append(String.format("%-5s %-30s %-15s %-10s %-10s\n", 
                      "ID", "Produit", "Catégorie", "Stock", "Seuil"));
        rapport.append("───────────────────────────────────────────────────\n");
        
        List<Produit> produits = produitDAO.getAll();
        for (Produit p : produits) {
            rapport.append(String.format("%-5d %-30s %-15s %-10d %-10d\n",
                          p.getId(),
                          p.getNom(),
                          p.getCategorie().getLibelle(),
                          p.getStockActuel(),
                          p.getSeuilAlerte()));
        }
        
        rapport.append("\n═══════════════════════════════════════════════════\n");
        rapport.append("              FIN DU RAPPORT\n");
        rapport.append("═══════════════════════════════════════════════════\n");
        
        return rapport.toString();
    }
    
    /**
     * Générer un rapport de mouvements de stock
     */
    public String genererRapportMouvements(Date dateDebut, Date dateFin) {
        StringBuilder rapport = new StringBuilder();
        MouvementStockDAO mouvementDAO = new MouvementStockDAO();
        
        rapport.append("═══════════════════════════════════════════════════\n");
        rapport.append("      RAPPORT DES MOUVEMENTS DE STOCK\n");
        rapport.append("═══════════════════════════════════════════════════\n\n");
        rapport.append("Période: ").append(dateFormat.format(dateDebut))
               .append(" - ").append(dateFormat.format(dateFin)).append("\n");
        rapport.append("Généré le: ").append(dateFormat.format(new Date())).append("\n");
        rapport.append("Par: ").append(SessionManager.getInstance().getLogin()).append("\n\n");
        
        List<MouvementStock> mouvements = mouvementDAO.getHistorique();
        
        int nbEntrees = 0;
        int nbSorties = 0;
        int totalQuantiteEntree = 0;
        int totalQuantiteSortie = 0;
        
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append("DÉTAIL DES MOUVEMENTS\n");
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append(String.format("%-12s %-10s %-25s %-10s %-20s\n", 
                      "Date", "Type", "Produit", "Quantité", "Motif"));
        rapport.append("───────────────────────────────────────────────────\n");
        
        for (MouvementStock m : mouvements) {
            Date dateMvt = m.getDateMouvement();
            if (dateMvt.after(dateDebut) && dateMvt.before(dateFin)) {
                rapport.append(String.format("%-12s %-10s %-25s %-10d %-20s\n",
                              dateFormat.format(dateMvt),
                              m.getType(),
                              m.getProduit().getNom(),
                              m.getQuantite(),
                              m.getMotif() != null ? m.getMotif() : ""));
                
                if ("ENTRÉE".equals(m.getType())) {
                    nbEntrees++;
                    totalQuantiteEntree += m.getQuantite();
                } else {
                    nbSorties++;
                    totalQuantiteSortie += m.getQuantite();
                }
            }
        }
        
        rapport.append("\n───────────────────────────────────────────────────\n");
        rapport.append("RÉSUMÉ\n");
        rapport.append("───────────────────────────────────────────────────\n");
        rapport.append(String.format("Nombre d'entrées: %d (Total: %d unités)\n", 
                      nbEntrees, totalQuantiteEntree));
        rapport.append(String.format("Nombre de sorties: %d (Total: %d unités)\n", 
                      nbSorties, totalQuantiteSortie));
        rapport.append(String.format("Solde: %d unités\n", 
                      totalQuantiteEntree - totalQuantiteSortie));
        
        rapport.append("\n═══════════════════════════════════════════════════\n");
        rapport.append("              FIN DU RAPPORT\n");
        rapport.append("═══════════════════════════════════════════════════\n");
        
        return rapport.toString();
    }
    
    /**
     * Sauvegarder un rapport dans un fichier
     */
    public void sauvegarderRapport(String contenu, String nomFichier) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(nomFichier)) {
            fos.write(contenu.getBytes("UTF-8"));
            System.out.println("✅ Rapport sauvegardé: " + nomFichier);
        }
    }
    
    /**
     * Générer tous les rapports d'un coup
     */
    public void genererTousLesRapports(String dossierDestination) {
        try {
            Date maintenant = new Date();
            Date debut = new Date(maintenant.getTime() - (30L * 24 * 60 * 60 * 1000)); // 30 jours avant
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(maintenant);
            
            // Rapport de ventes
            String rapportVentes = genererRapportVentes(debut, maintenant);
            sauvegarderRapport(rapportVentes, 
                dossierDestination + "/rapport_ventes_" + timestamp + ".txt");
            
            // Rapport de stock
            String rapportStock = genererRapportStock();
            sauvegarderRapport(rapportStock, 
                dossierDestination + "/rapport_stock_" + timestamp + ".txt");
            
            // Rapport de mouvements
            String rapportMouvements = genererRapportMouvements(debut, maintenant);
            sauvegarderRapport(rapportMouvements, 
                dossierDestination + "/rapport_mouvements_" + timestamp + ".txt");
            
            System.out.println("✅ Tous les rapports ont été générés avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur génération rapports: " + e.getMessage());
            e.printStackTrace();
        }
    }
}