/*
 * Vue CORRIGÉE de la liste des commandes
 */
package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.models.Commande;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListeCommandesView extends JFrame {
    private JTable tableCommandes;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFiltreEtat;
    private JTextField txtRecherche;
    private JButton btnActualiser, btnVoirDetail, btnAnnuler, btnExporter, btnNouvelle;
    private JLabel lblTotal, lblValidees, lblEnCours, lblAnnulees;
    
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneCommandeDAO;
    private SimpleDateFormat dateFormat;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public ListeCommandesView() {
        initComponents();
        setupListeners();
        chargerCommandes();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Liste des Commandes");
        setSize(1300, 750);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        commandeDAO = new CommandeDAO();
        ligneCommandeDAO = new LigneCommandeDAO();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Titre
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel("Liste des Commandes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Consultez et gérez toutes les commandes");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Statistiques AVEC LABELS VISIBLES
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(CARD_COLOR);
        
        statsPanel.add(createStatCardPanel("0", "Total", PRIMARY_COLOR));
        statsPanel.add(createStatCardPanel("0", "Validées", SUCCESS_COLOR));
        statsPanel.add(createStatCardPanel("0", "En cours", WARNING_COLOR));
        statsPanel.add(createStatCardPanel("0", "Annulées", DANGER_COLOR));
        
        // Récupérer les labels pour mise à jour
        lblTotal = (JLabel) ((JPanel) statsPanel.getComponent(0)).getComponent(0);
        lblValidees = (JLabel) ((JPanel) statsPanel.getComponent(1)).getComponent(0);
        lblEnCours = (JLabel) ((JPanel) statsPanel.getComponent(2)).getComponent(0);
        lblAnnulees = (JLabel) ((JPanel) statsPanel.getComponent(3)).getComponent(0);
        
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatCardPanel(String value, String label, Color color) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(TEXT_SECONDARY);
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(valueLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(labelText);
        
        return cardPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Barre d'outils
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setBackground(CARD_COLOR);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftToolbar.setBackground(CARD_COLOR);
        
        txtRecherche = new JTextField(20);
        txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRecherche.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtRecherche.setPreferredSize(new Dimension(250, 38));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(txtRecherche, BorderLayout.CENTER);
        
        leftToolbar.add(searchPanel);
        
        JLabel filterLabel = new JLabel("État:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterLabel.setForeground(TEXT_PRIMARY);
        
        cbFiltreEtat = new JComboBox<>(new String[]{"TOUS", "EN_COURS", "VALIDÉE", "ANNULÉE"});
        cbFiltreEtat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltreEtat.setPreferredSize(new Dimension(150, 38));
        cbFiltreEtat.setBackground(Color.WHITE);
        
        leftToolbar.add(Box.createHorizontalStrut(10));
        leftToolbar.add(filterLabel);
        leftToolbar.add(cbFiltreEtat);
        
        toolbarPanel.add(leftToolbar, BorderLayout.WEST);
        
        // Boutons
        JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightToolbar.setBackground(CARD_COLOR);
        
        btnNouvelle = createStyledButton(" Nouvelle", SUCCESS_COLOR);
        btnVoirDetail = createStyledButton(" Détails", PRIMARY_COLOR);
        btnAnnuler = createStyledButton(" Annuler", DANGER_COLOR);
        btnExporter = createStyledButton("Exporter", new Color(52, 152, 219));
        btnActualiser = createStyledButton(" Actualiser", new Color(149, 165, 166));
        
        rightToolbar.add(btnNouvelle);
        rightToolbar.add(btnVoirDetail);
        rightToolbar.add(btnAnnuler);
        rightToolbar.add(btnExporter);
        rightToolbar.add(btnActualiser);
        
        toolbarPanel.add(rightToolbar, BorderLayout.EAST);
        contentPanel.add(toolbarPanel, BorderLayout.NORTH);
        
        // Table - COLONNES DANS LE BON ORDRE: ID, Date, État, Total, Articles
        String[] columns = {"ID", "Date & Heure", "État", "Total (FCFA)", "Articles"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCommandes = new JTable(tableModel);
        tableCommandes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCommandes.setRowHeight(50);
        tableCommandes.setGridColor(new Color(240, 240, 240));
        tableCommandes.setSelectionBackground(new Color(232, 240, 254));
        tableCommandes.setSelectionForeground(TEXT_PRIMARY);
        tableCommandes.setShowVerticalLines(false);
        
        JTableHeader header = tableCommandes.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
        
        tableCommandes.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableCommandes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableCommandes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableCommandes.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableCommandes.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // CENTRER TOUTES LES COLONNES
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tableCommandes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableCommandes.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableCommandes.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Badge pour État (colonne 2)
        tableCommandes.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                
                String etat = value.toString();
                switch (etat) {
                    case "VALIDÉE":
                        label.setText(" VALIDÉE");
                        label.setBackground(new Color(212, 237, 218));
                        label.setForeground(new Color(21, 87, 36));
                        break;
                    case "EN_COURS":
                        label.setText(" EN COURS");
                        label.setBackground(new Color(255, 243, 205));
                        label.setForeground(new Color(133, 100, 4));
                        break;
                    case "ANNULÉE":
                        label.setText(" ANNULÉE");
                        label.setBackground(new Color(248, 215, 218));
                        label.setForeground(new Color(114, 28, 36));
                        break;
                    default:
                        label.setBackground(Color.WHITE);
                        label.setForeground(TEXT_PRIMARY);
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                }
                
                return label;
            }
        });
        
        // Total centré et en vert (colonne 3)
        tableCommandes.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(SUCCESS_COLOR);
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCommandes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        return centerPanel;
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
    
    private void setupListeners() {
        cbFiltreEtat.addActionListener(e -> filtrerCommandes());
        
        txtRecherche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rechercherCommandes();
            }
        });
        
        btnNouvelle.addActionListener(e -> {
            NouvelleCommandeView nouvelleCommande = new NouvelleCommandeView();
            nouvelleCommande.setVisible(true);
            nouvelleCommande.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    chargerCommandes();
                }
            });
        });
        
        btnActualiser.addActionListener(e -> chargerCommandes());
        btnVoirDetail.addActionListener(e -> voirDetailCommande());
        btnAnnuler.addActionListener(e -> annulerCommande());
        btnExporter.addActionListener(e -> exporterCommandes());
        
        tableCommandes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    voirDetailCommande();
                }
            }
        });
    }
    
    private void chargerCommandes() {
        tableModel.setRowCount(0);
        
        List<Commande> commandes = commandeDAO.getAll();
        
        int totalCommandes = commandes.size();
        int nbValidees = 0;
        int nbEnCours = 0;
        int nbAnnulees = 0;
        
        for (Commande commande : commandes) {
            commande.setLignes(ligneCommandeDAO.getByCommande(commande.getId()));
            
            int nbArticles = 0;
            if (commande.getLignes() != null) {
                nbArticles = commande.getLignes().size();
            }
            
            // ORDRE: ID, Date, État, Total, Articles
            Object[] row = {
                commande.getId(),
                dateFormat.format(commande.getDateCommande()),
                commande.getEtat(),
                String.format("%,d", (int)commande.getTotal()),
                nbArticles
            };
            tableModel.addRow(row);
            
            switch (commande.getEtat()) {
                case "VALIDÉE":
                    nbValidees++;
                    break;
                case "EN_COURS":
                    nbEnCours++;
                    break;
                case "ANNULÉE":
                    nbAnnulees++;
                    break;
            }
        }
        
        // Mise à jour des statistiques
        lblTotal.setText(String.valueOf(totalCommandes));
        lblValidees.setText(String.valueOf(nbValidees));
        lblEnCours.setText(String.valueOf(nbEnCours));
        lblAnnulees.setText(String.valueOf(nbAnnulees));
    }
    
    private void filtrerCommandes() {
        String etatFiltre = (String) cbFiltreEtat.getSelectedItem();
        
        if (etatFiltre.equals("TOUS")) {
            chargerCommandes();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Commande> commandes = commandeDAO.getByEtat(etatFiltre);
        
        for (Commande commande : commandes) {
            commande.setLignes(ligneCommandeDAO.getByCommande(commande.getId()));
            
            int nbArticles = 0;
            if (commande.getLignes() != null) {
                nbArticles = commande.getLignes().size();
            }
            
            Object[] row = {
                commande.getId(),
                dateFormat.format(commande.getDateCommande()),
                commande.getEtat(),
                String.format("%,d", (int)commande.getTotal()),
                nbArticles
            };
            tableModel.addRow(row);
        }
    }
    
    private void rechercherCommandes() {
        String recherche = txtRecherche.getText().toLowerCase().trim();
        
        if (recherche.isEmpty()) {
            chargerCommandes();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Commande> commandes = commandeDAO.getAll();
        
        for (Commande commande : commandes) {
            String idStr = String.valueOf(commande.getId());
            String dateStr = dateFormat.format(commande.getDateCommande()).toLowerCase();
            String etatStr = commande.getEtat().toLowerCase();
            
            if (idStr.contains(recherche) || dateStr.contains(recherche) || etatStr.contains(recherche)) {
                commande.setLignes(ligneCommandeDAO.getByCommande(commande.getId()));
                
                int nbArticles = 0;
                if (commande.getLignes() != null) {
                    nbArticles = commande.getLignes().size();
                }
                
                Object[] row = {
                    commande.getId(),
                    dateFormat.format(commande.getDateCommande()),
                    commande.getEtat(),
                    String.format("%,d", (int)commande.getTotal()),
                    nbArticles
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void voirDetailCommande() {
        int selectedRow = tableCommandes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner une commande",
                "Sélection requise",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int commandeId = (int) tableModel.getValueAt(selectedRow, 0);
        Commande commande = commandeDAO.read(commandeId);
        
        if (commande != null) {
            DetailCommandeView dialog = new DetailCommandeView(this, commande);
            dialog.setVisible(true);
        }
    }
    
    private void annulerCommande() {
        int selectedRow = tableCommandes.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner une commande",
                "Sélection requise",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int commandeId = (int) tableModel.getValueAt(selectedRow, 0);
        String etat = (String) tableModel.getValueAt(selectedRow, 2);
        
        if (etat.equals("ANNULÉE")) {
            JOptionPane.showMessageDialog(this,
                "ℹ️ Cette commande est déjà annulée",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String message;
        if (etat.equals("VALIDÉE")) {
            message = "⚠️ Cette commande est déjà validée. L'annulation remettra les produits en stock.\n\n" +
                     "Êtes-vous sûr de vouloir annuler la commande #" + commandeId + " ?";
        } else {
            message = "Êtes-vous sûr de vouloir annuler la commande #" + commandeId + " ?";
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Confirmation d'annulation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (etat.equals("VALIDÉE")) {
                    commandeDAO.annulerCommande(commandeId);
                } else {
                    Commande commande = commandeDAO.read(commandeId);
                    commande.setEtat("ANNULÉE");
                    commandeDAO.update(commande);
                }
                
                JOptionPane.showMessageDialog(this,
                    "✅ Commande annulée avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                chargerCommandes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de l'annulation : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exporterCommandes() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter les commandes");
        fileChooser.setSelectedFile(new java.io.File("commandes_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                writer.println("ID;Date;État;Total;Articles");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(
                        tableModel.getValueAt(i, 0) + ";" +
                        tableModel.getValueAt(i, 1) + ";" +
                        tableModel.getValueAt(i, 2) + ";" +
                        tableModel.getValueAt(i, 3) + ";" +
                        tableModel.getValueAt(i, 4)
                    );
                }
                
                writer.close();
                
                JOptionPane.showMessageDialog(this,
                    "✅ Export terminé avec succès !\n\nFichier : " + file.getAbsolutePath(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de l'export : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}