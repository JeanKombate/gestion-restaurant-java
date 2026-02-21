/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.models.Produit;
import com.restaurant.models.Categorie;
import com.restaurant.dao.CategorieDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProduitDialog extends JDialog {
    private JTextField txtNom;
    private JComboBox<Categorie> comboCategorie;
    private JTextField txtPrixVente;
    private JTextField txtStockActuel;
    private JTextField txtSeuilAlerte;
    private JButton btnValider, btnAnnuler;
    private Produit produit;
    private boolean modeEdition;
    private CategorieDAO categorieDAO;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    
    public ProduitDialog(Frame parent, Produit produit, boolean modeEdition) {
        super(parent, modeEdition ? "Modifier le Produit" : "Nouveau Produit", true);
        this.produit = produit;
        this.modeEdition = modeEdition;
        this.categorieDAO = new CategorieDAO();
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(550, 750);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel iconLabel = new JLabel(modeEdition ? " Modifier le produit" : "Créer un nouveau produit");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Formulaire avec scroll
        JScrollPane scrollPane = new JScrollPane(createFormPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Boutons
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        
        if (modeEdition && produit != null) {
            chargerDonnees();
        }
    }
    
    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(CARD_COLOR);
        form.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Nom
        form.add(createLabel(" Nom du produit *"));
        txtNom = createTextField("Ex: Pizza Margherita");
        form.add(txtNom);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Catégorie
        form.add(createLabel("️ Catégorie *"));
        comboCategorie = new JComboBox<>();
        comboCategorie.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboCategorie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        comboCategorie.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadCategories();
        form.add(comboCategorie);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Prix
        form.add(createLabel("Prix de vente (FCFA) *"));
        txtPrixVente = createTextField("Ex: 5000");
        form.add(txtPrixVente);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Stock
        form.add(createLabel(" Stock actuel *"));
        txtStockActuel = createTextField("Ex: 100");
        form.add(txtStockActuel);
        form.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Seuil
        form.add(createLabel("️ Seuil d'alerte"));
        JLabel hint = new JLabel("Vous serez alerté quand le stock descend sous ce seuil");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(TEXT_SECONDARY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        txtSeuilAlerte = createTextField("Ex: 20");
        form.add(txtSeuilAlerte);
        
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
    
    private void loadCategories() {
        List<Categorie> categories = categorieDAO.getAll();
        for (Categorie cat : categories) {
            comboCategorie.addItem(cat);
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        
        btnAnnuler = createButton(" Annuler", DANGER_COLOR);
        btnAnnuler.addActionListener(e -> dispose());
        
        btnValider = createButton(modeEdition ? " Enregistrer" : " Créer", SUCCESS_COLOR);
        
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
        btn.setPreferredSize(new Dimension(140, 45));
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
    
    private void chargerDonnees() {
        txtNom.setForeground(Color.BLACK);
        txtNom.setText(produit.getNom());
        
        txtPrixVente.setForeground(Color.BLACK);
        txtPrixVente.setText(String.valueOf(produit.getPrixVente()));
        
        txtStockActuel.setForeground(Color.BLACK);
        txtStockActuel.setText(String.valueOf(produit.getStockActuel()));
        
        txtSeuilAlerte.setForeground(Color.BLACK);
        txtSeuilAlerte.setText(String.valueOf(produit.getSeuilAlerte()));
        
        for (int i = 0; i < comboCategorie.getItemCount(); i++) {
            if (comboCategorie.getItemAt(i).getId() == produit.getCategorie().getId()) {
                comboCategorie.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private boolean isPlaceholder(JTextField field, String placeholder) {
        return field.getForeground().equals(Color.GRAY) && field.getText().equals(placeholder);
    }
    
    public boolean validerFormulaire() {
        if (txtNom.getText().trim().isEmpty() || isPlaceholder(txtNom, "Ex: Pizza Margherita")) {
            JOptionPane.showMessageDialog(this, " Le nom du produit est obligatoire !");
            return false;
        }
        
        if (comboCategorie.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "️ Veuillez sélectionner une catégorie !");
            return false;
        }
        
        try {
            if (isPlaceholder(txtPrixVente, "Ex: 5000")) {
                JOptionPane.showMessageDialog(this, "️ Le prix de vente est obligatoire !");
                return false;
            }
            double prix = Double.parseDouble(txtPrixVente.getText().trim());
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this, "️ Le prix doit être supérieur à zéro !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "️ Le prix doit être un nombre valide !");
            return false;
        }
        
        try {
            if (isPlaceholder(txtStockActuel, "Ex: 100")) {
                JOptionPane.showMessageDialog(this, "️ Le stock actuel est obligatoire !");
                return false;
            }
            int stock = Integer.parseInt(txtStockActuel.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "️ Le stock ne peut pas être négatif !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, " Le stock doit être un nombre entier !");
            return false;
        }
        
        return true;
    }
    
    public Produit getProduit() {
        if (produit == null) {
            produit = new Produit();
        }
        
        produit.setNom(txtNom.getText().trim());
        produit.setCategorie((Categorie) comboCategorie.getSelectedItem());
        produit.setPrixVente(Double.parseDouble(txtPrixVente.getText().trim()));
        produit.setStockActuel(Integer.parseInt(txtStockActuel.getText().trim()));
        
        if (!isPlaceholder(txtSeuilAlerte, "Ex: 10") && !txtSeuilAlerte.getText().trim().isEmpty()) {
            produit.setSeuilAlerte(Integer.parseInt(txtSeuilAlerte.getText().trim()));
        } else {
            produit.setSeuilAlerte(0);
        }
        
        return produit;
    }
    
    public JButton getBtnValider() {
        return btnValider;
    }
}