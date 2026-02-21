package com.restaurant.views;

import com.restaurant.dao.MouvementStockDAO;
import com.restaurant.models.MouvementStock;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class HistoriqueStockView extends JFrame {
    private JTable tableMouvements;
    private DefaultTableModel tableModel;
    private JButton btnActualiser, btnFiltrer, btnExporter, btnFermer;
    private JComboBox<String> cbFiltreType;
    private JTextField txtDateDebut, txtDateFin;
    
    private MouvementStockDAO mouvementStockDAO;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat filterDateFormat;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(230, 126, 34);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    private final Color TABLE_HEADER_COLOR = new Color(248, 249, 250);
    
    public HistoriqueStockView() {
        initComponents();
        chargerHistorique();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Historique des Mouvements de Stock");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mouvementStockDAO = new MouvementStockDAO();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitre = new JLabel("Historique des Mouvements de Stock");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);
        
        headerPanel.add(lblTitre, BorderLayout.WEST);
        
        /* Bouton fermer dans le header
        btnFermer = createIconButton("✕", Color.WHITE, PRIMARY_COLOR.darker());
        btnFermer.addActionListener(e -> dispose());
        headerPanel.add(btnFermer, BorderLayout.EAST);*/
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de contenu
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de filtres
        JPanel filterPanel = createFilterPanel();
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Table des mouvements
        JScrollPane tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createFilterLabel("Type"), gbc);
        
        gbc.gridx = 1;
        cbFiltreType = new JComboBox<>(new String[]{"Tous", "ENTRÉE", "SORTIE"});
        cbFiltreType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbFiltreType.setPreferredSize(new Dimension(120, 35));
        panel.add(cbFiltreType, gbc);
        
        // Date début
        gbc.gridx = 2;
        panel.add(createFilterLabel("Du"), gbc);
        
        gbc.gridx = 3;
        txtDateDebut = createFilterField("30/01/2026");
        panel.add(txtDateDebut, gbc);
        
        // Date fin
        gbc.gridx = 4;
        panel.add(createFilterLabel("Au"), gbc);
        
        gbc.gridx = 5;
        txtDateFin = createFilterField("31/01/2026");
        panel.add(txtDateFin, gbc);
        
        // Boutons
        gbc.gridx = 6;
        gbc.fill = GridBagConstraints.NONE;
        btnFiltrer = createSmallButton("Filtrer", PRIMARY_COLOR);
        btnFiltrer.addActionListener(e -> filtrerHistorique());
        panel.add(btnFiltrer, gbc);
        
        gbc.gridx = 7;
        btnActualiser = createSmallButton("Actualiser", SUCCESS_COLOR);
        btnActualiser.addActionListener(e -> chargerHistorique());
        panel.add(btnActualiser, gbc);
        
        gbc.gridx = 8;
        btnExporter = createSmallButton("Exporter", WARNING_COLOR);
        btnExporter.addActionListener(e -> exporterHistorique());
        panel.add(btnExporter, gbc);
        
        return panel;
    }
    
    private JLabel createFilterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    private JTextField createFilterField(String text) {
        JTextField field = new JTextField(text, 10);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(120, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JButton createSmallButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private JScrollPane createTablePanel() {
        // Colonnes du tableau
        String[] columns = {"ID", "Date", "Type", "Produit", "Quantité", "Motif", "Stock après"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 4 || column == 6) return Integer.class;
                return String.class;
            }
        };
        
        tableMouvements = new JTable(tableModel);
        tableMouvements.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableMouvements.setRowHeight(35);
        tableMouvements.setSelectionBackground(new Color(41, 128, 185, 50));
        tableMouvements.setSelectionForeground(TEXT_PRIMARY);
        tableMouvements.setGridColor(new Color(240, 240, 240));
        tableMouvements.setShowGrid(true);
        
        // Style du header
        tableMouvements.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableMouvements.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        tableMouvements.getTableHeader().setForeground(TEXT_PRIMARY);
        tableMouvements.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableMouvements.getTableHeader().setReorderingAllowed(false);
        
        // Largeur des colonnes
        tableMouvements.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tableMouvements.getColumnModel().getColumn(1).setPreferredWidth(140);  // Date
        tableMouvements.getColumnModel().getColumn(2).setPreferredWidth(80);   // Type
        tableMouvements.getColumnModel().getColumn(3).setPreferredWidth(180);  // Produit
        tableMouvements.getColumnModel().getColumn(4).setPreferredWidth(90);   // Quantité
        tableMouvements.getColumnModel().getColumn(5).setPreferredWidth(250);  // Motif
        tableMouvements.getColumnModel().getColumn(6).setPreferredWidth(100);  // Stock après
        
        // RENDERER pour ID - Aligné à DROITE avec padding
        DefaultTableCellRenderer idRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à droite
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        
        // RENDERER pour Date - Aligné à GAUCHE avec padding
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à gauche
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(1).setCellRenderer(dateRenderer);
        
        // RENDERER pour Type - Aligné à GAUCHE avec couleur
        tableMouvements.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String type = value.toString();
                    c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    
                    if (type.equals("ENTRÉE")) {
                        c.setForeground(SUCCESS_COLOR);
                    } else if (type.equals("SORTIE")) {
                        c.setForeground(DANGER_COLOR);
                    } else {
                        c.setForeground(TEXT_PRIMARY);
                    }
                }
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à gauche
                return c;
            }
        });
        
        // RENDERER pour Produit - Aligné à GAUCHE avec padding
        DefaultTableCellRenderer produitRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à gauche
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(3).setCellRenderer(produitRenderer);
        
        // RENDERER pour Quantité - Aligné à DROITE avec padding
        DefaultTableCellRenderer quantiteRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à droite
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(4).setCellRenderer(quantiteRenderer);
        
        // RENDERER pour Motif - Aligné à GAUCHE avec padding
        DefaultTableCellRenderer motifRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à gauche
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(5).setCellRenderer(motifRenderer);
        
        // RENDERER pour Stock après - Aligné à DROITE avec padding
        DefaultTableCellRenderer stockRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // Padding à droite
                return c;
            }
        };
        tableMouvements.getColumnModel().getColumn(6).setCellRenderer(stockRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tableMouvements);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }
    
    private JButton createIconButton(String text, Color fg, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    private void chargerHistorique() {
        tableModel.setRowCount(0);
        List<MouvementStock> mouvements = mouvementStockDAO.getHistorique();
        
        for (MouvementStock mouvement : mouvements) {
            String type = mouvement.getType();
            int quantite = mouvement.getQuantite();
            
            // Calculer le stock après mouvement
            int stockApres = mouvement.getProduit().getStockActuel();
            
            Object[] row = {
                mouvement.getId(),
                dateFormat.format(mouvement.getDateMouvement()),
                type,
                mouvement.getProduit().getNom(),
                quantite,
                mouvement.getMotif(),
                stockApres
            };
            tableModel.addRow(row);
        }
    }
    
    private void filtrerHistorique() {
        String typeFiltre = (String) cbFiltreType.getSelectedItem();
        String dateDebutStr = txtDateDebut.getText().trim();
        String dateFinStr = txtDateFin.getText().trim();
        
        try {
            // Charger tous les mouvements
            List<MouvementStock> allMouvements = mouvementStockDAO.getHistorique();
            tableModel.setRowCount(0);
            
            // Analyser les dates de filtrage
            Date dateDebutFilter = filterDateFormat.parse(dateDebutStr);
            Date dateFinFilter = filterDateFormat.parse(dateFinStr);
            
            // Ajouter un jour à la date de fin pour inclure toute la journée
            Date dateFinInclusive = new Date(dateFinFilter.getTime() + 24 * 60 * 60 * 1000 - 1000);
            
            for (MouvementStock mouvement : allMouvements) {
                String type = mouvement.getType();
                Date dateMouvement = mouvement.getDateMouvement();
                
                // Vérifier le type
                boolean typeMatch = typeFiltre.equals("Tous") || type.equals(typeFiltre);
                
                // Vérifier la date
                boolean dateMatch = dateMouvement.compareTo(dateDebutFilter) >= 0 
                                 && dateMouvement.compareTo(dateFinInclusive) <= 0;
                
                if (typeMatch && dateMatch) {
                    int stockApres = mouvement.getProduit().getStockActuel();
                    
                    Object[] row = {
                        mouvement.getId(),
                        dateFormat.format(dateMouvement),
                        type,
                        mouvement.getProduit().getNom(),
                        mouvement.getQuantite(),
                        mouvement.getMotif(),
                        stockApres
                    };
                    tableModel.addRow(row);
                }
            }
            
            // Message informatif
            int nbResultats = tableModel.getRowCount();
            if (nbResultats == 0) {
                JOptionPane.showMessageDialog(this,
                    "Aucun mouvement trouvé pour les critères spécifiés.",
                    "Résultats",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    nbResultats + " mouvements trouvés pour la période " + 
                    dateDebutStr + " - " + dateFinStr,
                    "Filtrage appliqué",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Format de date invalide. Utilisez le format JJ/MM/AAAA",
                "Erreur de date",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du filtrage: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exporterHistorique() {
        JOptionPane.showMessageDialog(this,
            "Exportation PDF - Fonctionnalité à implémenter",
            "Exportation",
            JOptionPane.INFORMATION_MESSAGE);
    }
}