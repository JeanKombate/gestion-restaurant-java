/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.models.Commande;
import com.restaurant.models.LigneCommande;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class DetailCommandeView extends JDialog {
    private Commande commande;
    private SimpleDateFormat dateFormat;
    
    public DetailCommandeView(JFrame parent, Commande commande) {
        super(parent, "Détail de la commande #" + commande.getId(), true);
        this.commande = commande;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Informations de la commande
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations de la commande"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("N° Commande:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.valueOf(commande.getId())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(dateFormat.format(commande.getDateCommande())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("État:"), gbc);
        gbc.gridx = 1;
        JLabel lblEtat = new JLabel(commande.getEtat());
        switch (commande.getEtat()) {
            case "VALIDÉE":
                lblEtat.setForeground(new Color(0, 150, 0));
                break;
            case "ANNULÉE":
                lblEtat.setForeground(Color.RED);
                break;
            case "EN_COURS":
                lblEtat.setForeground(Color.ORANGE);
                break;
        }
        infoPanel.add(lblEtat, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        JLabel lblTotal = new JLabel(String.format("%.2f", commande.getTotal()));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(Color.BLUE);
        infoPanel.add(lblTotal, gbc);
        
        // Table des articles
        String[] columns = {"Produit", "Quantité", "Prix unitaire", "Montant"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tableArticles = new JTable(tableModel);
        tableArticles.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableArticles);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Articles commandés"));
        
        // Remplir la table
        if (commande.getLignes() != null) {
            for (LigneCommande ligne : commande.getLignes()) {
                Object[] row = {
                    ligne.getProduit().getNom(),
                    ligne.getQuantite(),
                    String.format("%.2f", ligne.getPrixUnitaire()),
                    String.format("%.2f", ligne.getMontantLigne())
                };
                tableModel.addRow(row);
            }
        }
        
        // Panel du bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());
        bottomPanel.add(btnFermer);
        
        // Assemblage
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}
