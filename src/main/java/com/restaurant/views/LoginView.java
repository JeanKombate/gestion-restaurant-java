/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class LoginView extends JFrame {
    // Composants
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;
    private JLabel lblMessage;
    
    public LoginView() {
        initComponents();
        setLocationRelativeTo(null); // Centrer la fenêtre
    }
    
    private void initComponents() {
        setTitle("Connexion - Gestion Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Panel du titre avec dégradé visuel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(245, 245, 250));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("AUTHENTIFICATION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Système de Gestion de Restaurant");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(245, 245, 250));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        mainPanel.add(titleContainer, BorderLayout.NORTH);
        
        // Panel du formulaire avec meilleur styling
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblLogin = new JLabel("Login :");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLogin.setForeground(new Color(60, 60, 60));
        formPanel.add(lblLogin, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtLogin = new JTextField(20);
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtLogin.setPreferredSize(new Dimension(250, 35));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtLogin, gbc);
        
        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPassword = new JLabel("Mot de passe :");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(60, 60, 60));
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtPassword, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel des boutons avec meilleur design
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        // Bouton Annuler - Style moderne
        btnCancel = new JButton("Annuler");
        btnCancel.setPreferredSize(new Dimension(130, 40));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        // Effet hover pour Annuler
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(new Color(231, 76, 60));
            }
        });

        // Bouton Se connecter - Style moderne
        btnLogin = new JButton("Se connecter");
        btnLogin.setPreferredSize(new Dimension(130, 40));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(new Color(39, 174, 96));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        // Effet hover pour Se connecter
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(30, 130, 76));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(39, 174, 96));
            }
        });
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnLogin);
        
        // Panel du message d'erreur
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(245, 245, 250));
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setForeground(new Color(231, 76, 60));
        messagePanel.add(lblMessage);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(245, 245, 250));
        southPanel.add(messagePanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Ajouter le panel principal à la fenêtre
        add(mainPanel);
        pack();
    }
    
    // Getters pour les données
    public String getLogin() {
        return txtLogin.getText();
    }
    
    public String getPassword() {
        return new String(txtPassword.getPassword());
    }
    
    // Méthodes pour gérer les événements
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }
    
    public void addCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }
    
    // Méthodes utilitaires
    public void showMessage(String message, boolean isError) {
        lblMessage.setText(message);
        lblMessage.setForeground(isError ? new Color(231, 76, 60) : new Color(39, 174, 96));
    }
    
    public void clearFields() {
        txtLogin.setText("");
        txtPassword.setText("");
        lblMessage.setText(" ");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Erreur d'authentification", 
            JOptionPane.ERROR_MESSAGE);
    }
}