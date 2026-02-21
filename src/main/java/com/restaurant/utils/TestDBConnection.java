package com.restaurant.utils;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("=== Test amélioré de la connexion ===");
        
        // Test 1: Connexion basique
        System.out.println("\n1. Test de connexion basique...");
        boolean testOk = DBConnection.testConnection();
        System.out.println("Test connexion: " + (testOk ? "✅ SUCCÈS" : "❌ ÉCHEC"));
        
        // Test 2: Obtenir une connexion
        System.out.println("\n2. Obtention d'une connexion...");
        Connection conn1 = DBConnection.getConnection();
        System.out.println("Connexion obtenue: " + (conn1 != null ? "✅" : "❌"));
        
        // Test 3: Vérifier la validité
        System.out.println("\n3. Vérification validité...");
        boolean isValid = DBConnection.isConnectionValid();
        System.out.println("Connexion valide: " + (isValid ? "✅" : "❌"));
        
        // Test 4: Obtenir une nouvelle connexion (pour transaction)
        System.out.println("\n4. Obtention d'une nouvelle connexion...");
        Connection conn2 = DBConnection.getNewConnection();
        System.out.println("Nouvelle connexion: " + (conn2 != null ? "✅" : "❌"));
        
        // Test 5: Informations
        System.out.println("\n5. Informations de connexion:");
        System.out.println(DBConnection.getConnectionInfo());
        
        // Nettoyage
        try {
            if (conn2 != null) {
                conn2.close();
                System.out.println("✅ Connexion test fermée");
            }
            
            // Fermer la connexion du thread
            DBConnection.closeThreadConnection();
            System.out.println("✅ Connexion thread fermée");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur nettoyage: " + e.getMessage());
        }
        
        System.out.println("\n=== Test terminé ===");
    }
}