/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.LigneCommande;
import com.restaurant.models.Produit;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class LigneCommandeDAO {
    
    // PAS de variable connection d'instance !
    
    public LigneCommandeDAO() {
        // Constructeur vide
    }
    
    // Créer une ligne de commande
    public void create(LigneCommande ligne, int commandeId) {
        String sql = "INSERT INTO LigneCommande (commande_id, produit_id, quantite, prixUnitaire, montantLigne) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, commandeId);
            stmt.setInt(2, ligne.getProduit().getId());
            stmt.setInt(3, ligne.getQuantite());
            stmt.setDouble(4, ligne.getPrixUnitaire());
            stmt.setDouble(5, ligne.getMontantLigne());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ligne.setId(rs.getInt(1));
            }
            
            System.out.println("Ligne commande créée pour commande #" + commandeId);
            
        } catch (SQLException e) {
            System.err.println("Erreur création ligne commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Lire une ligne par ID
    public LigneCommande read(int id) {
        String sql = "SELECT lc.*, p.nom as produit_nom, p.prixVente, p.stockActuel " +
                    "FROM LigneCommande lc " +
                    "JOIN Produit p ON lc.produit_id = p.id " +
                    "WHERE lc.id = ?";
        LigneCommande ligne = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ligne = mapResultSetToLigneCommande(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lecture ligne commande: " + e.getMessage());
        }
        
        return ligne;
    }
    
    // Mettre à jour une ligne
    public void update(LigneCommande ligne) {
        String sql = "UPDATE LigneCommande SET quantite = ?, prixUnitaire = ?, montantLigne = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ligne.getQuantite());
            stmt.setDouble(2, ligne.getPrixUnitaire());
            stmt.setDouble(3, ligne.getMontantLigne());
            stmt.setInt(4, ligne.getId());
            stmt.executeUpdate();
            
            System.out.println("Ligne commande mise à jour: " + ligne.getId());
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour ligne commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Supprimer une ligne
    public void delete(int id) {
        String sql = "DELETE FROM LigneCommande WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            System.out.println("Ligne commande supprimée: " + id);
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression ligne commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Obtenir toutes les lignes d'une commande
    public List<LigneCommande> getByCommande(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom as produit_nom, p.prixVente, p.stockActuel " +
                    "FROM LigneCommande lc " +
                    "JOIN Produit p ON lc.produit_id = p.id " +
                    "WHERE lc.commande_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lignes.add(mapResultSetToLigneCommande(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération lignes commande: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lignes;
    }
    
    // Obtenir le total d'une commande
    public double getTotalCommande(int commandeId) {
        String sql = "SELECT SUM(montantLigne) as total FROM LigneCommande WHERE commande_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur calcul total commande: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // Méthode utilitaire pour mapper ResultSet à LigneCommande
    private LigneCommande mapResultSetToLigneCommande(ResultSet rs) throws SQLException {
        LigneCommande ligne = new LigneCommande();
        ligne.setId(rs.getInt("id"));
        ligne.setQuantite(rs.getInt("quantite"));
        ligne.setPrixUnitaire(rs.getDouble("prixUnitaire"));
        ligne.setMontantLigne(rs.getDouble("montantLigne"));
        
        Produit produit = new Produit();
        produit.setId(rs.getInt("produit_id"));
        produit.setNom(rs.getString("produit_nom"));
        produit.setPrixVente(rs.getDouble("prixVente"));
        produit.setStockActuel(rs.getInt("stockActuel"));
        ligne.setProduit(produit);
        
        return ligne;
    }
}