/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.utils;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class DBConnection {
    // Supprimer la connexion statique - chaque thread aura sa propre connexion
    // private static Connection connection = null;
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;"
                                    + "databaseName=gestion_restaurant;"
                                    + "encrypt=true;"
                                    + "trustServerCertificate=true";
    private static final String USER = "sa";  // Votre utilisateur
    private static final String PASSWORD = "123456";  // Votre mot de passe
    
    // Pool simple de connexions pour éviter les fermetures intempestives
    private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    
    public static Connection getConnection() {
        Connection connection = threadLocalConnection.get();
        
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("🔄 Création d'une nouvelle connexion pour le thread " + Thread.currentThread().getId());
                
                // Charger le driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                
                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connection.setAutoCommit(true); // Par défaut, auto-commit activé
                
                // Stocker dans ThreadLocal pour ce thread
                threadLocalConnection.set(connection);
                
                System.out.println("✅ Connexion établie pour le thread " + Thread.currentThread().getId());
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Driver JDBC introuvable : " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Driver introuvable", e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur de connexion à la base : " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Erreur connexion", e);
            
            // Tentative de reconnexion
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connection.setAutoCommit(true);
                threadLocalConnection.set(connection);
                System.out.println("✅ Reconnexion réussie après erreur");
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Échec reconnexion", ex);
            }
        }
        
        return connection;
    }
    
    // Nouvelle méthode pour obtenir une nouvelle connexion (pour les transactions)
    public static Connection getNewConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection newConn = DriverManager.getConnection(URL, USER, PASSWORD);
            newConn.setAutoCommit(true);
            System.out.println("🔗 Nouvelle connexion créée (sans ThreadLocal)");
            return newConn;
        } catch (Exception e) {
            System.err.println("❌ Impossible de créer une nouvelle connexion: " + e.getMessage());
            return null;
        }
    }
    
    // Fermer la connexion pour ce thread
    public static void closeThreadConnection() {
        Connection connection = threadLocalConnection.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("🔒 Connexion fermée pour le thread " + Thread.currentThread().getId());
                }
            } catch (SQLException e) {
                System.err.println("Erreur fermeture connexion: " + e.getMessage());
            } finally {
                threadLocalConnection.remove(); // Retirer du ThreadLocal
            }
        }
    }
    
    // Fermer toutes les connexions (à appeler à la fermeture de l'application)
    public static void closeAllConnections() {
        // Cette méthode est difficile à implémenter avec ThreadLocal
        // Nous devons garder une trace de toutes les connexions
        System.out.println("⚠️  Fermeture de toutes les connexions non gérée avec ThreadLocal");
        System.out.println("⚠️  Utilisez closeThreadConnection() dans chaque thread");
    }
    
    // Méthode pour les transactions
    public static void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        if (conn != null) {
            conn.setAutoCommit(false);
            System.out.println("🚀 Transaction démarrée");
        }
    }
    
    public static void commitTransaction() throws SQLException {
        Connection conn = getConnection();
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("✅ Transaction validée");
        }
    }
    
    public static void rollbackTransaction() {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("↩️ Transaction annulée");
            } catch (SQLException e) {
                System.err.println("Erreur rollback: " + e.getMessage());
            }
        }
    }
    
    // Getters pour les paramètres de connexion
    public static String getURL() { return URL; }
    public static String getUser() { return USER; }
    public static String getPassword() { return PASSWORD; }
    
    // Test de la connexion
    public static boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✓ Test de connexion : Réussi !");
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Échec de connexion SQL Server : " + e.getMessage());
            return false;
        }
    }

    public static String getConnectionInfo() {
        StringBuilder info = new StringBuilder();
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                info.append("=== Informations de connexion ===\n");
                info.append("Base de données : ").append(conn.getCatalog()).append("\n");
                info.append("URL : ").append(URL).append("\n");
                info.append("Utilisateur : ").append(USER).append("\n");
                info.append("Statut : Connecté\n");
                info.append("Auto-commit : ").append(conn.getAutoCommit()).append("\n");
                info.append("Transaction isolation : ").append(conn.getTransactionIsolation()).append("\n");
                info.append("Thread ID : ").append(Thread.currentThread().getId()).append("\n");
            } else {
                info.append("Aucune connexion active.");
            }
        } catch (SQLException e) {
            info.append("Erreur lors de la récupération des informations : ").append(e.getMessage());
        }
        return info.toString();
    }
    
    // Vérifier si la connexion est valide
    public static boolean isConnectionValid() {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            try {
                return conn.isValid(3); // Test de 3 secondes
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }
}