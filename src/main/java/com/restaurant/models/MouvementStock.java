/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.models;

import java.util.Date;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class MouvementStock {
    private int id;
    private String type; // ENTRÉE ou SORTIE
    private Produit produit;
    private int quantite;
    private Date dateMouvement;
    private String motif;
    
    // Constructeurs
    public MouvementStock() {
        this.dateMouvement = new Date();
    }
    
    public MouvementStock(String type, Produit produit, int quantite, String motif) {
        this.type = type;
        this.produit = produit;
        this.quantite = quantite;
        this.dateMouvement = new Date();
        this.motif = motif;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public Date getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(Date dateMouvement) { this.dateMouvement = dateMouvement; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
