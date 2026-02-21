/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.utils;

import com.restaurant.models.Utilisateur;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestionnaire de session utilisateur (Singleton)
 * Gère l'état de la session pendant l'exécution de l'application
 */

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class SessionManager {
    
    private static SessionManager instance;
    private Utilisateur utilisateurConnecte;
    private LocalDateTime heureConnexion;
    
    // Constructeur privé (Singleton)
    private SessionManager() {
    }
    
    /**
     * Obtenir l'instance unique du SessionManager
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Connecter un utilisateur
     * @param utilisateur
     */
    public void login(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.heureConnexion = LocalDateTime.now();
        System.out.println("✅ Session ouverte pour : " + utilisateur.getLogin() + 
                         " (" + utilisateur.getRole() + ") à " + 
                         heureConnexion.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    /**
     * Déconnecter l'utilisateur actuel
     */
    public void logout() {
        if (utilisateurConnecte != null) {
            System.out.println("✅ Déconnexion de : " + utilisateurConnecte.getLogin());
            utilisateurConnecte = null;
            heureConnexion = null;
        }
    }
    
    /**
     * Obtenir l'utilisateur connecté
     * @return 
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * Vérifier si un utilisateur est connecté
     */
    public boolean isLoggedIn() {
        return utilisateurConnecte != null;
    }
    
    /**
     * Vérifier si l'utilisateur connecté est administrateur
     */
    public boolean isAdmin() {
        return utilisateurConnecte != null && 
               "ADMIN".equalsIgnoreCase(utilisateurConnecte.getRole());
    }
    
    /**
     * Obtenir l'ID de l'utilisateur connecté
     */
    public int getUserId() {
        return utilisateurConnecte != null ? utilisateurConnecte.getId() : -1;
    }
    
    /**
     * Obtenir le login de l'utilisateur connecté
     */
    public String getLogin() {
        return utilisateurConnecte != null ? utilisateurConnecte.getLogin() : "Non connecté";
    }
    
    /**
     * Obtenir le rôle de l'utilisateur connecté
     */
    public String getRole() {
        return utilisateurConnecte != null ? utilisateurConnecte.getRole() : "AUCUN";
    }
    
    /**
     * Obtenir le nom d'affichage complet
     */
    public String getDisplayName() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getLogin() + " (" + utilisateurConnecte.getRole() + ")";
        }
        return "Non connecté";
    }
    
    /**
     * Obtenir l'heure de connexion
     */
    public String getHeureConnexion() {
        if (heureConnexion != null) {
            return heureConnexion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        return "N/A";
    }
    
    /**
     * Obtenir la durée de la session
     */
    public String getDureeSession() {
        if (heureConnexion != null) {
            LocalDateTime maintenant = LocalDateTime.now();
            long minutes = java.time.Duration.between(heureConnexion, maintenant).toMinutes();
            long heures = minutes / 60;
            minutes = minutes % 60;
            return heures + "h " + minutes + "m";
        }
        return "N/A";
    }
    
    /**
     * Vérifier si l'utilisateur a la permission pour une action
     */
    public boolean hasPermission(String permission) {
        if (!isLoggedIn()) {
            return false;
        }
        
        // Les admins ont toutes les permissions
        if (isAdmin()) {
            return true;
        }
        
        // Définir les permissions pour les autres rôles
        return switch (permission) {
            case "GERER_UTILISATEURS", "GENERER_RAPPORTS", "GERER_FOURNISSEURS", "BACKUP_DATABASE" -> false;
            case "GERER_PRODUITS", "GERER_COMMANDES", "GERER_STOCK" -> true;
            default -> false;
        }; // Réservé aux admins
        // Accessible à tous les utilisateurs connectés
    }
    
    /**
     * Afficher les informations de session
     */
    public void afficherInfosSession() {
        System.out.println("\n=== Informations de session ===");
        System.out.println("Utilisateur : " + getLogin());
        System.out.println("Rôle : " + getRole());
        System.out.println("Heure connexion : " + getHeureConnexion());
        System.out.println("Durée session : " + getDureeSession());
        System.out.println("Admin : " + (isAdmin() ? "Oui" : "Non"));
        System.out.println("==============================\n");
    }

    /**
     * Vérifier si l'utilisateur connecté est administrateur (alias)
     */
    public boolean estAdmin() {
        return isAdmin();
    }
}