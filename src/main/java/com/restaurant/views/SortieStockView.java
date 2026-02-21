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
public class SortieStockView extends JDialog {
    private JComboBox<Produit> cbProduit;
    private JTextField txtQuantite;
    private JTextArea txtMotif;
    private JLabel lblStockActuel;
    private JButton btnValider, btnAnnuler;
    
    private ProduitDAO produitDAO;
    private MouvementStockDAO mouvementStockDAO;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color WARNING_COLOR = new Color(230, 126, 34);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    
    public SortieStockView(JFrame parent) {
        super(parent, "Sortie de Stock", true);
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
        headerPanel.setBackground(WARNING_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel("Sortie de Stock");
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
                afficherStockActuel();
            }
        });
        form.add(cbProduit);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Stock actuel
        form.add(createLabel("Stock actuel"));
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        stockPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stockPanel.setOpaque(false);
        
        lblStockActuel = new JLabel("0");
        lblStockActuel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStockActuel.setForeground(WARNING_COLOR);
        
        stockPanel.add(lblStockActuel);
        form.add(stockPanel);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Quantité
        form.add(createLabel("Quantité à sortir *"));
        txtQuantite = createTextField("Ex: 20");
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
        
        btnValider = createButton("Valider la sortie", WARNING_COLOR);
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerSortie();
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
            afficherStockActuel();
        }
    }
    
    private void afficherStockActuel() {
        Produit produit = (Produit) cbProduit.getSelectedItem();
        if (produit != null) {
            lblStockActuel.setText(String.valueOf(produit.getStockActuel()));
            
            // Mettre en évidence si le stock est faible
            if (produit.getStockActuel() <= produit.getSeuilAlerte() && produit.getSeuilAlerte() > 0) {
                lblStockActuel.setForeground(DANGER_COLOR);
            } else {
                lblStockActuel.setForeground(WARNING_COLOR);
            }
        } else {
            lblStockActuel.setText("0");
            lblStockActuel.setForeground(WARNING_COLOR);
        }
    }
    
    private boolean isPlaceholder(JTextField field, String placeholder) {
        return field.getForeground().equals(Color.GRAY) && field.getText().equals(placeholder);
    }
    
    private void validerSortie() {
        // Validation du produit
        if (cbProduit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation de la quantité
        if (txtQuantite.getText().trim().isEmpty() || isPlaceholder(txtQuantite, "Ex: 20")) {
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
            
            // Vérifier le stock
            if (produit.getStockActuel() < quantite) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuffisant !\n\n" +
                    "Stock actuel: " + produit.getStockActuel() + " unités\n" +
                    "Quantité demandée: " + quantite + " unités\n" +
                    "Déficit: " + (quantite - produit.getStockActuel()) + " unités",
                    "Erreur de stock",
                    JOptionPane.ERROR_MESSAGE);
                txtQuantite.requestFocus();
                txtQuantite.selectAll();
                return;
            }
            
            // Vérifier le seuil d'alerte
            int stockApresSortie = produit.getStockActuel() - quantite;
            if (produit.getSeuilAlerte() > 0 && stockApresSortie <= produit.getSeuilAlerte()) {
                int reponse = JOptionPane.showConfirmDialog(this,
                    "Attention ! Après cette sortie, le stock sera en alerte.\n\n" +
                    "Stock après sortie: " + stockApresSortie + "\n" +
                    "Seuil d'alerte: " + produit.getSeuilAlerte() + "\n\n" +
                    "Voulez-vous continuer ?",
                    "Alerte de stock",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                    
                if (reponse != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            String motif = txtMotif.getText().trim();
            if (motif.isEmpty()) {
                motif = "Sortie manuelle";
            }
            
            // Préparer le message de confirmation
            String message = "Confirmez-vous la sortie de stock ?\n\n" +
                           "Produit: " + produit.getNom() + "\n" +
                           "Quantité: " + quantite + " unités\n" +
                           "Motif: " + motif + "\n\n" +
                           "Stock actuel: " + produit.getStockActuel() + "\n" +
                           "Stock après sortie: " + stockApresSortie;
            
            // Confirmation
            int confirm = JOptionPane.showConfirmDialog(this,
                message,
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Enregistrer la sortie
                mouvementStockDAO.enregistrerSortie(produit, quantite, motif);
                
                // Message de succès
                String successMessage = "Sortie de stock enregistrée avec succès !\n\n" +
                                      "Produit: " + produit.getNom() + "\n" +
                                      "Quantité sortie: " + quantite + " unités\n" +
                                      "Nouveau stock: " + stockApresSortie;
                
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