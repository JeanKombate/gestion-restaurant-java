/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import com.restaurant.models.Utilisateur;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 /*
 * @author Kanlanfaï KOMBATE
 */

public class UtilisateurDAO {
    
    // Méthode authentifier - AVEC MISE À JOUR DE LA DERNIÈRE CONNEXION
    public Utilisateur authentifier(String login, String password) {
        String sql = "SELECT * FROM Utilisateur WHERE login = ? AND motDePasse = ?";

        Utilisateur utilisateur = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setRole(rs.getString("role"));
                
                // Récupérer le statut
                try {
                    String statut = rs.getString("statut");
                    utilisateur.setStatut(statut != null ? statut : "ACTIF");
                } catch (SQLException e) {
                    utilisateur.setStatut("ACTIF");
                }

                // Mettre à jour la dernière connexion
                updateDerniereConnexion(utilisateur.getId());
                
                System.out.println("✅ Authentification réussie pour : " + login);
            } else {
                System.out.println("❌ Authentification échouée pour : " + login);
            }

        } catch (SQLException e) {
            System.err.println("Erreur authentification: " + e.getMessage());
            e.printStackTrace();
        }

        return utilisateur;
    }
    
    // Méthode helper pour mettre à jour la dernière connexion
    private void updateDerniereConnexion(int userId) {
        String sql = "UPDATE utilisateur SET derniereConnexion = GETDATE() WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("✅ Dernière connexion mise à jour pour l'utilisateur ID: " + userId);
            
        } catch (SQLException e) {
            System.err.println("⚠️ Impossible de mettre à jour la dernière connexion: " + e.getMessage());
        }
    }
    
    // NOUVEAU: Lire un utilisateur par ID
    public Utilisateur read(int id) {
        String sql = "SELECT id, login, role FROM utilisateur WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture utilisateur: " + e.getMessage());
        }
        return null;
    }
    
    // NOUVEAU: Mettre à jour un utilisateur
    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE Utilisateur SET login = ?, motDePasse = ?, role = ?, statut = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getLogin());
            stmt.setString(2, utilisateur.getMotDePasse());
            stmt.setString(3, utilisateur.getRole());
            stmt.setString(4, utilisateur.getStatut() != null ? utilisateur.getStatut() : "ACTIF");
            stmt.setInt(5, utilisateur.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur mis à jour : " + utilisateur.getLogin());
                return true;
            } else {
                System.err.println("Aucun utilisateur mis à jour");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // NOUVEAU: Mettre à jour le mot de passe
    public boolean updatePassword(int id, String ancienMotDePasse, String nouveauMotDePasse) {
        // D'abord vérifier l'ancien mot de passe
        String sqlVerif = "SELECT id FROM utilisateur WHERE id = ? AND mot_de_passe = SHA2(?, 256)";
        String sqlUpdate = "UPDATE utilisateur SET mot_de_passe = SHA2(?, 256) WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            // Vérification
            try (PreparedStatement pstmtVerif = conn.prepareStatement(sqlVerif)) {
                pstmtVerif.setInt(1, id);
                pstmtVerif.setString(2, ancienMotDePasse);
                
                ResultSet rs = pstmtVerif.executeQuery();
                if (!rs.next()) {
                    System.err.println("Ancien mot de passe incorrect");
                    return false;
                }
            }
            
            // Mise à jour
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setString(1, nouveauMotDePasse);
                pstmtUpdate.setInt(2, id);
                
                return pstmtUpdate.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour mot de passe: " + e.getMessage());
        }
        return false;
    }
    
    // Méthode existante: créer
    public boolean create(Utilisateur utilisateur) {
        // dateCreation a déjà une valeur par défaut (GETDATE()), on n'a pas besoin de la spécifier
        String sql = "INSERT INTO Utilisateur (login, motDePasse, role, statut) OUTPUT INSERTED.id " +
                    "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, utilisateur.getLogin());
            stmt.setString(2, utilisateur.getMotDePasse());
            stmt.setString(3, utilisateur.getRole());
            stmt.setString(4, utilisateur.getStatut() != null ? utilisateur.getStatut() : "ACTIF");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                utilisateur.setId(rs.getInt(1));
            }

            System.out.println("✅ Utilisateur créé : " + utilisateur.getLogin());
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Erreur création utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode existante: supprimer - CORRIGÉE AVEC GESTION DES CONTRAINTES
    public boolean delete(int id) {
        // Empêcher la suppression de l'admin principal
        if (id == 1) {
            System.err.println("⛔ Impossible de supprimer l'administrateur principal (ID=1)");
            return false;
        }
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction
            
            // ÉTAPE 1 : Supprimer d'abord les logs associés
            String sqlDeleteLogs = "DELETE FROM Log_Utilisateur WHERE utilisateur_id = ?";
            try (PreparedStatement pstmtLogs = conn.prepareStatement(sqlDeleteLogs)) {
                pstmtLogs.setInt(1, id);
                int logsDeleted = pstmtLogs.executeUpdate();
                System.out.println("🗑️ " + logsDeleted + " logs supprimés pour l'utilisateur ID: " + id);
            }
            
            // ÉTAPE 2 : Supprimer l'utilisateur
            String sqlDeleteUser = "DELETE FROM utilisateur WHERE id = ?";
            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlDeleteUser)) {
                pstmtUser.setInt(1, id);
                int rowsAffected = pstmtUser.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit(); // Valider la transaction
                    System.out.println("✅ Utilisateur supprimé avec succès (ID: " + id + ")");
                    return true;
                } else {
                    conn.rollback(); // Annuler si l'utilisateur n'existe pas
                    System.err.println("⚠️ Aucun utilisateur trouvé avec l'ID: " + id);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression utilisateur: " + e.getMessage());
            e.printStackTrace();
            
            // Annuler la transaction en cas d'erreur
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
            
        } finally {
            // Restaurer l'auto-commit et fermer la connexion
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // NOUVEAU: Rechercher des utilisateurs
    public List<Utilisateur> search(String keyword) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT id, login, role FROM utilisateur " +
                    "WHERE login LIKE ? OR role LIKE ? ORDER BY login";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                utilisateurs.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }
    
    // Méthode existante: getAll - AVEC LES COLONNES DE DATES
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        // Maintenant avec les colonnes de dates qui existent
        String sql = "SELECT id, login, role, statut, dateCreation, derniereConnexion FROM utilisateur ORDER BY login";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(mapUtilisateur(rs));
            }
            
            System.out.println("✅ " + utilisateurs.size() + " utilisateurs chargés");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
        return utilisateurs;
    }
    
    // Méthode existante: existeLogin
    public boolean existeLogin(String login) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE login = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification login: " + e.getMessage());
        }
        return false;
    }
    
    // Méthode helper pour mapper ResultSet -> Utilisateur
    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setRole(rs.getString("role"));
        
        // Gérer le statut
        try {
            String statut = rs.getString("statut");
            utilisateur.setStatut(statut != null ? statut : "ACTIF");
        } catch (SQLException e) {
            utilisateur.setStatut("ACTIF"); // Par défaut
        }
        
        // Gérer le mot de passe (non récupéré dans certaines requêtes)
        try {
            utilisateur.setMotDePasse(rs.getString("motDePasse"));
        } catch (SQLException e) {
            // motDePasse non présent dans le SELECT
        }
        
        // Gérer dateCreation
        try {
            Timestamp dateCreation = rs.getTimestamp("dateCreation");
            if (dateCreation != null) {
                utilisateur.setDateCreation(dateCreation);
            }
        } catch (SQLException e) {
            // dateCreation non présent ou NULL
        }
        
        // Gérer derniereConnexion
        try {
            Timestamp derniereConnexion = rs.getTimestamp("derniereConnexion");
            if (derniereConnexion != null) {
                utilisateur.setDerniereConnexion(derniereConnexion.toLocalDateTime());
            }
        } catch (SQLException e) {
            // derniereConnexion non présent ou NULL
        }
        
        return utilisateur;
    }
}