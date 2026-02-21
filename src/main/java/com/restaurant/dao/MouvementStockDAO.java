package com.restaurant.dao;

import com.restaurant.models.MouvementStock;
import com.restaurant.models.Produit;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO {
    
    public MouvementStockDAO() {
    }
    
    // Créer un mouvement de stock (avec connexion fournie)
    private void create(MouvementStock mouvement, Connection conn) throws SQLException {
        String sql = "INSERT INTO MouvementStock (type, produit_id, quantite, dateMouvement, motif) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mouvement.getType());
            stmt.setInt(2, mouvement.getProduit().getId());
            stmt.setInt(3, mouvement.getQuantite());
            stmt.setTimestamp(4, new Timestamp(mouvement.getDateMouvement().getTime()));
            stmt.setString(5, mouvement.getMotif());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                mouvement.setId(rs.getInt(1));
            }
            
            System.out.println("Mouvement de stock créé : " + mouvement.getType() + " - " + mouvement.getQuantite());
        }
    }
    
    // Créer un mouvement de stock (sans transaction)
    public void create(MouvementStock mouvement) {
        try (Connection conn = DBConnection.getConnection()) {
            create(mouvement, conn);
        } catch (SQLException e) {
            System.err.println("Erreur création mouvement stock: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Enregistrer une entrée de stock (AVEC connexion pour transactions)
    public void enregistrerEntree(Produit produit, int quantite, String motif, Connection conn) throws SQLException {
        MouvementStock mouvement = new MouvementStock("ENTRÉE", produit, quantite, motif);
        create(mouvement, conn);
        
        String sql = "UPDATE Produit SET stockActuel = stockActuel + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantite);
            stmt.setInt(2, produit.getId());
            stmt.executeUpdate();
        }
    }
    
    // Enregistrer une entrée de stock (SANS connexion - crée sa propre transaction)
    public void enregistrerEntree(Produit produit, int quantite, String motif) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            enregistrerEntree(produit, quantite, motif, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erreur lors de l'entrée de stock: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    // Enregistrer une sortie de stock (AVEC connexion pour transactions)
    public void enregistrerSortie(Produit produit, int quantite, String motif, Connection conn) throws SQLException {
        if (produit.getStockActuel() < quantite) {
            throw new RuntimeException("Stock insuffisant. Stock actuel: " + produit.getStockActuel());
        }
        
        MouvementStock mouvement = new MouvementStock("SORTIE", produit, quantite, motif);
        create(mouvement, conn);
        
        String sql = "UPDATE Produit SET stockActuel = stockActuel - ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantite);
            stmt.setInt(2, produit.getId());
            stmt.executeUpdate();
        }
    }
    
    // Enregistrer une sortie de stock (SANS connexion - crée sa propre transaction)
    public void enregistrerSortie(Produit produit, int quantite, String motif) {
        Connection conn = null;
        try {
            if (produit.getStockActuel() < quantite) {
                throw new RuntimeException("Stock insuffisant");
            }
            
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            enregistrerSortie(produit, quantite, motif, conn);
            
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erreur lors de la sortie de stock: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    public List<MouvementStock> getHistorique() {
        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT m.*, p.nom as produit_nom, p.stockActuel FROM MouvementStock m JOIN Produit p ON m.produit_id = p.id ORDER BY m.dateMouvement DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                mouvements.add(mapResultSetToMouvement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mouvements;
    }
    
    private MouvementStock mapResultSetToMouvement(ResultSet rs) throws SQLException {
        MouvementStock mouvement = new MouvementStock();
        mouvement.setId(rs.getInt("id"));
        mouvement.setType(rs.getString("type"));
        mouvement.setQuantite(rs.getInt("quantite"));
        mouvement.setDateMouvement(rs.getTimestamp("dateMouvement"));
        mouvement.setMotif(rs.getString("motif"));
        
        Produit produit = new Produit();
        produit.setId(rs.getInt("produit_id"));
        produit.setNom(rs.getString("produit_nom"));
        produit.setStockActuel(rs.getInt("stockActuel"));
        mouvement.setProduit(produit);
        
        return mouvement;
    }
}