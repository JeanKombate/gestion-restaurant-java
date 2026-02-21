/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class Commande {
        private int id;
    private Date dateCommande;
    private String etat; // EN_COURS, VALIDÉE, ANNULÉE
    private double total;
    private List<LigneCommande> lignes;
    
    // Constructeurs
    public Commande() {
        this.dateCommande = new Date();
        this.etat = "EN_COURS";
        this.total = 0.0;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getDateCommande() { return dateCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }
    
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
    
    // Méthode pour calculer le total
    public void calculerTotal() {
        if (lignes != null) {
            total = 0;
            for (LigneCommande ligne : lignes) {
                total += ligne.getMontantLigne();
            }
        }
    }
}
