/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.utils.DBConnection;
import com.restaurant.utils.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les logs des actions utilisateurs
 */

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class LogDAO {
    
    public LogDAO() {
    }
    
    /**
     * Enregistrer une action dans les logs
     */
    public void log(String action, String description) {
        // Récupérer l'utilisateur connecté
        int userId = SessionManager.getInstance().getUserId();
        
        if (userId == -1) {
            System.err.println("⚠️ Tentative de log sans utilisateur connecté");
            return;
        }
        
        String sql = "INSERT INTO Log_Utilisateur (utilisateur_id, action, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, description);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur enregistrement log: " + e.getMessage());
        }
    }
    
    /**
     * Enregistrer un login
     */
    public void logLogin(int userId, String login) {
        String sql = "INSERT INTO Log_Utilisateur (utilisateur_id, action, description) VALUES (?, 'LOGIN', ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, "Connexion de " + login);
            stmt.executeUpdate();
            
            System.out.println("📝 Login enregistré pour : " + login);
            
        } catch (SQLException e) {
            System.err.println("Erreur log login: " + e.getMessage());
        }
    }
    
    /**
     * Enregistrer un logout
     */
    public void logLogout(int userId, String login) {
        String sql = "INSERT INTO Log_Utilisateur (utilisateur_id, action, description) VALUES (?, 'LOGOUT', ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, "Déconnexion de " + login);
            stmt.executeUpdate();
            
            System.out.println("📝 Logout enregistré pour : " + login);
            
        } catch (SQLException e) {
            System.err.println("Erreur log logout: " + e.getMessage());
        }
    }
    
    /**
     * Obtenir tous les logs
     */
    public List<String[]> getAllLogs() {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.login FROM Log_Utilisateur l " +
                    "JOIN Utilisateur u ON l.utilisateur_id = u.id " +
                    "ORDER BY l.dateAction DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String[] log = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("login"),
                    rs.getString("action"),
                    rs.getString("description"),
                    rs.getTimestamp("dateAction").toString()
                };
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération logs: " + e.getMessage());
        }
        
        return logs;
    }
    
    /**
     * Obtenir les logs d'un utilisateur
     */
    public List<String[]> getLogsByUser(int userId) {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.login FROM Log_Utilisateur l " +
                    "JOIN Utilisateur u ON l.utilisateur_id = u.id " +
                    "WHERE l.utilisateur_id = ? " +
                    "ORDER BY l.dateAction DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String[] log = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("login"),
                    rs.getString("action"),
                    rs.getString("description"),
                    rs.getTimestamp("dateAction").toString()
                };
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération logs utilisateur: " + e.getMessage());
        }
        
        return logs;
    }
    
    /**
     * Obtenir les logs par période
     */
    public List<String[]> getLogsByPeriode(Date dateDebut, Date dateFin) {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.login FROM Log_Utilisateur l " +
                    "JOIN Utilisateur u ON l.utilisateur_id = u.id " +
                    "WHERE l.dateAction BETWEEN ? AND ? " +
                    "ORDER BY l.dateAction DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, dateDebut);
            stmt.setDate(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String[] log = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("login"),
                    rs.getString("action"),
                    rs.getString("description"),
                    rs.getTimestamp("dateAction").toString()
                };
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération logs par période: " + e.getMessage());
        }
        
        return logs;
    }
    
    /**
     * Compter le nombre d'actions par type
     */
    public int countActionsByType(String action) {
        String sql = "SELECT COUNT(*) FROM Log_Utilisateur WHERE action = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, action);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur comptage actions: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Nettoyer les logs anciens (plus de X jours)
     */
    public void cleanOldLogs(int daysToKeep) {
        String sql = "DELETE FROM Log_Utilisateur WHERE dateAction < DATEADD(day, ?, GETDATE())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, -daysToKeep);
            int deleted = stmt.executeUpdate();
            
            System.out.println("🗑️ " + deleted + " logs supprimés (plus de " + daysToKeep + " jours)");
            
        } catch (SQLException e) {
            System.err.println("Erreur nettoyage logs: " + e.getMessage());
        }
    }
}
