/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.Categorie;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class CategorieDAO implements IDao<Categorie> {
    
    private final Connection connection;
    
    public CategorieDAO() {
        this.connection = DBConnection.getConnection();
    }
    
    @Override
    public void create(Categorie categorie) {
        String sql = "INSERT INTO Categorie (libelle) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categorie.getLibelle());
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                categorie.setId(rs.getInt(1));
            }
            
            System.out.println("Catégorie créée : " + categorie.getLibelle());
            
        } catch (SQLException e) {
            System.err.println("Erreur création catégorie: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Categorie read(int id) {
        String sql = "SELECT * FROM Categorie WHERE id = ?";
        Categorie categorie = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                categorie = new Categorie();
                categorie.setId(rs.getInt("id"));
                categorie.setLibelle(rs.getString("libelle"));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lecture catégorie: " + e.getMessage());
        }
        
        return categorie;
    }
    
    @Override
    public void update(Categorie categorie) {
        String sql = "UPDATE Categorie SET libelle = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categorie.getLibelle());
            stmt.setInt(2, categorie.getId());
            stmt.executeUpdate();
            
            System.out.println("Catégorie mise à jour : " + categorie.getLibelle());
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour catégorie: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Categorie WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Catégorie supprimée, ID: " + id);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression catégorie: " + e.getMessage());
            // Vérifier si c'est une violation de clé étrangère
            if (e.getErrorCode() == 547) { // Code d'erreur SQL Server pour FK violation
                throw new RuntimeException("Impossible de supprimer : des produits sont associés à cette catégorie");
            }
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categorie ORDER BY libelle";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(rs.getInt("id"));
                categorie.setLibelle(rs.getString("libelle"));
                categories.add(categorie);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération catégories: " + e.getMessage());
        }
        
        return categories;
    }
    
    // Méthode supplémentaire : vérifier si un libellé existe déjà
    public boolean libelleExists(String libelle) {
        String sql = "SELECT COUNT(*) FROM Categorie WHERE libelle = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur vérification libellé: " + e.getMessage());
        }
        
        return false;
    }
}
