/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.Fournisseur;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class FournisseurDAO {
    
    public FournisseurDAO() {
    }
    
    // Créer un fournisseur
    public void create(Fournisseur fournisseur) {
        String sql = "INSERT INTO Fournisseur (nom, contact, telephone, email, adresse, specialite, actif) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getAdresse());
            stmt.setString(6, fournisseur.getSpecialite());
            stmt.setBoolean(7, fournisseur.isActif());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                fournisseur.setId(rs.getInt(1));
            }
            
            System.out.println("Fournisseur créé : " + fournisseur.getNom());
            
        } catch (SQLException e) {
            System.err.println("Erreur création fournisseur: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Lire un fournisseur par ID
    public Fournisseur read(int id) {
        String sql = "SELECT * FROM Fournisseur WHERE id = ?";
        Fournisseur fournisseur = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                fournisseur = mapResultSetToFournisseur(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lecture fournisseur: " + e.getMessage());
        }
        
        return fournisseur;
    }
    
    // Mettre à jour un fournisseur
    public void update(Fournisseur fournisseur) {
        String sql = "UPDATE Fournisseur SET nom = ?, contact = ?, telephone = ?, " +
                    "email = ?, adresse = ?, specialite = ?, actif = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getAdresse());
            stmt.setString(6, fournisseur.getSpecialite());
            stmt.setBoolean(7, fournisseur.isActif());
            stmt.setInt(8, fournisseur.getId());
            
            stmt.executeUpdate();
            System.out.println("Fournisseur mis à jour : " + fournisseur.getNom());
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour fournisseur: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Supprimer un fournisseur
    public void delete(int id) {
        String sql = "DELETE FROM Fournisseur WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Fournisseur supprimé, ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression fournisseur: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Désactiver un fournisseur (au lieu de le supprimer)
    public void desactiver(int id) {
        String sql = "UPDATE Fournisseur SET actif = 0 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Fournisseur désactivé, ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Erreur désactivation fournisseur: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Réactiver un fournisseur
    public void reactiver(int id) {
        String sql = "UPDATE Fournisseur SET actif = 1 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Fournisseur réactivé, ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Erreur réactivation fournisseur: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Obtenir tous les fournisseurs
    public List<Fournisseur> getAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération fournisseurs: " + e.getMessage());
        }
        
        return fournisseurs;
    }
    
    // Obtenir uniquement les fournisseurs actifs
    public List<Fournisseur> getActifs() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur WHERE actif = 1 ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération fournisseurs actifs: " + e.getMessage());
        }
        
        return fournisseurs;
    }
    
    // Rechercher par nom
    public List<Fournisseur> searchByName(String searchTerm) {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur WHERE nom LIKE ? ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur recherche fournisseurs: " + e.getMessage());
        }
        
        return fournisseurs;
    }
    
    // Obtenir par spécialité
    public List<Fournisseur> getBySpecialite(String specialite) {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur WHERE specialite = ? AND actif = 1 ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, specialite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération par spécialité: " + e.getMessage());
        }
        
        return fournisseurs;
    }
    
    // Vérifier si un nom existe déjà
    public boolean nomExists(String nom) {
        String sql = "SELECT COUNT(*) FROM Fournisseur WHERE nom = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur vérification nom: " + e.getMessage());
        }
        
        return false;
    }
    
    // Mapper ResultSet vers Fournisseur
    private Fournisseur mapResultSetToFournisseur(ResultSet rs) throws SQLException {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(rs.getInt("id"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setContact(rs.getString("contact"));
        fournisseur.setTelephone(rs.getString("telephone"));
        fournisseur.setEmail(rs.getString("email"));
        fournisseur.setAdresse(rs.getString("adresse"));
        fournisseur.setSpecialite(rs.getString("specialite"));
        fournisseur.setActif(rs.getBoolean("actif"));
        return fournisseur;
    }
}