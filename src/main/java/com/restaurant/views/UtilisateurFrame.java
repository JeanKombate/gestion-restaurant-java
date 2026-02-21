/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.restaurant.views;

import com.restaurant.dao.UtilisateurDAO;
import com.restaurant.models.Utilisateur;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class UtilisateurFrame extends JFrame {
    private JTable tableUtilisateurs;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRafraichir;
    private JComboBox<String> comboRoleFilter;
    
    public UtilisateurFrame() {
        initComponents();
        setupFrame();
        chargerUtilisateurs();
    }
    
    private void initComponents() {
        setTitle("Gestion des Utilisateurs");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel supérieur: recherche et filtres
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        topPanel.add(new JLabel("Rechercher:"));
        txtRecherche = new JTextField(15);
        txtRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rechercherUtilisateurs();
            }
        });
        topPanel.add(txtRecherche);
        
        topPanel.add(new JLabel("Rôle:"));
        comboRoleFilter = new JComboBox<>(new String[]{"Tous", "ADMIN", "EMPLOYE", "GESTIONNAIRE"});
        comboRoleFilter.addActionListener(e -> filtrerUtilisateurs());
        topPanel.add(comboRoleFilter);
        
        btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> chargerUtilisateurs());
        topPanel.add(btnRafraichir);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Table des utilisateurs
        String[] columns = {"ID", "Login", "Rôle", "Date création"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return Integer.class; // ID
                    case 1: return String.class;  // Login
                    case 2: return String.class;  // Rôle
                    case 3: return String.class;  // Date création
                    default: return Object.class;
                }
            }
        };
        
        tableUtilisateurs = new JTable(tableModel);
        tableUtilisateurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUtilisateurs.getTableHeader().setReorderingAllowed(false);
        tableUtilisateurs.setRowHeight(25);
        
        // Centrer les colonnes
        tableUtilisateurs.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableUtilisateurs.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableUtilisateurs.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableUtilisateurs.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        
        btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierUtilisateur());
        
        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        
        // Désactiver les boutons si non-admin
        if (!SessionManager.getInstance().estAdmin()) {
            btnAjouter.setEnabled(false);
            btnModifier.setEnabled(false);
            btnSupprimer.setEnabled(false);
        }
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Raccourcis clavier
        setupKeyboardShortcuts();
    }
    
    private void setupFrame() {
        setSize(700, 400);
        setLocationRelativeTo(null);
    }
    
    private void setupKeyboardShortcuts() {
        // F5 pour rafraîchir
        KeyStroke strokeRefresh = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        getRootPane().registerKeyboardAction(e -> chargerUtilisateurs(),
            strokeRefresh, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    private void chargerUtilisateurs() {
        tableModel.setRowCount(0);
        UtilisateurDAO dao = new UtilisateurDAO();
        List<Utilisateur> utilisateurs = dao.getAll();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Utilisateur u : utilisateurs) {
            String dateStr = u.getDateCreation() != null ? 
                sdf.format(u.getDateCreation()) : "";
            
            Object[] row = {
                u.getId(),
                u.getLogin(),
                u.getRole(),
                dateStr
            };
            tableModel.addRow(row);
        }
    }
    
    private void rechercherUtilisateurs() {
        String keyword = txtRecherche.getText().trim();
        if (keyword.isEmpty()) {
            chargerUtilisateurs();
            return;
        }
        
        tableModel.setRowCount(0);
        UtilisateurDAO dao = new UtilisateurDAO();
        List<Utilisateur> utilisateurs = dao.search(keyword);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Utilisateur u : utilisateurs) {
            String dateStr = u.getDateCreation() != null ? 
                sdf.format(u.getDateCreation()) : "";
            
            Object[] row = {
                u.getId(),
                u.getLogin(),
                u.getRole(),
                dateStr
            };
            tableModel.addRow(row);
        }
    }
    
    private void filtrerUtilisateurs() {
        String roleFilter = (String) comboRoleFilter.getSelectedItem();
        if ("Tous".equals(roleFilter)) {
            chargerUtilisateurs();
            return;
        }
        
        tableModel.setRowCount(0);
        UtilisateurDAO dao = new UtilisateurDAO();
        List<Utilisateur> utilisateurs = dao.getAll();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Utilisateur u : utilisateurs) {
            if (roleFilter.equals(u.getRole())) {
                String dateStr = u.getDateCreation() != null ? 
                    sdf.format(u.getDateCreation()) : "";
                
                Object[] row = {
                    u.getId(),
                    u.getLogin(),
                    u.getRole(),
                    dateStr
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void ajouterUtilisateur() {
        // Vérifier les permissions
        if (!SessionManager.getInstance().estAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Seuls les administrateurs peuvent ajouter des utilisateurs",
                "Permission refusée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField txtLogin = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> comboRole = new JComboBox<>(new String[]{"EMPLOYE", "GESTIONNAIRE", "ADMIN"});
        
        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Rôle:"));
        panel.add(comboRole);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Nouvel utilisateur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String login = txtLogin.getText().trim();
            String password = new String(txtPassword.getPassword());
            String role = (String) comboRole.getSelectedItem();
            
            // Validation
            if (login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Le login et le mot de passe sont requis", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this,
                    "Le mot de passe doit contenir au moins 4 caractères",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            UtilisateurDAO dao = new UtilisateurDAO();
            
            // Vérifier si le login existe déjà
            if (dao.existeLogin(login)) {
                JOptionPane.showMessageDialog(this,
                    "Ce login existe déjà. Choisissez un autre login.",
                    "Login existant",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur(login, password, role);
            
            if (dao.create(utilisateur)) {
                JOptionPane.showMessageDialog(this,
                    "Utilisateur créé avec succès. ID: " + utilisateur.getId(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                chargerUtilisateurs();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création de l'utilisateur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void modifierUtilisateur() {
        // Vérifier les permissions
        if (!SessionManager.getInstance().estAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Seuls les administrateurs peuvent modifier des utilisateurs",
                "Permission refusée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un utilisateur à modifier",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String loginActuel = (String) tableModel.getValueAt(selectedRow, 1);
        String roleActuel = (String) tableModel.getValueAt(selectedRow, 2);
        
        // Empêcher la modification de l'admin principal
        if (id == 1) {
            JOptionPane.showMessageDialog(this,
                "Impossible de modifier l'administrateur principal",
                "Opération non autorisée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JTextField txtLogin = new JTextField(loginActuel);
        JComboBox<String> comboRole = new JComboBox<>(new String[]{"EMPLOYE", "GESTIONNAIRE", "ADMIN"});
        comboRole.setSelectedItem(roleActuel);
        
        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Rôle:"));
        panel.add(comboRole);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Modifier l'utilisateur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nouveauLogin = txtLogin.getText().trim();
            String nouveauRole = (String) comboRole.getSelectedItem();
            
            if (nouveauLogin.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le login ne peut pas être vide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur utilisateur = dao.read(id);
            
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(this,
                    "Utilisateur introuvable",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier si le login a changé et existe déjà
            if (!utilisateur.getLogin().equals(nouveauLogin) && dao.existeLogin(nouveauLogin)) {
                JOptionPane.showMessageDialog(this,
                    "Ce login existe déjà. Choisissez un autre login.",
                    "Login existant",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Mettre à jour
            utilisateur.setLogin(nouveauLogin);
            utilisateur.setRole(nouveauRole);
            
            if (dao.update(utilisateur)) {
                JOptionPane.showMessageDialog(this,
                    "Utilisateur modifié avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                chargerUtilisateurs();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void supprimerUtilisateur() {
        // Vérifier les permissions
        if (!SessionManager.getInstance().estAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Seuls les administrateurs peuvent supprimer des utilisateurs",
                "Permission refusée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un utilisateur à supprimer",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String login = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Empêcher la suppression de l'admin principal
        if (id == 1) {
            JOptionPane.showMessageDialog(this,
                "Impossible de supprimer l'administrateur principal",
                "Opération non autorisée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Empêcher la suppression de son propre compte
        Utilisateur utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateurConnecte != null && utilisateurConnecte.getId() == id) {
            JOptionPane.showMessageDialog(this,
                "Vous ne pouvez pas supprimer votre propre compte",
                "Opération non autorisée",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer l'utilisateur : " + login + " ?\n" +
            "Cette action est irréversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            UtilisateurDAO dao = new UtilisateurDAO();
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this,
                    "Utilisateur supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                chargerUtilisateurs();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}