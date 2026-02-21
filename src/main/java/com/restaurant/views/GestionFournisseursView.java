/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.dao.FournisseurDAO;
import com.restaurant.dao.LogDAO;
import com.restaurant.models.Fournisseur;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionFournisseursView extends JFrame {
    
    private JTable tableFournisseurs;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnDesactiver, btnActiver, btnFermer;
    private JTextField txtSearch;
    private JComboBox<String> cbFiltreActif;
    
    private FournisseurDAO fournisseurDAO;
    private LogDAO logDAO;
    
    public GestionFournisseursView() {
        // Vérifier les permissions
        if (!SessionManager.getInstance().hasPermission("GERER_FOURNISSEURS")) {
            JOptionPane.showMessageDialog(null,
                "Accès refusé - Réservé aux administrateurs",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        initComponents();
        chargerFournisseurs();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion des Fournisseurs");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        fournisseurDAO = new FournisseurDAO();
        logDAO = new LogDAO();
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel supérieur avec recherche et filtres
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("🔍");
        btnSearch.addActionListener(e -> rechercherFournisseur());
        searchPanel.add(btnSearch);
        
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Statut:"));
        cbFiltreActif = new JComboBox<>(new String[]{"Tous", "Actifs", "Inactifs"});
        cbFiltreActif.addActionListener(e -> filtrerFournisseurs());
        searchPanel.add(cbFiltreActif);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        
        // Table des fournisseurs
        String[] columns = {"ID", "Nom", "Contact", "Téléphone", "Email", "Spécialité", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableFournisseurs = new JTable(tableModel);
        tableFournisseurs.setRowHeight(30);
        
        // Colorer selon le statut
        tableFournisseurs.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String statut = (String) table.getValueAt(row, 6);
                if ("Inactif".equals(statut)) {
                    c.setBackground(new Color(255, 220, 220));
                } else {
                    c.setBackground(Color.WHITE);
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableFournisseurs);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        btnAjouter = new JButton("➕ Ajouter");
        btnAjouter.setBackground(new Color(0, 153, 76));
        btnAjouter.setForeground(Color.WHITE);
        
        btnModifier = new JButton("✏️ Modifier");
        btnModifier.setBackground(new Color(0, 102, 204));
        btnModifier.setForeground(Color.WHITE);
        
        btnSupprimer = new JButton("🗑️ Supprimer");
        btnSupprimer.setBackground(new Color(204, 0, 0));
        btnSupprimer.setForeground(Color.WHITE);
        
        btnDesactiver = new JButton("⏸️ Désactiver");
        btnDesactiver.setBackground(new Color(255, 165, 0));
        btnDesactiver.setForeground(Color.BLACK);
        
        btnActiver = new JButton("▶️ Activer");
        btnActiver.setBackground(new Color(0, 153, 76));
        btnActiver.setForeground(Color.WHITE);
        
        btnFermer = new JButton("❌ Fermer");
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnDesactiver);
        buttonPanel.add(btnActiver);
        buttonPanel.add(btnFermer);
        
        // Listeners
        btnAjouter.addActionListener(e -> ajouterFournisseur());
        btnModifier.addActionListener(e -> modifierFournisseur());
        btnSupprimer.addActionListener(e -> supprimerFournisseur());
        btnDesactiver.addActionListener(e -> changerStatut(false));
        btnActiver.addActionListener(e -> changerStatut(true));
        btnFermer.addActionListener(e -> dispose());
        
        // Double-clic pour modifier
        tableFournisseurs.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modifierFournisseur();
                }
            }
        });
        
        // Assemblage
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerFournisseurs() {
        tableModel.setRowCount(0);
        
        for (Fournisseur f : fournisseurDAO.getAll()) {
            Object[] row = {
                f.getId(),
                f.getNom(),
                f.getContact(),
                f.getTelephone(),
                f.getEmail(),
                f.getSpecialite(),
                f.isActif() ? "Actif" : "Inactif"
            };
            tableModel.addRow(row);
        }
    }
    
    private void filtrerFournisseurs() {
        tableModel.setRowCount(0);
        String filtre = (String) cbFiltreActif.getSelectedItem();
        
        for (Fournisseur f : fournisseurDAO.getAll()) {
            boolean afficher = false;
            
            if ("Tous".equals(filtre)) {
                afficher = true;
            } else if ("Actifs".equals(filtre) && f.isActif()) {
                afficher = true;
            } else if ("Inactifs".equals(filtre) && !f.isActif()) {
                afficher = true;
            }
            
            if (afficher) {
                Object[] row = {
                    f.getId(),
                    f.getNom(),
                    f.getContact(),
                    f.getTelephone(),
                    f.getEmail(),
                    f.getSpecialite(),
                    f.isActif() ? "Actif" : "Inactif"
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void rechercherFournisseur() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            filtrerFournisseurs();
            return;
        }
        
        tableModel.setRowCount(0);
        for (Fournisseur f : fournisseurDAO.searchByName(searchTerm)) {
            Object[] row = {
                f.getId(),
                f.getNom(),
                f.getContact(),
                f.getTelephone(),
                f.getEmail(),
                f.getSpecialite(),
                f.isActif() ? "Actif" : "Inactif"
            };
            tableModel.addRow(row);
        }
    }
    
    private void ajouterFournisseur() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField txtNom = new JTextField();
        JTextField txtContact = new JTextField();
        JTextField txtTelephone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAdresse = new JTextField();
        JComboBox<String> cbSpecialite = new JComboBox<>(new String[]{
            "Boissons", "Viandes", "Légumes et fruits", "Épicerie générale", 
            "Produits laitiers", "Boulangerie", "Autre"
        });
        
        panel.add(new JLabel("Nom:"));
        panel.add(txtNom);
        panel.add(new JLabel("Contact:"));
        panel.add(txtContact);
        panel.add(new JLabel("Téléphone:"));
        panel.add(txtTelephone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Adresse:"));
        panel.add(txtAdresse);
        panel.add(new JLabel("Spécialité:"));
        panel.add(cbSpecialite);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Ajouter un fournisseur", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String nom = txtNom.getText().trim();
            
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Le nom est obligatoire", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (fournisseurDAO.nomExists(nom)) {
                JOptionPane.showMessageDialog(this, 
                    "Un fournisseur avec ce nom existe déjà", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Fournisseur f = new Fournisseur(
                    nom,
                    txtContact.getText().trim(),
                    txtTelephone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAdresse.getText().trim(),
                    (String) cbSpecialite.getSelectedItem()
                );
                
                fournisseurDAO.create(f);
                logDAO.log("CREATE_FOURNISSEUR", "Création fournisseur: " + nom);
                
                JOptionPane.showMessageDialog(this, 
                    "Fournisseur créé avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerFournisseurs();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void modifierFournisseur() {
        int selectedRow = tableFournisseurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un fournisseur", 
                "Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int fournisseurId = (int) tableModel.getValueAt(selectedRow, 0);
        Fournisseur f = fournisseurDAO.read(fournisseurId);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField txtNom = new JTextField(f.getNom());
        JTextField txtContact = new JTextField(f.getContact());
        JTextField txtTelephone = new JTextField(f.getTelephone());
        JTextField txtEmail = new JTextField(f.getEmail());
        JTextField txtAdresse = new JTextField(f.getAdresse());
        JComboBox<String> cbSpecialite = new JComboBox<>(new String[]{
            "Boissons", "Viandes", "Légumes et fruits", "Épicerie générale", 
            "Produits laitiers", "Boulangerie", "Autre"
        });
        cbSpecialite.setSelectedItem(f.getSpecialite());
        
        panel.add(new JLabel("Nom:"));
        panel.add(txtNom);
        panel.add(new JLabel("Contact:"));
        panel.add(txtContact);
        panel.add(new JLabel("Téléphone:"));
        panel.add(txtTelephone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Adresse:"));
        panel.add(txtAdresse);
        panel.add(new JLabel("Spécialité:"));
        panel.add(cbSpecialite);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Modifier le fournisseur", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                f.setNom(txtNom.getText().trim());
                f.setContact(txtContact.getText().trim());
                f.setTelephone(txtTelephone.getText().trim());
                f.setEmail(txtEmail.getText().trim());
                f.setAdresse(txtAdresse.getText().trim());
                f.setSpecialite((String) cbSpecialite.getSelectedItem());
                
                fournisseurDAO.update(f);
                logDAO.log("UPDATE_FOURNISSEUR", "Modification fournisseur: " + f.getNom());
                
                JOptionPane.showMessageDialog(this, 
                    "Fournisseur modifié avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerFournisseurs();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void supprimerFournisseur() {
        int selectedRow = tableFournisseurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un fournisseur", 
                "Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int fournisseurId = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer le fournisseur '" + nom + "' ?\n" +
            "Cette action est irréversible.", 
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fournisseurDAO.delete(fournisseurId);
                logDAO.log("DELETE_FOURNISSEUR", "Suppression fournisseur: " + nom);
                
                JOptionPane.showMessageDialog(this, 
                    "Fournisseur supprimé avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerFournisseurs();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur: " + e.getMessage(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void changerStatut(boolean activer) {
        int selectedRow = tableFournisseurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un fournisseur", 
                "Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int fournisseurId = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            if (activer) {
                fournisseurDAO.reactiver(fournisseurId);
                logDAO.log("ACTIVER_FOURNISSEUR", "Activation fournisseur: " + nom);
            } else {
                fournisseurDAO.desactiver(fournisseurId);
                logDAO.log("DESACTIVER_FOURNISSEUR", "Désactivation fournisseur: " + nom);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Statut modifié avec succès", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerFournisseurs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}