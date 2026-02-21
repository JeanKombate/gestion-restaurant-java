/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

/**
 * Modèle représentant un fournisseur
 */

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class Fournisseur {
    private int id;
    private String nom;
    private String contact;
    private String telephone;
    private String email;
    private String adresse;
    private String specialite; // Ex: "Boissons", "Viandes", "Légumes"
    private boolean actif;
    
    // Constructeurs
    public Fournisseur() {
        this.actif = true;
    }
    
    public Fournisseur(String nom, String contact, String telephone, String email) {
        this.nom = nom;
        this.contact = contact;
        this.telephone = telephone;
        this.email = email;
        this.actif = true;
    }
    
    public Fournisseur(String nom, String contact, String telephone, String email, 
                      String adresse, String specialite) {
        this.nom = nom;
        this.contact = contact;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.specialite = specialite;
        this.actif = true;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    @Override
    public String toString() {
        return nom + " - " + specialite;
    }
}