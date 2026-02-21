/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class LigneCommande {
    private int id;
    private Produit produit;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne;
    
    // Constructeurs
    public LigneCommande() {}
    
    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        if (produit != null) {
            this.prixUnitaire = produit.getPrixVente();
        }
        calculerMontant();
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { 
        this.produit = produit;
        if (produit != null) {
            this.prixUnitaire = produit.getPrixVente();
        }
    }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { 
        this.quantite = quantite;
        calculerMontant();
    }
    
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { 
        this.prixUnitaire = prixUnitaire;
        calculerMontant();
    }
    
    public double getMontantLigne() { 
        return montantLigne; 
    }
    
    // IMPORTANT : Modifiez cette méthode pour qu'elle ne lance pas d'exception
    public void setMontantLigne(double montantLigne) {
        this.montantLigne = montantLigne;
    }
    
    // Méthode privée
    private void calculerMontant() {
        this.montantLigne = quantite * prixUnitaire;
    }
    
    @Override
    public String toString() {
        return quantite + " x " + (produit != null ? produit.getNom() : "N/A") + 
               " = " + montantLigne;
    }
}
