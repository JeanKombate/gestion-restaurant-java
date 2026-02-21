/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class Utilisateur {
    private int id;
    private String login;
    private String motDePasse;
    private String role;
    private String statut;
    private Timestamp dateCreation;
    private LocalDateTime derniereConnexion;
    
    public Utilisateur() {}
    
    public Utilisateur(String login, String motDePasse, String role) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.statut = "ACTIF"; // Statut par défaut
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }
    
    // Support pour LocalDateTime
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = Timestamp.valueOf(dateCreation);
    }
    
    public LocalDateTime getDerniereConnexion() { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime derniereConnexion) { 
        this.derniereConnexion = derniereConnexion; 
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", login, role);
    }
}