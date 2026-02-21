/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.dao.MouvementStockDAO;
import com.restaurant.models.Produit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class EntreeStockView extends JDialog {
    private JComboBox<Produit> cbProduit;
    private JTextField txtQuantite;
    private JTextArea txtMotif;
    private JButton btnValider, btnAnnuler;
    private JLabel lblStockActuel;
    
    private ProduitDAO produitDAO;
    private MouvementStockDAO mouvementStockDAO;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    
    public EntreeStockView(JFrame parent) {
        super(parent, "Entrée de Stock", true);
        initComponents();
        chargerProduits();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(500, 550);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel("Entrée de Stock");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Formulaire
        JScrollPane scrollPane = new JScrollPane(createFormPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Boutons
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(CARD_COLOR);
        form.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        produitDAO = new ProduitDAO();
        mouvementStockDAO = new MouvementStockDAO();
        
        // Produit
        form.add(createLabel("Produit"));
        cbProduit = new JComboBox<>();
        cbProduit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbProduit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cbProduit.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbProduit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStockInfo();
            }
        });
        form.add(cbProduit);
        form.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Stock actuel
        form.add(createLabel("Stock actuel"));
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        stockPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stockPanel.setOpaque(false);
        
        lblStockActuel = new JLabel("-");
        lblStockActuel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStockActuel.setForeground(PRIMARY_COLOR);
        
        stockPanel.add(lblStockActuel);
        form.add(stockPanel);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Quantité
        form.add(createLabel("Quantité *"));
        txtQuantite = createTextField("Ex: 50");
        form.add(txtQuantite);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Motif
        form.add(createLabel("Motif"));
        txtMotif = new JTextArea(4, 20);
        txtMotif.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMotif.setLineWrap(true);
        txtMotif.setWrapStyleWord(true);
        txtMotif.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JScrollPane motifScroll = new JScrollPane(txtMotif);
        motifScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        motifScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(motifScroll);
        
        return form;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return label;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        
        btnAnnuler = createButton("Annuler", DANGER_COLOR);
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnValider = createButton("Valider l'entrée", SUCCESS_COLOR);
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerEntree();
            }
        });
        
        panel.add(btnAnnuler);
        panel.add(btnValider);
        return panel;
    }
    
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    private void chargerProduits() {
        List<Produit> produits = produitDAO.getAll();
        DefaultComboBoxModel<Produit> model = new DefaultComboBoxModel<>();
        for (Produit produit : produits) {
            model.addElement(produit);
        }
        cbProduit.setModel(model);
        
        if (cbProduit.getItemCount() > 0) {
            updateStockInfo();
        }
    }
    
    private void updateStockInfo() {
        Produit produit = (Produit) cbProduit.getSelectedItem();
        if (produit != null) {
            lblStockActuel.setText(String.valueOf(produit.getStockActuel()));
        } else {
            lblStockActuel.setText("-");
        }
    }
    
    private boolean isPlaceholder(JTextField field, String placeholder) {
        return field.getForeground().equals(Color.GRAY) && field.getText().equals(placeholder);
    }
    
    private void validerEntree() {
        // Validation du produit
        if (cbProduit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation de la quantité
        if (txtQuantite.getText().trim().isEmpty() || isPlaceholder(txtQuantite, "Ex: 50")) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir une quantité", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtQuantite.requestFocus();
            return;
        }
        
        try {
            int quantite = Integer.parseInt(txtQuantite.getText().trim());
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La quantité doit être positive", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                txtQuantite.requestFocus();
                txtQuantite.selectAll();
                return;
            }
            
            Produit produit = (Produit) cbProduit.getSelectedItem();
            String motif = txtMotif.getText().trim();
            if (motif.isEmpty()) {
                motif = "Réapprovisionnement";
            }
            
            // Préparer le message de confirmation
            String message = "Confirmez-vous l'entrée de stock ?\n\n" +
                           "Produit: " + produit.getNom() + "\n" +
                           "Quantité: " + quantite + " unités\n" +
                           "Motif: " + motif + "\n\n" +
                           "Stock actuel: " + produit.getStockActuel() + "\n" +
                           "Nouveau stock: " + (produit.getStockActuel() + quantite);
            
            // Confirmation
            int confirm = JOptionPane.showConfirmDialog(this,
                message,
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Enregistrer l'entrée
                mouvementStockDAO.enregistrerEntree(produit, quantite, motif);
                
                // Message de succès
                String successMessage = "Entrée de stock enregistrée avec succès !\n\n" +
                                      "Produit: " + produit.getNom() + "\n" +
                                      "Quantité ajoutée: " + quantite + " unités\n" +
                                      "Nouveau stock: " + (produit.getStockActuel() + quantite);
                
                JOptionPane.showMessageDialog(this,
                    successMessage,
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir une quantité valide (nombre entier)", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtQuantite.requestFocus();
            txtQuantite.selectAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}