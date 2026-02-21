/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.models.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class UtilisateurDialog extends JDialog {
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> comboRole;
    private JComboBox<String> comboStatut;
    private JButton btnValider, btnAnnuler;
    private Utilisateur utilisateur;
    private boolean modeEdition;
    
    // Couleurs
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public UtilisateurDialog(Frame parent, Utilisateur utilisateur, boolean modeEdition) {
        super(parent, modeEdition ? "Modifier Utilisateur" : "Nouvel Utilisateur", true);
        this.utilisateur = utilisateur;
        this.modeEdition = modeEdition;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(500, 650);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Formulaire
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Charger les données si en mode édition
        if (modeEdition && utilisateur != null) {
            chargerDonnees();
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel(modeEdition ? "️ Modifier l'utilisateur" : "Créer un utilisateur");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(modeEdition ? "Modifiez les informations" : "Remplissez le formulaire");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Login
        formPanel.add(createLabel("Login"));
        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setPreferredSize(new Dimension(400, 40));
        txtLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Mot de passe
        formPanel.add(createLabel(modeEdition ? "Nouveau mot de passe (laisser vide pour ne pas changer)" : "Mot de passe"));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(400, 40));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Confirmer mot de passe
        formPanel.add(createLabel("Confirmer le mot de passe"));
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setPreferredSize(new Dimension(400, 40));
        txtConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtConfirmPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Rôle
        formPanel.add(createLabel("Rôle"));
        String[] roles = {"ADMIN", "UTILISATEUR", "GERANT", "SERVEUR"};
        comboRole = new JComboBox<>(roles);
        comboRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboRole.setPreferredSize(new Dimension(400, 40));
        comboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboRole.setBackground(Color.WHITE);
        formPanel.add(comboRole);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Statut
        formPanel.add(createLabel("Statut"));
        String[] statuts = {"ACTIF", "INACTIF"};
        comboStatut = new JComboBox<>(statuts);
        comboStatut.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboStatut.setPreferredSize(new Dimension(400, 40));
        comboStatut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboStatut.setBackground(Color.WHITE);
        formPanel.add(comboStatut);
        
        return formPanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Bouton Annuler
        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAnnuler.setPreferredSize(new Dimension(130, 42));
        btnAnnuler.setBackground(DANGER_COLOR);
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBorderPainted(false);
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAnnuler.addActionListener(e -> dispose());
        
        // Bouton Valider
        btnValider = new JButton(modeEdition ? "Modifier" : "Créer");
        btnValider.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnValider.setPreferredSize(new Dimension(130, 42));
        btnValider.setBackground(SUCCESS_COLOR);
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        btnValider.setBorderPainted(false);
        btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(btnAnnuler);
        buttonPanel.add(btnValider);
        
        return buttonPanel;
    }
    
    private void chargerDonnees() {
        txtLogin.setText(utilisateur.getLogin());
        comboRole.setSelectedItem(utilisateur.getRole());
        if (utilisateur.getStatut() != null) {
            comboStatut.setSelectedItem(utilisateur.getStatut());
        }
    }
    
    public boolean validerFormulaire() {
        // Vérifier le login
        if (txtLogin.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Le login est obligatoire !",
                "Validation",
                JOptionPane.WARNING_MESSAGE);
            txtLogin.requestFocus();
            return false;
        }
        
        // Vérifier le mot de passe seulement si ce n'est pas le mode édition
        // ou si un nouveau mot de passe est saisi
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (!modeEdition) {
            // En mode création, le mot de passe est obligatoire
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Le mot de passe est obligatoire !",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return false;
            }
        }
        
        // Si un mot de passe est saisi (création ou modification)
        if (!password.isEmpty()) {
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Le mot de passe doit contenir au moins 4 caractères !",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return false;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Les mots de passe ne correspondent pas !",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
                txtConfirmPassword.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    public Utilisateur getUtilisateur() {
        if (utilisateur == null) {
            utilisateur = new Utilisateur();
            utilisateur.setDateCreation(LocalDateTime.now());
        }
        
        utilisateur.setLogin(txtLogin.getText().trim());
        
        // Ne mettre à jour le mot de passe que s'il a été saisi
        String password = new String(txtPassword.getPassword());
        if (!password.isEmpty()) {
            utilisateur.setMotDePasse(password);
        }
        
        utilisateur.setRole((String) comboRole.getSelectedItem());
        utilisateur.setStatut((String) comboStatut.getSelectedItem());
        
        return utilisateur;
    }
    
    public JButton getBtnValider() {
        return btnValider;
    }
}