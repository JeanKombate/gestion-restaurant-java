package com.restaurant.dao;

import com.restaurant.models.Categorie;
import com.restaurant.models.Produit;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO implements IDao<Produit> {
    
    // PAS de variable connection d'instance !
    
    public ProduitDAO() {
        // Constructeur vide
    }
    
    @Override
    public void create(Produit produit) {
        String sql = "INSERT INTO Produit (nom, categorie_id, prixVente, stockActuel, seuilAlerte) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produit.getNom());
            stmt.setInt(2, produit.getCategorie().getId());
            stmt.setDouble(3, produit.getPrixVente());
            stmt.setInt(4, produit.getStockActuel());
            stmt.setInt(5, produit.getSeuilAlerte());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                produit.setId(rs.getInt(1));
            }
            
            System.out.println("Produit créé : " + produit.getNom());
            
        } catch (SQLException e) {
            System.err.println("Erreur création produit: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Produit read(int id) {
        String sql = "SELECT p.*, c.libelle as categorie_libelle FROM Produit p " +
                    "JOIN Categorie c ON p.categorie_id = c.id WHERE p.id = ?";
        Produit produit = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                produit = mapResultSetToProduit(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lecture produit: " + e.getMessage());
            e.printStackTrace();
        }
        
        return produit;
    }
    
    @Override
    public void update(Produit produit) {
        String sql = "UPDATE Produit SET nom = ?, categorie_id = ?, prixVente = ?, " +
                    "stockActuel = ?, seuilAlerte = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produit.getNom());
            stmt.setInt(2, produit.getCategorie().getId());
            stmt.setDouble(3, produit.getPrixVente());
            stmt.setInt(4, produit.getStockActuel());
            stmt.setInt(5, produit.getSeuilAlerte());
            stmt.setInt(6, produit.getId());
            stmt.executeUpdate();
            
            System.out.println("Produit mis à jour : " + produit.getNom());
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour produit: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Produit WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Produit supprimé, ID: " + id);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression produit: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle as categorie_libelle FROM Produit p " +
                    "JOIN Categorie c ON p.categorie_id = c.id ORDER BY p.nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération produits: " + e.getMessage());
        }
        
        return produits;
    }
    
    // Méthode pour transformer un ResultSet en objet Produit
    private Produit mapResultSetToProduit(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getInt("id"));
        produit.setNom(rs.getString("nom"));
        
        Categorie categorie = new Categorie();
        categorie.setId(rs.getInt("categorie_id"));
        categorie.setLibelle(rs.getString("categorie_libelle"));
        produit.setCategorie(categorie);
        
        produit.setPrixVente(rs.getDouble("prixVente"));
        produit.setStockActuel(rs.getInt("stockActuel"));
        produit.setSeuilAlerte(rs.getInt("seuilAlerte"));
        
        return produit;
    }
    
    // Méthodes supplémentaires pour la gestion du stock
    public void updateStock(int produitId, int quantite) {
        String sql = "UPDATE Produit SET stockActuel = stockActuel + ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantite);
            stmt.setInt(2, produitId);
            stmt.executeUpdate();
            
            System.out.println("Stock mis à jour pour produit ID " + produitId + ": " + quantite);
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour stock: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public List<Produit> getProduitsStockBas() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle as categorie_libelle FROM Produit p " +
                    "JOIN Categorie c ON p.categorie_id = c.id " +
                    "WHERE p.stockActuel < p.seuilAlerte ORDER BY p.nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération produits stock bas: " + e.getMessage());
        }
        
        return produits;
    }
    
    // Recherche de produits par nom
    public List<Produit> searchByName(String searchTerm) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle as categorie_libelle FROM Produit p " +
                    "JOIN Categorie c ON p.categorie_id = c.id " +
                    "WHERE p.nom LIKE ? ORDER BY p.nom";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur recherche produits: " + e.getMessage());
        }
        
        return produits;
    }
}