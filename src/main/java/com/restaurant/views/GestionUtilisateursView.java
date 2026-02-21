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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.*;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class GestionUtilisateursView extends JFrame {
    private JTable tableUtilisateurs;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser, btnResetPassword;
    private JTextField txtRecherche;
    private JLabel lblTotal, lblAdmins, lblUtilisateurs, lblActifs;
    private UtilisateurDAO utilisateurDAO;
    
    // Couleurs du thème
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color INFO_COLOR = new Color(142, 68, 173);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public GestionUtilisateursView() {
        // Vérifier les permissions
        if (!SessionManager.getInstance().isAdmin()) {
            JOptionPane.showMessageDialog(null,
                "⛔ Accès refusé ! Seuls les administrateurs peuvent gérer les utilisateurs.",
                "Accès refusé",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        initComponents();
        setupListeners();
        loadUtilisateurs();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion des Utilisateurs");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        utilisateurDAO = new UtilisateurDAO();
        
        // Panel principal avec fond moderne
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header avec titre et statistiques
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central avec table
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Titre et sous-titre
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel("Gestion des Utilisateurs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Gérez les comptes et les privilèges");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        // Panel des statistiques
        JPanel statsPanel = createStatsPanel();
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(CARD_COLOR);
        
        // Carte Total
        lblTotal = createStatCard("0", "Total", PRIMARY_COLOR);
        statsPanel.add(lblTotal);
        
        // Carte Admins
        lblAdmins = createStatCard("0", "Admins", DANGER_COLOR);
        statsPanel.add(lblAdmins);
        
        // Carte Utilisateurs
        lblUtilisateurs = createStatCard("0", "Utilisateurs", SUCCESS_COLOR);
        statsPanel.add(lblUtilisateurs);
        
        // Carte Actifs
        lblActifs = createStatCard("0", "Actifs", INFO_COLOR);
        statsPanel.add(lblActifs);
        
        return statsPanel;
    }
    
    private JLabel createStatCard(String value, String label, Color color) {
        JLabel card = new JLabel();
        card.setLayout(new BorderLayout(5, 5));
        card.setOpaque(true);
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        
        String html = String.format(
            "<html><div style='text-align: center;'>" +
            "<div style='font-size: 22px; font-weight: bold; color: rgb(%d,%d,%d);'>%s</div>" +
            "<div style='font-size: 11px; color: rgb(127,140,141); margin-top: 3px;'>%s</div>" +
            "</div></html>",
            color.getRed(), color.getGreen(), color.getBlue(), value, label
        );
        card.setText(html);
        card.setHorizontalAlignment(SwingConstants.CENTER);
        
        return card;
    }
    
    private void updateStatCard(JLabel card, String value, String label, Color color) {
        String html = String.format(
            "<html><div style='text-align: center;'>" +
            "<div style='font-size: 22px; font-weight: bold; color: rgb(%d,%d,%d);'>%s</div>" +
            "<div style='font-size: 11px; color: rgb(127,140,141); margin-top: 3px;'>%s</div>" +
            "</div></html>",
            color.getRed(), color.getGreen(), color.getBlue(), value, label
        );
        card.setText(html);
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Barre d'outils avec recherche
        JPanel toolbarPanel = createToolbarPanel();
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        
        // Panel de la table
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createToolbarPanel() {
        JPanel toolbarPanel = new JPanel(new BorderLayout(15, 0));
        toolbarPanel.setBackground(CARD_COLOR);
        toolbarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Panel de gauche avec recherche
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(CARD_COLOR);
        
        // Icône de recherche
        JLabel searchIcon = new JLabel("");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leftPanel.add(searchIcon);
        
        // Champ de recherche avec placeholder
        txtRecherche = new JTextField(18);
        txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRecherche.setPreferredSize(new Dimension(200, 35));
        txtRecherche.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtRecherche.setForeground(Color.GRAY);
        txtRecherche.setText("Rechercher un utilisateur");
        
        // Ajouter les listeners pour le placeholder
        txtRecherche.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtRecherche.getText().equals("Rechercher un utilisateur")) {
                    txtRecherche.setText("");
                    txtRecherche.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtRecherche.getText().isEmpty()) {
                    txtRecherche.setForeground(Color.GRAY);
                    txtRecherche.setText("Rechercher un utilisateur");
                }
            }
        });
        
        leftPanel.add(txtRecherche);
        
        // Panel de droite avec boutons d'action
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(CARD_COLOR);
        
        // Bouton Ajouter
        btnAjouter = createStyledButton("Ajouter", SUCCESS_COLOR);
        rightPanel.add(btnAjouter);
        
        // Bouton Modifier
        btnModifier = createStyledButton(" Modifier", PRIMARY_COLOR);
        rightPanel.add(btnModifier);
        
        // Bouton Reset Password
        btnResetPassword = createStyledButton("Reset MDP", WARNING_COLOR);
        rightPanel.add(btnResetPassword);
        
        // Bouton Supprimer
        btnSupprimer = createStyledButton("Supprimer", DANGER_COLOR);
        rightPanel.add(btnSupprimer);
        
        // Bouton Actualiser
        btnActualiser = createStyledButton(" Actualiser", new Color(52, 73, 94));
        rightPanel.add(btnActualiser);
        
        toolbarPanel.add(leftPanel, BorderLayout.WEST);
        toolbarPanel.add(rightPanel, BorderLayout.EAST);
        
        return toolbarPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;
            Color hoverColor = bgColor.darker();
            
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Colonnes de la table
        String[] columns = {"ID", "Login", "Rôle", "Statut", "Date Création", "Dernière Connexion"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableUtilisateurs = new JTable(tableModel);
        tableUtilisateurs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableUtilisateurs.setRowHeight(40);
        tableUtilisateurs.setGridColor(new Color(240, 240, 240));
        tableUtilisateurs.setSelectionBackground(new Color(232, 245, 253));
        tableUtilisateurs.setSelectionForeground(TEXT_PRIMARY);
        tableUtilisateurs.setShowGrid(true);
        tableUtilisateurs.setIntercellSpacing(new Dimension(1, 1));
        tableUtilisateurs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style du header avec meilleure visibilité
        JTableHeader header = tableUtilisateurs.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PRIMARY_COLOR));
        header.setOpaque(true);
        header.setReorderingAllowed(false);
        
        // Renderer personnalisé pour le header
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBackground(new Color(52, 73, 94));
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
                return label;
            }
        });
        
        // Largeurs des colonnes
        tableUtilisateurs.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableUtilisateurs.getColumnModel().getColumn(0).setMaxWidth(60);
        tableUtilisateurs.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableUtilisateurs.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableUtilisateurs.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableUtilisateurs.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableUtilisateurs.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        // Centrer les colonnes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableUtilisateurs.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Renderer personnalisé pour la colonne Rôle avec badges
        tableUtilisateurs.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                label.setOpaque(true);
                
                String role = value.toString();
                
                if (role.equalsIgnoreCase("ADMIN")) {
                    label.setBackground(isSelected ? new Color(231, 76, 60, 150) : new Color(231, 76, 60, 30));
                    label.setForeground(new Color(231, 76, 60));
                    label.setText(" ADMIN");
                } else {
                    label.setBackground(isSelected ? new Color(52, 152, 219, 150) : new Color(52, 152, 219, 30));
                    label.setForeground(new Color(52, 152, 219));
                    label.setText(" UTILISATEUR");
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                return label;
            }
        });
        
        // Renderer personnalisé pour la colonne Statut
        tableUtilisateurs.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                label.setOpaque(true);
                
                String statut = value.toString();
                
                if (statut.equalsIgnoreCase("ACTIF")) {
                    label.setBackground(isSelected ? new Color(39, 174, 96, 150) : new Color(39, 174, 96, 30));
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("● ACTIF");
                } else {
                    label.setBackground(isSelected ? new Color(189, 195, 199, 150) : new Color(189, 195, 199, 30));
                    label.setForeground(new Color(127, 140, 141));
                    label.setText("○ INACTIF");
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel du bas avec information
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(250, 250, 250));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel infoLabel = new JLabel(" Double-cliquez sur un utilisateur pour le modifier");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        bottomPanel.add(infoLabel);
        
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void setupListeners() {
        // Recherche en temps réel
        txtRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrerUtilisateurs();
            }
        });
        
        // Bouton Ajouter
        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        
        // Bouton Modifier
        btnModifier.addActionListener(e -> modifierUtilisateur());
        
        // Bouton Reset Password
        btnResetPassword.addActionListener(e -> resetPassword());
        
        // Bouton Supprimer
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        
        // Bouton Actualiser
        btnActualiser.addActionListener(e -> {
            loadUtilisateurs();
            JOptionPane.showMessageDialog(this,
                "✅ Liste actualisée avec succès !",
                "Actualisation",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Double-clic sur la table
        tableUtilisateurs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modifierUtilisateur();
                }
            }
        });
    }
    
    private void filtrerUtilisateurs() {
        String recherche = txtRecherche.getText().toLowerCase();
        
        // Ignorer si c'est le texte du placeholder
        if (recherche.equals("rechercher un utilisateur")) {
            recherche = "";
        }
        
        // Vider la table
        tableModel.setRowCount(0);
        
        // Charger et filtrer les utilisateurs
        var utilisateurs = utilisateurDAO.getAll();
        
        for (Utilisateur user : utilisateurs) {
            // Filtrer par recherche
            boolean matchRecherche = recherche.isEmpty() || 
                                     user.getLogin().toLowerCase().contains(recherche);
            
            if (matchRecherche) {
                Object[] row = {
                    user.getId(),
                    user.getLogin(),
                    user.getRole(),
                    user.getStatut() != null ? user.getStatut() : "ACTIF",
                    user.getDateCreation() != null ? user.getDateCreation().toString() : "N/A",
                    user.getDerniereConnexion() != null ? user.getDerniereConnexion().toString() : "Jamais"
                };
                tableModel.addRow(row);
            }
        }
        
        updateStatistics();
    }
    
    private void ajouterUtilisateur() {
        UtilisateurDialog dialog = new UtilisateurDialog(this, null, false);
        
        dialog.getBtnValider().addActionListener(e -> {
            if (dialog.validerFormulaire()) {
                Utilisateur nouveauUser = dialog.getUtilisateur();
                utilisateurDAO.create(nouveauUser);
                JOptionPane.showMessageDialog(this,
                    "✅ Utilisateur créé avec succès !\nLogin: " + nouveauUser.getLogin(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadUtilisateurs();
            }
        });
        
        dialog.setVisible(true);
    }
    
    private void modifierUtilisateur() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner un utilisateur à modifier",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        Utilisateur user = utilisateurDAO.read(userId);
        
        if (user != null) {
            UtilisateurDialog dialog = new UtilisateurDialog(this, user, true);
            
            dialog.getBtnValider().addActionListener(e -> {
                if (dialog.validerFormulaire()) {
                    Utilisateur userModifie = dialog.getUtilisateur();
                    utilisateurDAO.update(userModifie);
                    JOptionPane.showMessageDialog(this,
                        "✅ Utilisateur modifié avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUtilisateurs();
                }
            });
            
            dialog.setVisible(true);
        }
    }
    
    private void resetPassword() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner un utilisateur",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String login = (String) tableModel.getValueAt(selectedRow, 1);
        
        String newPassword = JOptionPane.showInputDialog(this,
            "🔑 Entrez le nouveau mot de passe pour: " + login,
            "Réinitialisation du mot de passe",
            JOptionPane.QUESTION_MESSAGE);
        
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            Utilisateur user = utilisateurDAO.read(userId);
            
            if (user != null) {
                user.setMotDePasse(newPassword);
                utilisateurDAO.update(user);
                JOptionPane.showMessageDialog(this,
                    "✅ Mot de passe réinitialisé avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void supprimerUtilisateur() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner un utilisateur à supprimer",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String login = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Empêcher la suppression de son propre compte
        if (SessionManager.getInstance().isLoggedIn() && 
            userId == SessionManager.getInstance().getUserId()) {
            JOptionPane.showMessageDialog(this,
                "⛔ Vous ne pouvez pas supprimer votre propre compte !",
                "Action interdite",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "🗑️ Êtes-vous sûr de vouloir supprimer l'utilisateur : " + login + " ?\n" +
            "Cette action est irréversible !",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = utilisateurDAO.delete(userId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Utilisateur supprimé avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadUtilisateurs();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "❌ Impossible de supprimer l'utilisateur.\nVérifiez qu'il n'est pas référencé ailleurs.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de la suppression : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    public void loadUtilisateurs() {
        // Vider la table
        tableModel.setRowCount(0);
        
        // Charger les utilisateurs depuis la base
        var utilisateurs = utilisateurDAO.getAll();
        
        for (Utilisateur user : utilisateurs) {
            Object[] row = {
                user.getId(),
                user.getLogin(),
                user.getRole(),
                user.getStatut() != null ? user.getStatut() : "ACTIF",
                user.getDateCreation() != null ? user.getDateCreation().toString() : "N/A",
                user.getDerniereConnexion() != null ? user.getDerniereConnexion().toString() : "Jamais"
            };
            tableModel.addRow(row);
        }
        
        updateStatistics();
    }
    
    private void updateStatistics() {
        int total = tableModel.getRowCount();
        int admins = 0;
        int utilisateurs = 0;
        int actifs = 0;
        
        for (int i = 0; i < total; i++) {
            String role = (String) tableModel.getValueAt(i, 2);
            String statut = (String) tableModel.getValueAt(i, 3);
            
            if (role.equalsIgnoreCase("ADMIN")) {
                admins++;
            } else {
                utilisateurs++;
            }
            
            if (statut.equalsIgnoreCase("ACTIF")) {
                actifs++;
            }
        }
        
        updateStatCard(lblTotal, String.valueOf(total), "Total", PRIMARY_COLOR);
        updateStatCard(lblAdmins, String.valueOf(admins), "Admins", DANGER_COLOR);
        updateStatCard(lblUtilisateurs, String.valueOf(utilisateurs), "Utilisateurs", SUCCESS_COLOR);
        updateStatCard(lblActifs, String.valueOf(actifs), "Actifs", INFO_COLOR);
    }
}