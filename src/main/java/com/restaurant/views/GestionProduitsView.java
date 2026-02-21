/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.views;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.models.Produit;
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
public class GestionProduitsView extends JFrame {
    private JTable tableProduits;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser, btnExporter;
    private JTextField txtRecherche;
    private JLabel lblTotal, lblDisponible, lblStockBas, lblRupture;
    private ProduitDAO produitDAO;
    
    // Couleurs du thème
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public GestionProduitsView() {
        initComponents();
        setupListeners();
        loadProduits();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion des Produits");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        produitDAO = new ProduitDAO();
        
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
        
        JLabel titleLabel = new JLabel("Gestion des Produits");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Gérez votre catalogue de produits");
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
        
        // Carte Disponible
        lblDisponible = createStatCard("0", "Disponible", SUCCESS_COLOR);
        statsPanel.add(lblDisponible);
        
        // Carte Stock Bas
        lblStockBas = createStatCard("0", "Stock Bas", WARNING_COLOR);
        statsPanel.add(lblStockBas);
        
        // Carte Rupture
        lblRupture = createStatCard("0", "Rupture", DANGER_COLOR);
        statsPanel.add(lblRupture);
        
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
        
        // Barre d'outils avec recherche et filtres
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
        
        // Panel de gauche avec recherche et filtre
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
        txtRecherche.setText("Rechercher un produit");
        
        // Ajouter les listeners pour le placeholder
        txtRecherche.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtRecherche.getText().equals("Rechercher un produit")) {
                    txtRecherche.setText("");
                    txtRecherche.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtRecherche.getText().isEmpty()) {
                    txtRecherche.setForeground(Color.GRAY);
                    txtRecherche.setText("Rechercher un produit");
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
        
        // Bouton Supprimer
        btnSupprimer = createStyledButton(" Supprimer", DANGER_COLOR);
        rightPanel.add(btnSupprimer);
        
        // Bouton Actualiser
        btnActualiser = createStyledButton("Actualiser", new Color(52, 73, 94));
        rightPanel.add(btnActualiser);
        
        // Bouton Exporter
        btnExporter = createStyledButton(" Exporter", new Color(142, 68, 173));
        rightPanel.add(btnExporter);
        
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
        button.setPreferredSize(new Dimension(120, 38));
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
        String[] columns = {"ID", "Nom", "Catégorie", "Prix (FCFA)", "Stock", "Seuil", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProduits = new JTable(tableModel);
        tableProduits.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableProduits.setRowHeight(40);
        tableProduits.setGridColor(new Color(240, 240, 240));
        tableProduits.setSelectionBackground(new Color(232, 245, 253));
        tableProduits.setSelectionForeground(TEXT_PRIMARY);
        tableProduits.setShowGrid(true);
        tableProduits.setIntercellSpacing(new Dimension(1, 1));
        tableProduits.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style du header avec meilleure visibilité
        JTableHeader header = tableProduits.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 73, 94));  // Gris-bleu foncé
        header.setForeground(Color.WHITE);  // Texte blanc
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(41, 128, 185)));
        header.setOpaque(true);
        header.setReorderingAllowed(false);
        
        // Renderer personnalisé pour le header pour garantir les couleurs
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
        tableProduits.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableProduits.getColumnModel().getColumn(0).setMaxWidth(60);
        tableProduits.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableProduits.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableProduits.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableProduits.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableProduits.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableProduits.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        // Centrer les colonnes numériques
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableProduits.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableProduits.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableProduits.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
        // Renderer personnalisé pour la colonne Prix
        DefaultTableCellRenderer priceRenderer = new DefaultTableCellRenderer();
        priceRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableProduits.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
        
        // Renderer personnalisé pour la colonne Statut avec badges
        tableProduits.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                label.setOpaque(true);
                
                String statut = value.toString();
                
                if (statut.equals("DISPONIBLE")) {
                    label.setBackground(isSelected ? new Color(46, 204, 113, 150) : new Color(46, 204, 113, 30));
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("● DISPONIBLE");
                } else if (statut.equals("STOCK BAS")) {
                    label.setBackground(isSelected ? new Color(243, 156, 18, 150) : new Color(243, 156, 18, 30));
                    label.setForeground(new Color(243, 156, 18));
                    label.setText("⚠ STOCK BAS");
                } else if (statut.equals("RUPTURE")) {
                    label.setBackground(isSelected ? new Color(231, 76, 60, 150) : new Color(231, 76, 60, 30));
                    label.setForeground(new Color(231, 76, 60));
                    label.setText("✖ RUPTURE");
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableProduits);
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
        
        JLabel infoLabel = new JLabel("💡 Double-cliquez sur un produit pour le modifier rapidement");
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
                filtrerProduits();
            }
        });
        
        // Bouton Ajouter
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterProduit();
            }
        });
        
        // Bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierProduit();
            }
        });
        
        // Bouton Supprimer
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerProduit();
            }
        });
        
        // Bouton Actualiser
        btnActualiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProduits();
                JOptionPane.showMessageDialog(GestionProduitsView.this,
                    "Liste actualisée avec succès !",
                    "Actualisation",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Bouton Exporter
        btnExporter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GestionProduitsView.this,
                    "Fonctionnalité d'export en cours de développement...",
                    "Export",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Double-clic sur la table
        tableProduits.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modifierProduit();
                }
            }
        });
    }
    
    private void filtrerProduits() {
        String recherche = txtRecherche.getText().toLowerCase();
        
        // Ignorer si c'est le texte du placeholder
        if (recherche.equals("rechercher un produit")) {
            recherche = "";
        }
        
        // Vider la table
        tableModel.setRowCount(0);
        
        // Charger et filtrer les produits
        var produits = produitDAO.getAll();
        
        for (Produit produit : produits) {
            String statut = getStatut(produit);
            
            // Filtrer par recherche
            boolean matchRecherche = recherche.isEmpty() || produit.getNom().toLowerCase().contains(recherche);
            
            if (matchRecherche) {
                Object[] row = {
                    produit.getId(),
                    produit.getNom(),
                    produit.getCategorie().getLibelle(),
                    String.format("%,.0f", produit.getPrixVente()),
                    produit.getStockActuel(),
                    produit.getSeuilAlerte(),
                    statut
                };
                tableModel.addRow(row);
            }
        }
        
        updateStatistics();
    }
    
    private String getStatut(Produit produit) {
        if (produit.getStockActuel() == 0) {
            return "RUPTURE";
        } else if (produit.getStockActuel() < produit.getSeuilAlerte()) {
            return "STOCK BAS";
        } else {
            return "DISPONIBLE";
        }
    }
    
    private void ajouterProduit() {
        ProduitDialog dialog = new ProduitDialog(this, null, false);
        
        dialog.getBtnValider().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog.validerFormulaire()) {
                    Produit nouveauProduit = dialog.getProduit();
                    produitDAO.create(nouveauProduit);
                    JOptionPane.showMessageDialog(GestionProduitsView.this,
                        "✅ Produit ajouté avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadProduits();
                }
            }
        });
        
        dialog.setVisible(true);
    }
    
    private void modifierProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner un produit à modifier",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int produitId = (int) tableModel.getValueAt(selectedRow, 0);
        Produit produit = produitDAO.read(produitId);
        
        if (produit != null) {
            ProduitDialog dialog = new ProduitDialog(this, produit, true);
            
            dialog.getBtnValider().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dialog.validerFormulaire()) {
                        Produit produitModifie = dialog.getProduit();
                        produitDAO.update(produitModifie);
                        JOptionPane.showMessageDialog(GestionProduitsView.this,
                            "✅ Produit modifié avec succès !",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadProduits();
                    }
                }
            });
            
            dialog.setVisible(true);
        }
    }
    
    private void supprimerProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner un produit à supprimer",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int produitId = (int) tableModel.getValueAt(selectedRow, 0);
        String produitNom = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "🗑️ Êtes-vous sûr de vouloir supprimer le produit : " + produitNom + " ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produitDAO.delete(produitId);
                JOptionPane.showMessageDialog(this,
                    "✅ Produit supprimé avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                loadProduits();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de la suppression : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void loadProduits() {
        // Vider la table
        tableModel.setRowCount(0);
        
        // Charger les produits depuis la base
        var produits = produitDAO.getAll();
        
        for (Produit produit : produits) {
            String statut = getStatut(produit);
            
            Object[] row = {
                produit.getId(),
                produit.getNom(),
                produit.getCategorie().getLibelle(),
                String.format("%,.0f", produit.getPrixVente()),
                produit.getStockActuel(),
                produit.getSeuilAlerte(),
                statut
            };
            tableModel.addRow(row);
        }
        
        updateStatistics();
    }
    
    private void updateStatistics() {
        int total = tableModel.getRowCount();
        int disponible = 0;
        int stockBas = 0;
        int rupture = 0;
        
        for (int i = 0; i < total; i++) {
            String statut = (String) tableModel.getValueAt(i, 6);
            switch (statut) {
                case "DISPONIBLE":
                    disponible++;
                    break;
                case "STOCK BAS":
                    stockBas++;
                    break;
                case "RUPTURE":
                    rupture++;
                    break;
            }
        }
        
        updateStatCard(lblTotal, String.valueOf(total), "Total", PRIMARY_COLOR);
        updateStatCard(lblDisponible, String.valueOf(disponible), "Disponible", SUCCESS_COLOR);
        updateStatCard(lblStockBas, String.valueOf(stockBas), "Stock Bas", WARNING_COLOR);
        updateStatCard(lblRupture, String.valueOf(rupture), "Rupture", DANGER_COLOR);
    }
}